package com.jkm.hss.interceptor;

import com.google.common.base.Optional;
import com.jkm.base.common.databind.DataBind;
import com.jkm.base.common.databind.DataBindManager;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.ResponseWriter;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.AppAuUserToken;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.service.AppAuTokenService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/7/31.
 */
@Slf4j
public class AppInterceptor extends HandlerInterceptorAdapter {

    private static final DataBind<MerchantInfo> APP_USER_INFO_DATA_BIND = DataBindManager.getInstance().getDataBind(ApplicationConsts.REQUEST_USER_INFO_DATA_BIND_APP);

    @Setter
    private AppAuTokenService appAuTokenService;

    @Setter
    private MerchantInfoService merchantInfoService;

    /**
     * app accessToken拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("accessToken");
        final Triple<Integer, String, MerchantInfo> checkResult = this.checkToken(token,request);
        if (0 != checkResult.getLeft()) {
            ResponseWriter.writeJsonResponse(response, CommonResponse.simpleResponse(checkResult.getLeft(), checkResult.getMiddle()));
            return false;
        }
        APP_USER_INFO_DATA_BIND.put(checkResult.getRight());
        return super.preHandle(request, response, handler);
    }

    private Triple<Integer, String, MerchantInfo> checkToken(final String token,HttpServletRequest request) {
        if (StringUtils.isEmpty(token)) {
            return Triple.of(-2, "商户未登录", null);
        }
        AppAuUserToken appAuUserToken = appAuTokenService.findLoginInfoByAccessToken(token);
        if (!(appAuUserToken != null && appAuUserToken.getOutTime() != null)) {
            return Triple.of(-2, "商户未登录", null);
        } else {
            if (appAuUserToken.getOutTime().before(new Date())) {
                return Triple.of(-2, "商户未登录", null);
            }
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(appAuUserToken.getUid());
        if(!merchantInfoOptional.isPresent()){
            return Triple.of(-1, "商户不存在", null);
        }
        List<String> list=new ArrayList<String>(){
            {
                add("/appMerchantInfo/save");
                add("/appMerchantInfo/sendVerifyCode");
                add("/appMerchantInfo/savePic");
                add("/appMerchantInfo/getMerchanStatus");
                add("/appMerchantInfo/getAuthenInfo");
            }
        };
        if(!list.contains(request.getRequestURI())){
            if(!(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.FRIEND.getId()||merchantInfoOptional.get().getStatus()==EnumMerchantStatus.PASSED.getId())){
                return Triple.of(-3, merchantInfoOptional.get().getStatus()+"",null);
            }
        }
        return Triple.of(0, "", merchantInfoOptional.get());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        APP_USER_INFO_DATA_BIND.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
