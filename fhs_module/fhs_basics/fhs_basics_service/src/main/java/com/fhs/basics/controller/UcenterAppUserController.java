package com.fhs.basics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.po.ServiceCountryPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.ServiceCountryService;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import com.fhs.trans.service.impl.TransService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * APP用户管理controller
 */
@RestController
@Api(tags = {"APP用户管理"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/sysAppUser")
@LogNamespace(namespace = "sysAppUser", module = "APP用户管理")
public class UcenterAppUserController extends ModelSuperController<UcenterMsUserVO, UcenterMsUserPO, Long> {
    @Autowired
    private TransService transService;
    @Autowired
    private ServiceCountryService serviceCountryService;


    /**
     * 查询bean列表数据
     *
     * @throws Exception
     */
    @Override
    @ResponseBody
    @PostMapping("pagerAdvance")
    @ApiOperation("后台-高级分页查询")
    public IPage<UcenterMsUserVO> findPagerAdvance(@RequestBody QueryFilter<UcenterMsUserPO> filter) {
        if (isPermitted("see")) {
            QueryWrapper wrapper = filter.asWrapper(getDOClass());
            IPage page = baseService.selectPageMP(filter.getPagerInfo(), wrapper);
            //这里的是1是DO的index
            List<UcenterMsUserVO> ucenterMsUserPOS = page.getRecords();
            ucenterMsUserPOS.forEach(u->{
                u.setInternationalCode(serviceCountryService.selectBean(ServiceCountryPO.builder().countryCode(u.getCountryCode()).build()).getInternationalCode());
                u.setCountryName(serviceCountryService.selectBean(ServiceCountryPO.builder().countryCode(u.getCountryCode()).build()).getFullCname());
            });
            return page;
        } else {
            throw new NotPremissionException();
        }
    }
    /**
     * 根据ID集合查询对象数据
     *
     * @param id      id
     * @param request request
     * @return
     * @throws Exception
     */
    @Override
    @LogMethod
    @ResponseBody
    @GetMapping("{id}")
    @ApiOperation("根据id获取单挑数据信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")})
    public UcenterMsUserVO info(@PathVariable("id") Long id, HttpServletRequest request)throws Exception {
        if (isPermitted("see")) {
            UcenterMsUserVO ucenterMsUserVO = baseService.selectById(id);
            ucenterMsUserVO.setCountryName(serviceCountryService.selectBean(ServiceCountryPO.builder().countryCode(ucenterMsUserVO.getCountryCode()).build()).getFullCname());
            ucenterMsUserVO.setInternationalCode(serviceCountryService.selectBean(ServiceCountryPO.builder().countryCode(ucenterMsUserVO.getCountryCode()).build()).getInternationalCode());
            transService.transOne(ucenterMsUserVO);
            return ucenterMsUserVO;
        } else {
            throw new NotPremissionException();
        }
    }
}
