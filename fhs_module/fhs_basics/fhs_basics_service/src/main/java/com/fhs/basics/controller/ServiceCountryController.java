package com.fhs.basics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.po.ServiceCountryPO;
import com.fhs.basics.service.ServiceCountryService;
import com.fhs.basics.vo.ServiceCountryVO;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags={""})
@RequestMapping("/serviceCountry")
public class ServiceCountryController extends ModelSuperController<ServiceCountryVO, ServiceCountryPO,Integer> {

    @Autowired
    private ServiceCountryService serviceCountryService;

    /**
     * 查询国家列表
     * @return
     */
    @GetMapping("findCountryList")
    public HttpResult<Map<String, List<ServiceCountryVO>>> findCountryList() {
        List<ServiceCountryVO> countryList = serviceCountryService.selectListMP(new LambdaQueryWrapper<ServiceCountryPO>().orderByAsc(ServiceCountryPO::getFbCountryCode));
        Map<String, List<ServiceCountryVO>> groupCountryList = countryList.stream().collect(Collectors.groupingBy(ServiceCountryPO::getFbCountryCode));
        return HttpResult.success(groupCountryList);
    }
}
