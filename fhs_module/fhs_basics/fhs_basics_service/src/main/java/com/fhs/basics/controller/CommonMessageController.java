package com.fhs.basics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.po.CommonMessagePO;
import com.fhs.basics.service.CommonMessageService;
import com.fhs.basics.vo.CommonMessageVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.JsonUtils;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * 消息推送表(CommonMessage)表控制层
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */

@RestController
@Api(tags = {"消息推送表"})
@RequestMapping("/ms/commonMessage")
public class CommonMessageController extends ModelSuperController<CommonMessageVO, CommonMessagePO, Long> {

    @Autowired
    private CommonMessageService commonMessageService;

    private static final Gson gson = new Gson().newBuilder().create();

    @ResponseBody
    @PostMapping({"delMessages"})
    @ApiOperation("批量删除消息")
    @LogMethod(type = 2, pkeyParamIndex = 0)
    public HttpResult<Boolean> delMessages(@RequestBody List<Long> ids) {
        if (!ids.isEmpty()) {
            int count = commonMessageService.deleteBatchIds(ids);
            if (count > 0) {
                return HttpResult.success(true);
            }
        }
        throw new ParamException("请选择需要删除的消息!");
    }
    @ResponseBody
    @PostMapping({"saveMessage"})
    @ApiOperation("新增消息推送")
    public HttpResult<Boolean> saveMessage(@RequestBody String messageJson){
        Map<String, Object> messageJsonMap = JsonUtils.parseJSON2Map(gson.toJson(messageJson));
        CommonMessagePO commonMessagePO = new CommonMessagePO();
        commonMessagePO.setIsAlert(Integer.parseInt(messageJsonMap.get("isAlert").toString()));
        commonMessagePO.setIsRead(Constant.ZERO);
        commonMessagePO.setIsRelease(Constant.ZERO);

        //英文
        commonMessagePO.setTitle(messageJsonMap.get("titleEn").toString());
        //commonMessagePO.setContent(messageJsonMap.get("contentEn").toString());
        commonMessagePO.setMsgLanguage(messageJsonMap.get("areaEn").toString());
        commonMessageService.insert(commonMessagePO);

        //中文
        commonMessagePO.setTitle(messageJsonMap.get("titleZh").toString());
       // commonMessagePO.setContent(messageJsonMap.get("contentZH").toString());
        commonMessagePO.setMsgLanguage(messageJsonMap.get("areaZh").toString());
        commonMessageService.insert(commonMessagePO);

        //阿拉伯文
        commonMessagePO.setTitle(messageJsonMap.get("titleAr").toString());
       // commonMessagePO.setContent(messageJsonMap.get("contentAr").toString());
        commonMessagePO.setMsgLanguage(messageJsonMap.get("areaAr").toString());
        commonMessageService.insert(commonMessagePO);
        return HttpResult.success(true);
    }

    /*
     */
/**
 * 无分页查询bean列表数据
 *
 * @throws Exception
 *//*

    @ResponseBody
    @GetMapping("findCommonMsgList")
    @ApiOperation("后台-不分页查询集合-一般用于下拉")
    public Map<String, List<CommonMessageVO>> findCommonMsgList(CommonMessageVO commonMessageVO) throws Exception {
        if (isPermitted("see")) {
            commonMessageVO.setUserId(this.getSessionuser().getUserId());
            List<CommonMessageVO> dataList = baseService.findForList(commonMessageVO);

            Map<String, List<CommonMessageVO>> dateListMap = dataList.subList(0, 9).stream().collect(Collectors.groupingBy(CommonMessagePO::getNoticeDate));
            return dateListMap;
        } else {
            throw new NotPremissionException();
        }
    }
*/


    /**
     * 查询bean列表数据
     *
     * @throws Exception
     *//*
    @ResponseBody
    @PostMapping("findCommonMsgList")
    @ApiOperation("后台-高级分页查询根据通知时间分组")
    public Map<String, List<CommonMessageVO>> findCommonMsgList(@RequestBody QueryFilter<CommonMessagePO> filter) {
        if (isPermitted("see")) {
            QueryWrapper wrapper = filter.asWrapper(getDOClass());
            wrapper.eq("user_id", this.getSessionuser().getUserId());
            wrapper.orderByDesc("create_time");
            //这里的是1是DO的indexo
            IPage<CommonMessageVO> page = baseService.selectPageMP(filter.getPagerInfo(), wrapper);
            return page.getRecords().stream().collect(Collectors.groupingBy(CommonMessagePO::getNoticeDate, TreeMap::new, Collectors.toList())).descendingMap();
        } else {
            throw new NotPremissionException();
        }
    }*/
}
