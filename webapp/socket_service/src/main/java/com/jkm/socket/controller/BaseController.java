package com.jkm.socket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yulong.zhang
 */
@Slf4j
public class BaseController {

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
     *
     *
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    public String read(final HttpServletRequest httpServletRequest) {
        final StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream(), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (final Throwable e) {
            log.error("读取数据流异常", e);
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (final IOException e) {
                log.error("关闭数据流异常", e);
            }
        }
        return result.toString();
    }
}
