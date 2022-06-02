package com.fhs.agreement.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.agreement.po.AgreementAgreementPO;
import com.fhs.agreement.service.AgreementAgreementService;
import com.fhs.agreement.vo.AgreementAgreementVO;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 采购协议(AgreementAgreement)表控制层
 *
 * @author caiyu
 * @since 2022-06-01 10:09:44
 */

@RestController
@Api(tags={"采购协议"})
@RequestMapping("/ms/agreementAgreement")
public class AgreementAgreementController extends ModelSuperController<AgreementAgreementVO, AgreementAgreementPO> {

    @Autowired
    private AgreementAgreementService agreementAgreementService;

    /**
     * 获取单条数据协议信息+商品信息
     *
     * @param request
     * @return
     */
    @SaCheckRole("agreemenetAgreemenet:see")
    @GetMapping("getAgreementGoosInfo")
    @LogMethod
    public AgreementAgreementVO getAgreementGoosInfo(@RequestParam("id") int id, HttpServletRequest request) {
        return agreementAgreementService.getAgreementGoosInfo(id);
    }


    /**
     * 审核
     *
     * @param request
     * @return
     */
    @PostMapping("agreementAudit")
    @LogMethod
    public HttpResult<Boolean> agreementAudit(@RequestParam("id") int id, HttpServletRequest request) {
        agreementAgreementService.agreementAudit(id);
        return HttpResult.success(true);
    }



}
