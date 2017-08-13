package com.jkm.hss.controller.userShop;

import com.google.common.base.Throwables;
import com.jkm.hss.helper.response.CreateApiOrderResponse;
import lombok.extern.slf4j.Slf4j;
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
 * Created by yuxiang on 2017-08-13.
 */
@Slf4j
public class BaseApiController {

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

    public CreateApiOrderResponse afterComplete() {
        return null;
    }

}
