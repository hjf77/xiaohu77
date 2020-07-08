package com.fhs.pagex.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fhs.basics.api.rpc.FeignSysMenuApiService;
import com.fhs.basics.vo.SettMsMenuVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.context.BisLoggerContext;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.exception.ParamException;
import com.fhs.core.feign.autowired.annotation.AutowiredFhs;
import com.fhs.core.result.HttpResult;
import com.fhs.core.trans.service.impl.TransService;
import com.fhs.logger.Logger;
import com.fhs.logger.anno.LogDesc;
import com.fhs.pagex.common.ExcelExportTools;
import com.fhs.pagex.service.PagexDataService;
import com.fhs.pagex.vo.PageXTreeVO;
import com.fhs.pagex.vo.TreeVO;
import com.mybatis.jpa.context.DataPermissonContext;
import com.mybatis.jpa.context.MultiTenancyContext;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * pagex的控制器
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex
 * @ClassName: PageXPubAction
 * @Author: JackWang
 * @CreateDate: 2018/12/3 0003 19:44
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/12/3 0003 19:44
 * @Version: 1.0
 */
@RestController
@AutowiredFhs
@RequestMapping("/ms/x/")
public class PageXMsPubController extends PageXBaseController {

    private static Logger LOG = Logger.getLogger(PageXMsPubController.class);

    @Autowired
    private TransService transService;

