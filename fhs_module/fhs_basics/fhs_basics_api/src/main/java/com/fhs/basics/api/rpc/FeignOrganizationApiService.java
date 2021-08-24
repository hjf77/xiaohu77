package com.fhs.basics.api.rpc;

import com.fhs.basics.dox.UcenterMsOrganizationDO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.core.feign.config.FeignConfiguration;
import com.fhs.core.result.HttpResult;
import com.fhs.excel.form.NamesForm;
import com.fhs.excel.service.TransRpcService;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 机构管理rest接口
 * @author user
 * @date 2020-05-18 14:39:22
 */
@FeignClient(value = "basics", configuration= FeignConfiguration.class,primary = false)
public interface FeignOrganizationApiService extends TransRpcService {



    /**
     * 根据id获取组织机构
     * @param id 组织机构id
     * @return 结构对象
     */
    @RequestLine("GET /api/com.fhs.basics.api.rpc.FeignOrganizationApiService/getOrgById?id={id}")
    HttpResult<UcenterMsOrganizationDO> getOrgById(@Param("id") String id);

    /**
     * 根据ids获取组织机构
     * @param ids 组织机构id 逗号分隔
     * @return 结构对象
     */
    @RequestLine("GET /api/com.fhs.basics.api.rpc.FeignOrganizationApiService/getOrgByIds?id={ids}")
    HttpResult<List<UcenterMsOrganizationVO>> getOrgByIds(@Param("ids") String ids);

    /**
     * 根据父亲节点id获取子节点集合
     * @param parentid 组织机构父id
     * @return 结构对象
     */
    @RequestLine("GET /api/com.fhs.basics.api.rpc.FeignOrganizationApiService/getOrgByIds?id={ids}")
    HttpResult<List<UcenterMsOrganizationVO>> getOrgByParentid(@Param("id") String parentid);

    /**
     * 反向翻译
     * @param namesForm 表单，包含names集合
     * @return
     */
    @RequestLine("POST /api/com.fhs.basics.api.rpc.FeignOrganizationApiService/doUnTrans")
    Map<String,Object> doUnTrans(@RequestBody NamesForm namesForm);



}
