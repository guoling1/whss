package com.jkm.hss.controller;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.jkm.base.common.databind.DataBind;
import com.jkm.base.common.databind.DataBindManager;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ResponseWriter;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.WxConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yulong.zhang
 */
@Slf4j
public class BaseController {

    /**
     * 经销商 登录缓存
     */
    private static final DataBind<Dealer> DEALER_USER_INFO_DATA_BIND = DataBindManager.getInstance().getDataBind(ApplicationConsts.REQUEST_USER_INFO_DATA_BIND_DEALER);

    /**
     * 日期处理
     *
     * @param binder
     * @throws Exception
     */
    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    /**
     * controller全局异常处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler
    public void handleException(final HttpServletRequest request, final HttpServletResponse response, final Exception ex) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        try {
            log.error("controller catch exception:", ex);
            request.setAttribute("errorMsg", ex.getMessage());
            request.getRequestDispatcher("/500.jsp").forward(request, response);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        } catch (ServletException e) {
            throw Throwables.propagate(e);
        }
    }


    /**
     * 获取当前经销商
     *
     * @return
     */
    protected Optional<Dealer> getDealer() {
        return Optional.fromNullable(DEALER_USER_INFO_DATA_BIND.get());
    }

    /**
     * 获取当前经销商id
     *
     * @return
     */
    protected long getDealerId() {
        final Optional<Dealer> dealerOptional = getDealer();
        if (dealerOptional.isPresent()) {
            return dealerOptional.get().getId();
        }
        return 0L;
    }



    /**
     * 获取用户id
     * @param request
     * @return
     */
    protected String getOpenId(HttpServletRequest request) {
        String cookie = CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY);
        log.info("获取id cookie是{}",cookie);
        return "ou2YpwZYsLc80lCRXF4vj6FFanvs";
    }

    /**
     * 判断用户是否登录
     * @param request
     * @return
     */
    protected boolean isLogin(HttpServletRequest request) {
        String cookie = CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY);
        log.info("判断cookie是{}",cookie);
        if("".equals(cookie)){
            return false;
        }else{
            return true;
        }
    }

    protected void setSessionAttribute(HttpServletRequest request,String key, Object value) {
        request.getSession().setAttribute(key, value);
    }

    protected Object getSessionAttribute(HttpServletRequest request,String key) {
        return request.getSession().getAttribute(key);
    }


}
