package com.jkm.hss.controller;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerPassport;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import com.jkm.hss.dealer.enums.EnumPassportType;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.service.DealerPassportService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.DealerLoginRequest;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuxiang on 2017-02-15.
 */

@Slf4j
@Controller
@RequestMapping(value = "/daili/login")
public class DealerLoginController extends BaseController{
    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerPassportService dealerPassportService;
    /**
     * 登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pc", method = RequestMethod.POST)
    public CommonResponse ajaxLogin(final HttpServletResponse response, @RequestBody final DealerLoginRequest loginRequest) {
        if (loginRequest.getLoginName()==null||"".equals(loginRequest.getLoginName())){
            return CommonResponse.simpleResponse(-1, "登录名不能为空");
        }
        if (loginRequest.getPwd()==null||"".equals(loginRequest.getPwd())){
            return CommonResponse.simpleResponse(-1, "密码不能为空");
        }
        final Dealer dealer = this.dealerService.getDealerByLoginName(loginRequest.getLoginName());
        if (dealer == null){
            return CommonResponse.simpleResponse(-1, "登录失败,用户不存在");
        }
        if ((DealerSupport.passwordDigest(loginRequest.getPwd(),"JKM")).equals(dealer.getLoginPwd())){
            //密码正确
            final DealerPassport dealerPassport =
                    this.dealerPassportService.createPassport(dealer.getId(), EnumPassportType.MOBILE, EnumLoginStatus.LOGIN);

            CookieUtil.setSessionCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, dealerPassport.getToken(),
                    ApplicationConsts.getApplicationConfig().domain(), (int)(DealerConsts.TOKEN_EXPIRE_MILLIS / 1000));
            this.dealerService.updateLoginDate(dealer.getId());
            return CommonResponse.simpleResponse(1, dealer.getProxyName());
        }

        return CommonResponse.simpleResponse(-1, "登录失败,密码错误");
    }

    /**
     * 登出
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public CommonResponse<BaseEntity> logout(final HttpServletResponse response){
        this.dealerPassportService.markAsLogout(getDealerId());
        CookieUtil.deleteCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登出成功");
    }
}
