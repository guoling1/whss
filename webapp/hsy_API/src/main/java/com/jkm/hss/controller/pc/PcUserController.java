package com.jkm.hss.controller.pc;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.PcUserLoginRequest;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.PcUserPassport;
import com.jkm.hsy.user.help.PcUserConsts;
import com.jkm.hsy.user.service.PcUserPassportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Slf4j
@Controller
@RequestMapping("/pc/user")
public class PcUserController extends BaseController {

    @Autowired
    private PcUserPassportService pcUserPassportService;
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HsyShopDao hsyShopDao;

    /**
     * 登录
     *
     * @param pcUserLoginRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody final PcUserLoginRequest pcUserLoginRequest, final HttpServletResponse response) {
        final String _username = StringUtils.trimToEmpty(pcUserLoginRequest.getUsername());
        final String _password = StringUtils.trimToEmpty(pcUserLoginRequest.getPassword());
        if (StringUtils.isNumeric(_username)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (StringUtils.isEmpty(_password)) {
            return CommonResponse.simpleResponse(-1, "密码不能为空");
        }
        final AppAuUser appAuUser = new AppAuUser();
        appAuUser.setCellphone(_username);
        final List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (CollectionUtils.isEmpty(list)) {
            return CommonResponse.simpleResponse(-1, "该手机号没有注册");
        }
        final AppAuUser appAuUserFind=list.get(0);
        if(!appAuUserFind.getPassword().equalsIgnoreCase(_password)) {
           return CommonResponse.simpleResponse(-1, "密码错误");
        }
        if(appAuUserFind.getStatus().equals(AppConstant.USER_STATUS_FORBID)) {
            return CommonResponse.simpleResponse(-1, "您已被禁止登陆");
        }
        final PcUserPassport pcUserPassport = pcUserPassportService.generateToken(appAuUser.getId());
        CookieUtil.setSessionCookie(response, ApplicationConsts.APP_USER_PC_COOKIE_KEY, pcUserPassport.getToken(),
                ApplicationConsts.getApplicationConfig().domain(), (int)(PcUserConsts.TOKEN_EXPIRE_MILLIS / 1000));
        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setUid(appAuUserFind.getId());
        List<AppBizShop> shopList= this.hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop);
        appBizShop = shopList.get(0);
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("shopId", appBizShop.getId())
                .addParam("accountId", appAuUser.getAccountID())
                .addParam("uid", appAuUser.getId())
                .build();
    }

    /**
     * 登出
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public CommonResponse logout(final HttpServletResponse response) {
        this.pcUserPassportService.logout(getPcUserPassport().getId());
        CookieUtil.deleteCookie(response, ApplicationConsts.APP_USER_PC_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登出成功");
    }

}
