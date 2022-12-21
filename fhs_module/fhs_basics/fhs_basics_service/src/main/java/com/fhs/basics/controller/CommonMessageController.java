package com.fhs.basics.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.constant.ExceptionConstant;
import com.fhs.basics.po.CommonMessagePO;
import com.fhs.basics.service.CommonMessageService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.CommonMessageVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.JsonUtils;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.exception.BusinessException;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private UcenterMsUserService userService;

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
        throw new BusinessException(ExceptionConstant.CHOICE_DELETE_MESSAGE);
    }

    @ResponseBody
    @PostMapping({"saveMessage"})
    @ApiOperation("新增消息推送")
    public HttpResult<Boolean> saveMessage(@RequestBody String messageJson) {
        Map<String, Object> messageJsonMapTemp = JsonUtils.parseJSON2Map(messageJson);
        Map<String, Object> messageJsonMap = JsonUtils.parseJSON2Map(messageJsonMapTemp.get("messageJson").toString());
        CommonMessagePO commonMessagePO = new CommonMessagePO();
        if (null != messageJsonMapTemp.get("id")) {
            CommonMessageVO commonMessageVO = commonMessageService.selectById(Long.parseLong(messageJsonMapTemp.get("id").toString()));
            commonMessagePO.setId(commonMessageVO.getId());
        }


        //英文
        CommonMessagePO commonMessageEn = new CommonMessagePO();
        commonMessageEn.setMsgLanguage(Constant.STR_NO);
        commonMessageEn.setTitle(messageJsonMap.get("titleEn").toString());
        commonMessageEn.setMsgContent(messageJsonMap.get("contentEn").toString());

        //中文
        CommonMessagePO commonMessageZh = new CommonMessagePO();
        commonMessageZh.setMsgLanguage(Constant.STR_YES);
        commonMessageZh.setTitle(messageJsonMap.get("titleZh").toString());
        commonMessageZh.setMsgContent(messageJsonMap.get("contentZh").toString());


        //阿拉伯文
        CommonMessagePO commonMessageAr = new CommonMessagePO();
        commonMessageAr.setMsgLanguage(Constant.CHECK_ING);
        commonMessageAr.setTitle(messageJsonMap.get("titleAr").toString());
        commonMessageAr.setMsgContent(messageJsonMap.get("contentAr").toString());

        BeanUtils.copyProperties(commonMessageZh, commonMessagePO);
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("messageCh", JsonUtils.bean2json(commonMessageZh));
        messageMap.put("messageAr", JsonUtils.bean2json(commonMessageAr));
        messageMap.put("messageEn", JsonUtils.bean2json(commonMessageEn));
        commonMessagePO.setMessageAttach(JsonUtils.map2json(messageMap));

        commonMessagePO.setIsAlert(Integer.parseInt(messageJsonMap.get("isAlertName").toString()));
        commonMessagePO.setArea(messageJsonMap.get("area").toString());
        commonMessagePO.setIsRead(Constant.ZERO);
        commonMessagePO.setIsRelease(Constant.ZERO);
        commonMessagePO.setIsAlert(Integer.parseInt(messageJsonMap.get("isAlertName").toString()));
        if (null != messageJsonMapTemp.get("id")) {
            CommonMessageVO commonMessageVO = commonMessageService.selectById(Long.parseLong(messageJsonMapTemp.get("id").toString()));
            commonMessagePO.setId(commonMessageVO.getId());
        }
        if (null != commonMessagePO.getId()) {
            commonMessageService.updateById(commonMessagePO);
        } else {
            commonMessageService.insert(commonMessagePO);
        }

        return HttpResult.success(true);
    }


    /**
     * 根据ID集合查询对象数据
     *
     * @param id
     * @return
     * @throws Exception
     */
    @LogMethod
    @ResponseBody
    @GetMapping("findDetail")
    @ApiOperation("根据id获取单挑数据信息")
    public CommonMessageVO info(@RequestParam("id") Long id) {
        CommonMessageVO commonMessageVO = commonMessageService.selectById(id);
        String messageAttach = commonMessageVO.getMessageAttach();
        Map<String, Object> messageMap = JsonUtils.parseJSON2Map(messageAttach);
        Map<String, CommonMessagePO> messageVOMap = new HashMap<>();
        messageVOMap.put("messageEn", JSON.parseObject(messageMap.get("messageEn").toString(), CommonMessageVO.class));
        messageVOMap.put("messageCh", JSON.parseObject(messageMap.get("messageCh").toString(), CommonMessageVO.class));
        messageVOMap.put("messageAr", JSON.parseObject(messageMap.get("messageAr").toString(), CommonMessageVO.class));
        commonMessageVO.setCommonMessageVOMap(messageVOMap);
        return commonMessageVO;
    }


    @Override
    @ResponseBody
    @PostMapping("pagerAdvance")
    @ApiOperation("后台-高级分页查询")
    public IPage<CommonMessageVO> findPagerAdvance(@RequestBody QueryFilter<CommonMessagePO> filter) {
        if (isPermitted("see")) {
            QueryWrapper wrapper = filter.asWrapper(getDOClass());
            Long userId = this.getSessionuser().getUserId();
            List<Map<String, Object>> userRoleMaps = userService.searchUserRole(userService.selectById(userId));
            wrapper.eq("user_id", userId);
            for (Map<String, Object> roleMap : userRoleMaps) {
                if (roleMap.get("roleId").equals(Constant.THREE)) {
                    wrapper.clear();
                    wrapper = filter.asWrapper(getDOClass());
                    break;
                }
            }
            //wrapper.isNull("msg_type");
            this.setExportCache(wrapper);
            //这里的是1是DO的index
            return baseService.selectPageMP(filter.getPagerInfo(), wrapper);
        } else {
            throw new NotPremissionException();
        }
    }

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
            return page.getRecords().stream().collect(Collectors.groupingBy(CommonMessagePO::getDateTime, TreeMap::new, Collectors.toList())).descendingMap();
        } else {
            throw new NotPremissionException();
        }
    }*/
}
