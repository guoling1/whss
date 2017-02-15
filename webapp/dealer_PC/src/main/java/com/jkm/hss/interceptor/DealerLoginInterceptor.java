package com.jkm.hss.interceptor;

import com.google.common.base.Optional;
import com.jkm.base.common.databind.DataBind;
import com.jkm.base.common.databind.DataBindManager;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.RequestUtil;
import com.jkm.base.common.util.ResponseWriter;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerPassport;
import com.jkm.hss.dealer.enums.EnumDealerStatus;
import com.jkm.hss.dealer.enums.EnumPassportType;
import com.jkm.hss.dealer.helper.TokenSupporter;
import com.jkm.hss.dealer.service.DealerPassportService;
import com.jkm.hss.dealer.service.DealerService;
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

    @Setter
    private DealerService dealerService;

    @Setter
    private DealerPassportService dealerPassportService;

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

        final Triple<Integer, String, Dealer> checkResult = this.checkToken(token);
        if (0 != checkResult.getLeft()) {
            response.setContentType("text/html;charset=UTF-8");
            response.sendError(-1, "请先登录");
            return false;
        }
        DEALER_USER_INFO_DATA_BIND.put(checkResult.getRight());
        return super.preHandle(request, response, handler);
    }

    private Triple<Integer, String, Dealer> checkToken(final String token) {
        if (StringUtils.isEmpty(token)) {
            return Triple.of(-100, "请先登录系统", null);
        }

        final long dealerId = TokenSupporter.decryptDealerId(token);
        if (dealerId == 0) {
            return Triple.of(-100, "请先登录系统", null);
        }

        final Optional<DealerPassport> passportOptional = this.dealerPassportService.getByToken(token, EnumPassportType.MOBILE);
        if (!passportOptional.isPresent()) {
            return Triple.of(-200, "该用户已在其地点登录，本次登录已失效！", null);
        }

        final DealerPassport dealerPassport = passportOptional.get();
        if (dealerPassport.isExpire()) {
            return Triple.of(-201, "登录状态已过期，请重新登录！", null);
        }

        if (!dealerPassport.isValid()) {
            return Triple.of(-202, "登录状态无效，请重新登录！", null);
        }

        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return Triple.of(-203, "用户不存在", null);
        }

        final Dealer dealer = dealerOptional.get();
        if (EnumDealerStatus.NORMAL.getId() != dealer.getStatus()) {
            return Triple.of(-204, "用户被禁用！", null);
        }

        if (dealerPassport.getExpireTime() < (System.currentTimeMillis() + 5 * 60 * 1000)) {
            this.dealerPassportService.refreshToken(dealerPassport.getId());
        }
        if (dealerPassport.isLogin()) {
            dealer.setLogin(true);
        } else {
            dealer.setLogin(false);
        }
        return Triple.of(0, "", dealer);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DEALER_USER_INFO_DATA_BIND.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
