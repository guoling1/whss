package com.jkm.hss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yuxiang on 2016/11/16
 */
@Controller
public class IndexController extends BaseController {

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
