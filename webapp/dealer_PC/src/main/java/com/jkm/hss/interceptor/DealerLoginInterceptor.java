package com.jkm.hss.interceptor;

import com.google.common.base.Optional;
import com.jkm.base.common.databind.DataBind;
import com.jkm.base.common.databind.DataBindManager;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ResponseWriter;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.helper.AdminTokenHelper;
import com.jkm.hss.admin.service.AdminUserPassportService;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
public class DealerLoginInterceptor extends HandlerInterceptorAdapter {

    private static final DataBind<Dealer> DEALER_USER_INFO_DATA_BIND = DataBindManager.getInstance().getDataBind(ApplicationConsts.REQUEST_USER_INFO_DATA_BIND_DEALER);

    private static final DataBind<AdminUser> Admin_USER_INFO_DATA_BIND = DataBindManager.getInstance().getDataBind(ApplicationConsts.ADMIN_USER_INFO_DATA_BIND_DEALER);
    @Setter
    private DealerService dealerService;

    @Setter
    private AdminUserPassportService adminUserPassportService;

    @Setter
    private AdminUserService adminUserService;

    /**
     * 经销商登录拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = CookieUtil.getCookie(request, ApplicationConsts.DEALER_COOKIE_KEY);

        final Triple<Integer, String, AdminUser> checkResult = this.checkToken(token);
        if (0 != checkResult.getLeft()) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-2, checkResult.getMiddle()));
            return false;
        }
        Admin_USER_INFO_DATA_BIND.put(checkResult.getRight());
        Optional<Dealer> dealerOptional = dealerService.getById(checkResult.getRight().getDealerId());
        DEALER_USER_INFO_DATA_BIND.put(dealerOptional.get());
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        int count = this.adminUserService.hasUrl(type,request.getRequestURI(),request.getMethod());
        if(count>0){
            int privilegeCount = this.adminUserService.getPrivilegeByContions(checkResult.getRight().getRoleId(),type,request.getRequestURI(),request.getMethod());
            if(privilegeCount<=0){
                ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-2, "权限不足"));
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    private Triple<Integer, String, AdminUser> checkToken(final String token) {
        if (StringUtils.isEmpty(token)) {
            return Triple.of(-100, "请先登录系统", null);
        }

        final long auid = AdminTokenHelper.decryptAdminUserId(token);
        if (auid == 0) {
            return Triple.of(-100, "请先登录系统", null);
        }

        final Optional<AdminUserPassport> adminUserPassportOptional = this.adminUserPassportService.getByToken(token);
        if (!adminUserPassportOptional.isPresent()) {
            return Triple.of(-200, "该用户已在其地点登录，本次登录已失效！", null);
        }

        final AdminUserPassport adminUserPassport = adminUserPassportOptional.get();
        if (adminUserPassport.isExpire()) {
            return Triple.of(-201, "登录状态已过期，请重新登录！", null);
        }

        final Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserById(auid);
        if (!adminUserOptional.isPresent()) {
            return Triple.of(-203, "用户不存在", null);
        }

        final AdminUser adminUser = adminUserOptional.get();
        if (EnumAdminUserStatus.NORMAL.getCode() != adminUser.getStatus()) {
            return Triple.of(-204, "用户被禁用！", null);
        }

        if (adminUserPassport.getExpireTime() < (System.currentTimeMillis() + 5 * 60 * 1000)) {
            this.adminUserPassportService.refreshToken(adminUserPassport.getId());
        }
        return Triple.of(0, "", adminUser);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Admin_USER_INFO_DATA_BIND.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
