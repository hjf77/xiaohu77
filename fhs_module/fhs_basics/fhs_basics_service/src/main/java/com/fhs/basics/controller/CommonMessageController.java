package com.fhs.basics.controller;

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
import com.fhs.core.exception.ParamException;
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
import java.util.UUID;


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
        boolean idEnTemp = messageJsonMap.containsKey("idEn");
        if (!idEnTemp) {
            commonMessagePO.setIsAlert(Integer.parseInt(messageJsonMap.get("isAlertName").toString()));
            commonMessagePO.setRelationId(UUID.randomUUID().toString());
            commonMessagePO.setIsRead(Constant.ZERO);
            commonMessagePO.setIsRelease(Constant.ZERO);
        }

        //英文
        CommonMessagePO commonMessageEn = new CommonMessagePO();
        commonMessagePO.setIsAlert(Integer.parseInt(messageJsonMap.get("isAlertName").toString()));
        BeanUtils.copyProperties(commonMessagePO, commonMessageEn);
        boolean idEn = messageJsonMap.containsKey("idEn");
        commonMessageEn.setMsgLanguage(Constant.STR_NO);
        commonMessageEn.setTitle(messageJsonMap.get("titleEn").toString());
        commonMessageEn.setMsgContent(messageJsonMap.get("contentEn").toString());
        commonMessageEn.setArea(messageJsonMap.get("areaEn").toString());
        if (!idEn) {
            commonMessageService.insert(commonMessageEn);
        } else {
            commonMessageEn.setId(Long.parseLong(messageJsonMap.get("idEn").toString()));
            commonMessageService.updateById(commonMessageEn);
        }

        //中文
        CommonMessagePO commonMessageZh = new CommonMessagePO();
        BeanUtils.copyProperties(commonMessagePO, commonMessageZh);
        boolean idZh = messageJsonMap.containsKey("idZh");
        commonMessageZh.setMsgLanguage(Constant.STR_YES);
        commonMessageZh.setTitle(messageJsonMap.get("titleZh").toString());
        commonMessageZh.setMsgContent(messageJsonMap.get("contentZh").toString());
        commonMessageZh.setArea(messageJsonMap.get("areaZh").toString());
        if (!idZh) {
            commonMessageService.insert(commonMessageZh);
        } else {
            commonMessageZh.setId(Long.parseLong(messageJsonMap.get("idZh").toString()));
            commonMessageService.updateById(commonMessageZh);
        }

        //阿拉伯文
        CommonMessagePO commonMessageAr = new CommonMessagePO();
        BeanUtils.copyProperties(commonMessagePO, commonMessageAr);
        boolean idAr = messageJsonMap.containsKey("idAr");
        commonMessageAr.setMsgLanguage(Constant.CHECK_ING);
        commonMessageAr.setTitle(messageJsonMap.get("titleAr").toString());
        commonMessageAr.setMsgContent(messageJsonMap.get("contentAr").toString());
        commonMessageAr.setArea(messageJsonMap.get("areaAr").toString());
        if (!idAr) {
            commonMessageService.insert(commonMessageAr);
        } else {
            commonMessageAr.setId(Long.parseLong(messageJsonMap.get("idAr").toString()));
            commonMessageService.updateById(commonMessageAr);
        }
        return HttpResult.success(true);
    }


    /**
     * 根据ID集合查询对象数据
     *
     * @param relationId relationId
     * @return
     * @throws Exception
     */
    @LogMethod
    @ResponseBody
    @GetMapping("findDetail")
    @ApiOperation("根据id获取单挑数据信息")
    public Map<String, CommonMessageVO> info(@RequestParam("relationId") String relationId) {
        Map<String, CommonMessageVO> messageVOMap = new HashMap<>();
        List<CommonMessageVO> commonMessageVOS = commonMessageService.findForList(CommonMessagePO.builder().relationId(relationId).build());
        commonMessageVOS.forEach(c -> {
            if (c.getMsgLanguage().equals("0")) {
                messageVOMap.put("messageEn", c);
            }
            if (c.getMsgLanguage().equals("1")) {
                messageVOMap.put("messageCh", c);
            }
            if (c.getMsgLanguage().equals("2")) {
                messageVOMap.put("messageAr", c);
            }
        });
        return messageVOMap;
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
