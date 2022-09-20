package com.fhs.basics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.po.ServiceCountryPO;
import com.fhs.basics.service.ServiceCountryService;
import com.fhs.basics.vo.ServiceCountryVO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (ServiceCountry)表控制层
 *
 * @author miyaxin
 * @since 2022-08-17 17:18:34
 */

@RestController
@Api(tags = {""})
@RequestMapping("/country/serviceCountry")
public class ServiceCountryController extends ModelSuperController<ServiceCountryVO, ServiceCountryPO, Integer> {

    @Autowired
    private ServiceCountryService serviceCountryService;
    private Map<String, String> map = new HashMap<>();

    /**
     * 查询国家列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping("findCountryList")
    public HttpResult<Map<String, List<ServiceCountryVO>>> findCountryList(ServiceCountryVO serviceCountryVO) {
        List<ServiceCountryVO> countryList = serviceCountryService.selectListMP(new LambdaQueryWrapper<ServiceCountryPO>().orderByAsc(ServiceCountryPO::getFbCountryCode).like(ServiceCountryPO::getCname, serviceCountryVO.getCname()));
        countryList = countryList.stream().filter(country -> country.getIsGulfCountry() > 0).collect(Collectors.toList());
        Map<String, List<ServiceCountryVO>> groupCountryList = countryList.stream().collect(Collectors.groupingBy(ServiceCountryPO::getFbCountryCode));
        return HttpResult.success(groupCountryList);
    }

    @Override
    @NotRepeat
    @ResponseBody
    @PostMapping
    @ApiOperation(value = "新增")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> add(@RequestBody @Validated(Add.class) ServiceCountryVO serviceCountryVO) {
        if (isPermitted("add")) {
            serviceCountryVO.preInsert(this.getSessionuser().getUserId());
            baseService.insert(serviceCountryVO);
            return HttpResult.success(true);

        }
        throw new NotPremissionException();
    }
}
