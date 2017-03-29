package com.jkm.hss.controller;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.helper.responseparam.AdminUserLoginResponse;
import com.jkm.hss.admin.service.AdminRoleService;
import com.jkm.hss.admin.service.AdminUserPassportService;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.DealerLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.immutables.value.internal.$processor$.meta.$TreesMirrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    private AdminUserPassportService adminUserPassportService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pc", method = RequestMethod.POST)
    public CommonResponse ajaxLogin(final HttpServletResponse response, @RequestBody final DealerLoginRequest loginRequest) {
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndType(loginRequest.getLoginName(), EnumAdminType.SECONDDEALER.getCode());
        if (adminUserOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "登录名已经存在");
        }
        if (adminUserOptional.get().getPassword().equals(DealerSupport.passwordDigest(loginRequest.getPwd(),"JKM"))){
            final AdminUserPassport adminUserToken = adminUserPassportService.generateToken(adminUserOptional.get().getId());
            CookieUtil.setSessionCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, adminUserToken.getToken(),
                    ApplicationConsts.getApplicationConfig().domain(), (int)(DealerConsts.TOKEN_EXPIRE_MILLIS / 1000));
            this.adminUserService.updateLastLoginDate(adminUserToken.getAuid());
            Optional<Dealer> dealerOptional= dealerService.getById(adminUserOptional.get().getDealerId());
            int level = dealerOptional.get().getLevel();
            int type = EnumAdminType.FIRSTDEALER.getCode();
            if(level==1){
                type=EnumAdminType.FIRSTDEALER.getCode();
            }
            if(level==2){
                type=EnumAdminType.SECONDDEALER.getCode();
            }
            List<AdminUserLoginResponse> loginMenu = this.adminRoleService.getLoginMenu(adminUserOptional.get().getRoleId(),type,adminUserOptional.get().getIsMaster());
            return CommonResponse.simpleResponse(1, adminUserOptional.get().getRealname());
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
    public CommonResponse<BaseEntity> logout(final HttpServletResponse response,final HttpServletRequest request){
        this.adminUserService.logout(getAdminUserId());
        CookieUtil.deleteCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登出成功");
    }
}
