package com.fhs.basics.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaJoinQueryWrapper;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
public class ServiceDictItemController extends ModelSuperController<ServiceDictItemVO, ServiceDictItemPO, Integer> {

    /**
     * 重写去掉权限校验
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ServiceDictItemVO> findList() throws Exception {
        List<ServiceDictItemVO> dataList = baseService.selectListMP(QueryFilter.reqParam2Wrapper(baseService.getPoClass()).orderByAsc(ServiceDictItemPO::getOrderNum));
        return dataList;
    }

    @ResponseBody
    @PostMapping({"findListAdvance"})
    @ApiOperation("后台-高级查询不分页一般用于下拉")
    @Override
    public List<ServiceDictItemVO> findListAdvance(@RequestBody QueryFilter<ServiceDictItemPO> filter) {
        LambdaJoinQueryWrapper<ServiceDictItemPO> wrapper = filter.asWrapper(this.getDOClass(), new String[0]);
        this.initQueryWrapper(wrapper, filter, false);
        List<ServiceDictItemVO> result = this.baseService.selectListMP(wrapper);
        this.parseRecords(result, false, false);
        return result;
    }
}