    /**
     * 添加-后台只做重复校验，不做参数格式校验
     *
     * @param namespace namespace
     */
    @RequestMapping("{namespace}/add")
    public HttpResult<Boolean> add(@PathVariable("namespace") String namespace, HttpServletRequest request, HttpServletResponse response) {

        checkPermiessAndNamespace(namespace, "add");
        EMap<String, Object> paramMap = super.getParameterMap();
        UcenterMsUserVO user = getSessionUser(request);
        paramMap.put("createUser", user.getUserId());
        paramMap.put("groupCode", user.getGroupCode());
        paramMap.put("updateUser", user.getUserId());
        String pkey =  StringUtil.getUUID();
        paramMap.put("pkey", pkey);
        super.setDB(PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace));
        HttpResult result = errorResult;
        Exception e = null;
        BisLoggerContext.init(StringUtil.getUUID());
        try {
            service.insert(paramMap, namespace);
            refreshPageXTransCache(namespace);
            result = successResult;
            addHistoryAndExtParam( pkey, namespace, LoggerConstant.METHOD_TYPE_ADD);
        }catch (Exception except){
            e = except;
            LOG.error("add出错" + namespace + ",param:" + paramMap,e);
            throw  except;
        }finally {
            addLog(namespace, JsonUtils.object2json(result), paramMap, request, LoggerConstant.METHOD_TYPE_ADD,e);
        }
        BisLoggerContext.clear();
        return result;
    }

    /**
     * 添加历史log和扩展参数
     * @param pkey  主键
     * @param namespace namespace
     * @param type 操作类型 见LoggerConstant
     */
    private void addHistoryAndExtParam(String pkey,String namespace,int type){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id", pkey);
        paramMap.put("groupCode", MultiTenancyContext.getProviderId());
        Map<String,Object>  rowData = service.findBeanMap(paramMap, namespace);
        VO vo = service.map2VO(rowData,service.getNamespaceClassMap().get(namespace));
        transService.transOne(vo);
        BisLoggerContext.addExtParam(namespace,pkey,type);
        BisLoggerContext.addHistoryData(vo,namespace);
    }


    /**
     * 获取数据详情
     *
     * @param namespace namespace
     * @param id        id
     */
    @RequestMapping("{namespace}/info/{id}")
    public JSONObject info(@PathVariable("namespace") String namespace, @PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        checkPermiessAndNamespace(namespace, "see");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("groupCode", MultiTenancyContext.getProviderId());
        super.setDB(PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace));
        JSONObject result = null;
        Exception exception = null;
        BisLoggerContext.init(StringUtil.getUUID());
        try{
            result = JSONObject.parseObject(service.findBean(paramMap, namespace));
        }catch (Exception e){
            exception = e;
            LOG.error("info出错" + namespace + ",id:" + id,e);
            throw  exception;
        }finally {
            addLog(namespace, JSONObject.toJSONString(result), paramMap, request, LoggerConstant.METHOD_TYPE_VIEW,exception);
        }
        BisLoggerContext.clear();
        return result;
    }

    /**
     * 更新
     *
     * @param namespace namespace
     * @param id        id
     */
    @RequestMapping("{namespace}/update/{id}")
    public HttpResult<Boolean> update(@PathVariable("namespace") String namespace, @PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        checkPermiessAndNamespace(namespace, "update");
        EMap<String, Object> paramMap = super.getParameterMap();
        paramMap.put("id", id);
        paramMap.put("groupCode", MultiTenancyContext.getProviderId());
        paramMap.put("updateUser", getSessionUser(request).getUserId());
        super.setDB(PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace));
        HttpResult result = errorResult;
        Exception exception = null;
        BisLoggerContext.init(StringUtil.getUUID());
        try{
            service.update(paramMap, namespace);
            refreshPageXTransCache(namespace);
            result = successResult;
            addHistoryAndExtParam( id, namespace, LoggerConstant.METHOD_TYPE_UPATE);
            return result;
        }catch (Exception e){
            exception = e;
            LOG.error("更新出错",e);
            throw  exception;
        }finally {
            addLog(namespace, JsonUtils.object2json(result), paramMap, request, LoggerConstant.METHOD_TYPE_UPATE,exception);
            BisLoggerContext.clear();
        }
    }


    /**
     * 删除
     *
     * @param namespace namespace
     * @param id        id
     */
    @RequestMapping("{namespace}/del/{id}")
    public HttpResult<Boolean> del(@PathVariable("namespace") String namespace, @PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (id.contains("jackToken")) {
            id = id.substring(0, id.indexOf("&amp;"));
        }
        checkPermiessAndNamespace(namespace, "del");
        super.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        HttpResult result = errorResult;
        Exception exception = null;
        BisLoggerContext.init(StringUtil.getUUID());
        try{
            service.del(id, namespace);
            refreshPageXTransCache(namespace);
            result = successResult;
            return result;
        }catch (Exception e){
            exception = e;
            LOG.error(e);
            throw  exception;
        }finally {
            addLog(namespace,JsonUtils.object2json(result), paramMap, request, LoggerConstant.METHOD_TYPE_DEL,exception);
            BisLoggerContext.clear();
        }
    }

    /**
     * 获取数据带分页
     *
     * @param namespace namespace
     */
    @RequestMapping("{namespace}/findPager")
    public Map<String, Object> findPager(@PathVariable("namespace") String namespace, HttpServletRequest request, HttpServletResponse response) {
        checkPermiessAndNamespace(namespace, "see");
        Map<String, Object> paramMap = super.getPageTurnNum();
        paramMap.put("dataPermissin", DataPermissonContext.getDataPermissonMap());
        paramMap.put("groupCode", MultiTenancyContext.getProviderId());
        request.getSession().setAttribute(this.getClass() + "preLoadParam", paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        JSONArray rows = super.findListDataAndInitJoin(namespace, paramMap);
        resultMap.put("rows", rows);
        super.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
        resultMap.put("total", service.findPageCount(paramMap, namespace));
        return resultMap;
    }

    /**
     * 获取ztree数据
     *
     * @param namespace namespace
     */
    @RequestMapping("{namespace}/ztreeData")
    public JSONArray ztreeData(@PathVariable("namespace") String namespace, HttpServletRequest request, HttpServletResponse response) {
        checkPermiessAndNamespace(namespace, "see");
        PageXTreeVO treeDTO = PagexDataService.SIGNEL.getPageXTreeDTOFromCache(namespace);
        if (treeDTO == null) {
            throw new ParamException("namespace:" + namespace + "不支持tree");
        }
        String fidField = ConverterUtils.toString(treeDTO.getKeySettMap().get("fidkey"));
        String pkey = ConverterUtils.toString(treeDTO.getModelConfig().get("pkey"));
        JSONArray result = new JSONArray();

        //祖宗的id
        String fkeyVal = request.getParameter("pid");
        // 所有的都查询出来
        JSONArray jsonArray = getAllListPage(namespace);
        if (CheckUtils.isNullOrEmpty(fkeyVal)) {
            return jsonArray;
        }
        //所有的 fkeyVal 的后辈都在这个集合中
        Set<String> posteritySet = new HashSet<>();

        JSONObject temp = null;

        //下面这段程序的名字叫做 他是不是我祖宗
        for (int i = 0; i < jsonArray.size(); i++) {
            temp = jsonArray.getJSONObject(i);
            //如果传进来的fkeyVal 是我的爸爸，就把我添加到返回的集合去  || 我爸爸是人家后辈，我肯定也是人家后辈
            if (temp.getString(fidField).equals(fkeyVal) || posteritySet.contains(temp.getString(fidField))) {
                result.add(temp);
                posteritySet.add(temp.getString(pkey));//他都是我爸爸了，我肯定是他的后辈
            }
        }
        return result;
    }

    /**
     * 将导出的列配置信息缓存到session中
     *
     * @param fieldSett 导出配置
     * @param request   request
     * @return 成功
     */
    @RequestMapping("{namespace}/setExportField")
    @ResponseBody
    public HttpResult setExportField(@PathVariable("namespace") String namespace, @RequestBody String fieldSett, HttpServletRequest request) {
        return ExcelExportTools.setExportField(fieldSett, request);
    }

    /**
     * 公共导出excel 03  by jackwang
     *
     * @param request  request
     * @param response response
     */
    @RequestMapping("{namespace}/pubExportExcel")
    @LogDesc(value = "导出数据", type = LogDesc.SEE)
    public void exportExcel(@PathVariable("namespace") String namespace, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> paramMap = (Map<String, Object>) request.getSession().getAttribute(this.getClass() + "preLoadParam");
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        Exception exception = null;
        BisLoggerContext.init(StringUtil.getUUID());
        try{
            //去掉分页
            paramMap.remove("start");
            paramMap.remove("end");
            paramMap.put("groupCode", MultiTenancyContext.getProviderId());
            paramMap.put("start", Constant.PAGE_ALL);
            paramMap.put("dataPermissin", DataPermissonContext.getDataPermissonMap());
            super.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
            String resultJson = service.findListPage(paramMap, namespace);
            List<JSONObject> dataList = new ArrayList<>();
            JSONArray jsonArray = JSON.parseArray(resultJson);
            jsonArray = joinService.initJoinData(jsonArray, namespace);
            listExtendsHanleService.processingData(namespace, jsonArray);
            for (int i = 0; i < jsonArray.size(); i++) {
                dataList.add(jsonArray.getJSONObject(i));
            }
            ExcelExportTools.exportExcel(dataList, request, response);
        }catch (Exception e){
            exception = e;
            LOG.error(e);
            throw  exception;
        }finally {
            addLog(namespace,Constant.STR_TRUE,paramMap,request,LoggerConstant.METHOD_TYPE_EXPORT,exception);
        }
        BisLoggerContext.clear();
    }

    /**
     * 获取数据不带分页
     *
     * @param namespace namespace
     */
    @RequestMapping("{namespace}/findListData")
    public JSONArray findListData(@PathVariable("namespace") String namespace, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> paramMap = super.getParameterMap();
        paramMap.put("start", Constant.PAGE_ALL);
        paramMap.put("dataPermissin", DataPermissonContext.getDataPermissonMap());
        paramMap.put("groupCode", MultiTenancyContext.getProviderId());
        super.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
        String resultJson = service.findListPage(paramMap, namespace);
        return JSONArray.parseArray(resultJson);
    }

    /**
     * 获取ztree数据
     *
     * @param namespace namespace
     */
    @RequestMapping("{namespace}/getTree")
    public List<TreeVO> getTree(@PathVariable("namespace") String namespace, HttpServletRequest request) {
        checkPermiessAndNamespace(namespace, "see");
        JSONArray jsonArray = getAllListPage(namespace);

        List<TreeVO> result = new ArrayList<>();
        Map<String, TreeVO> map = new HashMap();
        PageXTreeVO treeDTO = PagexDataService.SIGNEL.getPageXTreeDTOFromCache(namespace);
        if (treeDTO == null) {
            throw new ParamException("namespace:" + namespace + "不支持tree");
        }
        //父id
        String fidField = ConverterUtils.toString(treeDTO.getKeySettMap().get("fidkey"));
        //本身id
        String pkey = ConverterUtils.toString(treeDTO.getModelConfig().get("pkey"));
        //显示那个字段
        String namekey = ConverterUtils.toString(treeDTO.getKeySettMap().get("namekey"));

        for (int i = 0; i < jsonArray.size(); i++) {
            TreeVO tree = new TreeVO();
            tree.setId(jsonArray.getJSONObject(i).getString(pkey));
            tree.setParentId(jsonArray.getJSONObject(i).getString(fidField));
            tree.setText(jsonArray.getJSONObject(i).getString(namekey));
            map.put(tree.getId(), tree);
            //找爸爸
            if (map.containsKey(tree.getParentId())) {
                //和爸爸待一起
                map.get(tree.getParentId()).getChildren().add(tree);
            } else {
                result.add(tree);
            }
        }
        return result;
    }

    /**
     * 获取树形数据
     *
     * @param namespace
     * @return
     */
    private JSONArray getAllListPage(String namespace) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", -1);
        paramMap.put("end", -1);
        paramMap.put("groupCode", MultiTenancyContext.getProviderId());
        paramMap.put("sortTzwName", "create_time ASC");
        super.setDB(PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace));
        paramMap.put("dataPermissin", DataPermissonContext.getDataPermissonMap());
        // 所有的都查询出来
        return JSON.parseArray(service.findListPage(paramMap, namespace));
    }
}
