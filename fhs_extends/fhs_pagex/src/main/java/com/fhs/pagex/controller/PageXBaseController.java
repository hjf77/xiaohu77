package com.fhs.pagex.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fhs.basics.vo.SettMsMenuVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.context.BisLoggerContext;
import com.fhs.bislogger.api.rpc.FeignBisLoggerApiService;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.EMap;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.NetworkUtil;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.db.ds.ReadWriteDataSourceDecision;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.logger.Logger;
import com.fhs.pagex.service.JoinService;
import com.fhs.pagex.service.ListExtendsHanleService;
import com.fhs.pagex.service.PageXDBService;
import com.fhs.pagex.service.PagexDataService;
import com.fhs.pagex.vo.PagexBaseVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * pagex处理前段业务逻辑的公共代码
 * @author user
 * @date 2020-05-19 13:59:30
 */
public class PageXBaseController extends BaseController {

    private final Logger LOG = Logger.getLogger(PageXBaseController.class);

    protected static HttpResult errorResult = HttpResult.error(Constant.BFALSE);

    protected static HttpResult successResult = HttpResult.success(Constant.BTRUE);

    // 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行.
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    protected PageXDBService service;

    @Autowired
    protected JoinService joinService;


    @Autowired
    private RedisCacheService<String> redisCacheService;

    @Autowired
    protected ListExtendsHanleService listExtendsHanleService;

    private FeignBisLoggerApiService feignBisLoggerApiService;

    /**
     * namesapce:menu map
     */
    protected Map<String, SettMsMenuVO> namesapceMenuMap = new HashMap<>();

    /**
     * 根据modelconfig中的db配置，来设置本操作连接的数据库
     *
     * @param pagexBaseDTO pagexBaseDTO
     */
    protected void setDB(PagexBaseVO pagexBaseDTO) {
        if (pagexBaseDTO != null) {
            if (pagexBaseDTO.getModelConfig().containsKey("db")) {
                ReadWriteDataSourceDecision.markParam();
                ReadWriteDataSourceDecision.setDataSource(ConverterUtils.toString(pagexBaseDTO.getModelConfig().get("db")));
            }
        }
    }


    /**
     * 获取session中的用户
     *
     * @param request request
     * @return 系统用户
     */
    protected UcenterMsUserVO getSessionUser(HttpServletRequest request) {
        return (UcenterMsUserVO) request.getSession().getAttribute(Constant.SESSION_USER);
    }





    /**
     * 刷新namespace 翻译缓存
     *
     * @param namespace namespace
     */
    protected void refreshPageXTransCache(String namespace) {
        Map<String, String> message = new HashMap<>();
        message.put("transType", "pagex");
        message.put("namespace", namespace);
        redisCacheService.convertAndSend("trans", JsonUtils.map2json(message));
    }

    /**
     * 校验权限和namespace是否存在
     *
     * @param namespace       namespace
     * @param permiessionCode 权限
     */
    protected void checkPermiessAndNamespace(String namespace, String permiessionCode) {
        if (!SecurityUtils.getSubject().isPermitted(namespace + ":" + permiessionCode)) {
            throw new NotPremissionException();

        }
        if (!PagexDataService.SIGNEL.getPagexListSettDTOCache().containsKey(namespace)) {
            throw new ParamException("namespace不存在");
        }
    }

    /**
     * 添加日志
     *
     * @param namespace namespace
     * @param result      描述
     * @param paramMap  参数
     * @param request   request
     * @param type      操作类型
     */
    protected void addLog(String namespace, String result, Map<String, Object> paramMap, HttpServletRequest request, int type,Exception e) {
        if (feignBisLoggerApiService == null) {
            feignBisLoggerApiService = SpringContextUtil.getBeanByClassForApi(FeignBisLoggerApiService.class);
        }
        UcenterMsUserVO user = getSessionUser(request);
        LogOperatorMainVO logMainVO = new LogOperatorMainVO();
        logMainVO.setLogId(BisLoggerContext.getTranceId());
        logMainVO.preInsert(user.getUserId());
        logMainVO.setModel(PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace).getModelConfig().get("title").toString());
        logMainVO.setUrl(request.getRequestURI());
        logMainVO.setType(type);
        logMainVO.setNamespace(namespace);
        logMainVO.setReqParam(JsonUtils.map2json(paramMap));
        logMainVO.setReqMethod(request.getMethod());
        logMainVO.setRespBody(e == null ? JsonUtils.object2json(result) : e.getMessage());
        logMainVO.setState(e == null ? LoggerConstant.LOG_STATE_SUCCESS : LoggerConstant.LOG_STATE_ERROR);
        logMainVO.setIsDelete((e == null && type == LoggerConstant.METHOD_TYPE_DEL)?LoggerConstant.HAS_SOFT_DEL:LoggerConstant.SOFT_DEL);
        try {
            logMainVO.setIp(NetworkUtil.getIpAddress(request));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        LogAddOperatorLogVO logVO = new LogAddOperatorLogVO();
        logVO.setOperatorMainVO(logMainVO);
        logVO.setOperatorExtParamVOList(BisLoggerContext.getLogOperatorExtParamList());
        logVO.setHistoryDataVOList(BisLoggerContext.getLogHistoryDataVOList());
        addLog(logVO);
    }

    /**
     * 异步入库
     * @param log
     */
    private void addLog(LogAddOperatorLogVO log) {
        singleThreadExecutor.submit(() -> {
            try{
                feignBisLoggerApiService.addLog(log);
            }catch (Exception e){
                LOG.error("日志插入错误",e);
            }
        });
    }

    /**
     * 查询列表数据并且调用joinservice 填充
     *
     * @param namespace namespace
     * @param paramMap  参数
     * @return 填充好的JSONArray
     */
    protected JSONArray findListDataAndInitJoin(String namespace, Map<String, Object> paramMap) {
        this.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
        String resultJson = service.findListPage(paramMap, namespace);
        JSONArray rows = JSONObject.parseArray(resultJson);
        this.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
        rows = joinService.initJoinData(rows, namespace);
        listExtendsHanleService.processingData(namespace, rows);
        return rows;
    }
}
