package com.fhs.app.test;

import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.EMap;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.exception.ParamException;
import com.fhs.core.feign.autowired.annotation.AutowiredFhs;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.api.rpc.FeignWorkFlowApiService;
import com.fhs.flow.constant.FlowConstant;
import com.fhs.flow.service.FlowCoreService;
import com.fhs.flow.service.impl.FlowCoreServiceImpl;
import com.fhs.flow.vo.StartProcessInstanceVO;
import com.fhs.logger.anno.LogDesc;
import com.fhs.pagex.controller.PageXBaseController;
import com.fhs.pagex.service.PagexDataService;
import com.fhs.pagex.vo.PagexListSettVO;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author user
 */
@RestController
@AutowiredFhs
@RequestMapping("/webApi/test/flow")
public class TestMultilnstanceController extends PageXBaseController {

    @AutowiredFhs
    private FeignWorkFlowApiService feignWorkFlowApiService;
    @Autowired
    private FlowCoreServiceImpl flowCoreService;
    @Autowired
    private TaskService taskService;

    /**
     * 测试多实例会签
     * @param namespace
     * @param request
     * @return
     */
    @RequestMapping("{namespace}/countersign")
    @Transactional(rollbackFor = Exception.class)
    public HttpResult<Boolean> countersign(@PathVariable("namespace") String namespace, HttpServletRequest request) {
        checkPermiessAndNamespace(namespace, "add");
        EMap<String, Object> paramMap = this.getParameterMap();
        UcenterMsUserVO user = super.getSessionUser(request);
        paramMap.put("createUser", user.getUserId());
        paramMap.put("groupCode", user.getGroupCode());
        paramMap.put("updateUser", user.getUserId());
        paramMap.put("instanceStatus", FlowConstant.BUSINESS_INSTANCE_STATUS_APPROVAL);
        String pkey = StringUtil.getUUID();
        paramMap.put("pkey", pkey);
        super.setDB(PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace));
        //addLog(namespace, "添加", paramMap, request, LogDesc.ADD);
        PagexListSettVO listPageSett = PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace);
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> userIdList = new ArrayList<>();
        userIdList.add("1");
        map.put("userIdList", userIdList);
        Object increase = paramMap.get("increase");
        map.put("increase", increase);
        StartProcessInstanceVO startProcessInstance = new StartProcessInstanceVO();
        startProcessInstance.setBusinessKey(pkey);
        startProcessInstance.setVariables(map);
        startProcessInstance.setUserId(user.getUserId());
        startProcessInstance.setExtFormParam(new HashMap<>());
        startProcessInstance.setProcessDefinitionKey(ConverterUtils.toString(listPageSett.getModelConfig().get("processDefinitionKey")));
        HttpResult<String> stringHttpResult = feignWorkFlowApiService.startProcessInstanceForApi(startProcessInstance);
        if (stringHttpResult.getCode() != Constant.SUCCESS_CODE) {
            throw new ParamException("调用流程启动错误");
        }
        paramMap.put("instanceId", stringHttpResult.getData());
        service.insert(paramMap, namespace);
        refreshPageXTransCache(namespace);
        return HttpResult.success(true);
    }
}
