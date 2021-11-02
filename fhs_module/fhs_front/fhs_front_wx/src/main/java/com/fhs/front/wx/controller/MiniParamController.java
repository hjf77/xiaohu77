package com.fhs.front.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.result.HttpResult;
import com.fhs.front.dox.UcenterFrontUserDO;
import com.fhs.front.service.LoginService;
import com.fhs.front.service.UcenterFrontUserBindService;
import com.fhs.front.service.UcenterFrontUserService;
import com.fhs.front.vo.UcenterFrontUserBindVO;
import com.fhs.front.vo.UcenterFrontUserVO;
import com.fhs.front.wx.vo.LoginResultVo;
import com.fhs.front.wx.vo.MiniParamRegVo;
import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.enums.Check;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 小程序相关业务
 */
@Slf4j
@RestController
@Api(value = "小程序用户接口", tags = "小程序用户接口")
@RequestMapping("/mini_param")
public class MiniParamController {
    /**
     * 登录服务
     */
    @Autowired
    private LoginService loginService;

    @Autowired
    private UcenterFrontUserService frontUserService;

    @Autowired
    private UcenterFrontUserBindService frontUserBindService;

    @Autowired
    private WxMaService wxService;

    /**
     * 需要注册
     */
    private static final int NEED_REG = 601;

    /**
     * 登陆接口
     */
    @PostMapping("/login")
    @ValidateParam(value = Check.NotEmpty, argName = "code")
    @ApiOperation("使用code登录，返回token")
    public HttpResult<LoginResultVo> login(String code) {
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            UcenterFrontUserBindVO bind = frontUserBindService.selectBean(UcenterFrontUserBindVO.builder().authOpenid(session.getOpenid())
                    .authOpenidType(UcenterFrontUserBindService.OPENID_TYPE_WX_MINI_PARAM)
                    .build());
            if (bind == null) {
                HttpResult result = HttpResult.otherCodeMsgResult(NEED_REG, "用户第一次登录请调用注册接口获取token");
                result.setData(LoginResultVo.builder().openid(session.getOpenid()).sessionKey(session.getSessionKey()).build());
                return result;
            }
            String userId = bind.getUserId();
            return HttpResult.success(LoginResultVo.builder().openid(session.getOpenid()).sessionKey(session.getSessionKey()).token(loginService.login(userId)).build());
        } catch (WxErrorException e) {
            this.log.error(e.getMessage(), e);
            return HttpResult.error(null, "登录失败:" + e.getMessage());
        }
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @ApiOperation("注册")
    public HttpResult<LoginResultVo> register(@RequestBody @Valid MiniParamRegVo loginVo) {
        UcenterFrontUserBindVO bind = frontUserBindService.selectBean(UcenterFrontUserBindVO.builder().authOpenid(loginVo.getOpenid())
                .authOpenidType(UcenterFrontUserBindService.OPENID_TYPE_WX_MINI_PARAM)
                .build());
        String userId = null;
        if (bind == null) {
            if (!wxService.getUserService().checkUserInfo(loginVo.getSessionKey(), loginVo.getRawData(), loginVo.getSignature())) {
                HttpResult.error(null, "用户信息校验失败");
            }
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(loginVo.getSessionKey(), loginVo.getEncryptedData(), loginVo.getIv());
            UcenterFrontUserDO user = UcenterFrontUserVO.builder().userId(StringUtils.getUUID())
                    .nickName(userInfo.getNickName()).provinceId(userInfo.getProvince()).cityId(userInfo.getCity()).isEnable(Constant.INT_TRUE)
                    .sex(ConverterUtils.toString(userInfo.getGender())).language(userInfo.getLanguage()).imagePath(userInfo.getAvatarUrl()).build();
            userId = loginService.addBindAndUser(user, loginVo.getOpenid(), UcenterFrontUserBindService.OPENID_TYPE_WX_MINI_PARAM);
        } else {
            userId = bind.getUserId();
        }
        return HttpResult.success(LoginResultVo.builder().openid(loginVo.getOpenid()).sessionKey(loginVo.getSessionKey()).token(loginService.login(userId)).build());
    }


    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public HttpResult<WxMaUserInfo> info(String sessionKey,
                                         String signature, String rawData, String encryptedData, String iv) {
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            HttpResult.error("用户信息校验失败");
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        return HttpResult.success(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public HttpResult<WxMaPhoneNumberInfo> phone(String sessionKey, String encryptedData, String iv) {
        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        return HttpResult.success(phoneNoInfo);
    }

}
