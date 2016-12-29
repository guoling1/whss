package com.jkm.hss.controller.merchant;

import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.WxConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhangbin on 2016/11/24.
 */
@Controller
@RequestMapping(value = "/savePic")
public class SavePicController extends BaseController {

    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public String add(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws IOException {

        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            return "/upload";
        }

    }
}
