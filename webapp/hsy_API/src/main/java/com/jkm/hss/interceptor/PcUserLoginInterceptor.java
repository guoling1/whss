package com.jkm.hss.interceptor;

import com.google.common.base.Optional;
import com.jkm.base.common.databind.DataBind;
import com.jkm.base.common.databind.DataBindManager;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ResponseWriter;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.PcUserPassport;
import com.jkm.hsy.user.help.PcTokenHelper;
import com.jkm.hsy.user.service.PcUserPassportService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Slf4j
public class PcUserLoginInterceptor extends HandlerInterceptorAdapter {

    private static final DataBind<PcUserPassport> APP_PC_USER_INFO_DATA_BIND = DataBindManager.getInstance().getDataBind(ApplicationConsts.REQUEST_APP_USER_PC_DATA_BIND);


    @Setter
    private PcUserPassportService pcUserPassportService;

    @Setter
    private HsyUserDao hsyUserDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        final String token = CookieUtil.getCookie(request, ApplicationConsts.APP_USER_PC_COOKIE_KEY);

        if (StringUtils.isEmpty(token)) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-100, "请先登录系统"));
            return false;
        }

        final long uid = PcTokenHelper.decryptUid(token);
        if (uid == 0) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-100, "请先登录系统"));
            return false;
        }

        final Optional<PcUserPassport> passportOptional = this.pcUserPassportService.getByToken(token);
        if (!passportOptional.isPresent()) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-100, "token失效"));
            return false;
        }

        final PcUserPassport pcUserPassport = passportOptional.get();
        if (pcUserPassport.isExpire()) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-100, "token过期。请重新登录"));
            return false;
        }
        final List<AppAuUser> appAuUsers = this.hsyUserDao.findAppAuUserByID(uid);
        if (CollectionUtils.isEmpty(appAuUsers)) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-201, "用户不存在"));
            return false;
        }

        final AppAuUser appAuUser = appAuUsers.get(0);
        if(appAuUser.getStatus().equals(AppConstant.USER_STATUS_FORBID)) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(-201, "您已被禁止登陆"));
            return false;
        }
        if (pcUserPassport.getExpireTime() < (System.currentTimeMillis() + 86400000)) {
            this.pcUserPassportService.refreshToken(pcUserPassport.getId());
        }

        APP_PC_USER_INFO_DATA_BIND.put(pcUserPassport);
        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        APP_PC_USER_INFO_DATA_BIND.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
