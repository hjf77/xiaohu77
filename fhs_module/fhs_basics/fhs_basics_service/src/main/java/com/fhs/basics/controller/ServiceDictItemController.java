package com.fhs.basics.controller;

import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典管理controller
 *
 * @author wanglei
 * @date 2020-05-18 16:52:33
 */
@RestController
@RequestMapping("ms/dictItem")
@Api(tags = {"字典"})
@ApiGroup(group = "group_default")
@LogNamespace(namespace = "dictItem", module = "字典项管理")
public class ServiceDictItemController extends ModelSuperController<ServiceDictItemVO, ServiceDictItemPO> {

    /**
     * 重写去掉权限校验
     * @param e
     * @param request  response
     * @return
     * @throws Exception
     */
    @Override
    public List<ServiceDictItemVO> findList(ServiceDictItemVO e, HttpServletRequest request) throws Exception {
        List<ServiceDictItemVO> dataList = baseService.findForList( e);
        return dataList;
    }
}
