package com.jkm.hss.controller.code;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.MerchantLoginCodeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Thinkpad on 2017/1/16.
 */
@Slf4j
@Controller
@RequestMapping(value = "/qrCode")
public class QRCodeController extends BaseController {
    /**
     * 店铺绑定二维码
     * @param codeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bindShop", method = RequestMethod.POST)
    public CommonResponse getCode(@RequestBody MerchantLoginCodeRequest codeRequest) {
//        final String mobile = codeRequest.getMobile();
//        if (StringUtils.isBlank(mobile)) {
//            return CommonResponse.simpleResponse(-1, "手机号不能为空");
//        }
//        if (!ValidateUtils.isMobile(mobile)) {
//            return CommonResponse.simpleResponse(-1, "手机号格式错误");
//        }
//        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.REGISTER_MERCHANT);
//        if (1 == verifyCode.getLeft()) {
//            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
//            this.sendMessageService.sendMessage(SendMessageParams.builder()
//                    .mobile(mobile)
//                    .uid("")
//                    .data(params)
//                    .userType(EnumUserType.BACKGROUND_USER)
//                    .noticeType(EnumNoticeType.REGISTER_MERCHANT)
//                    .build()
//            );
//            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
//        }
        return CommonResponse.simpleResponse(-1, "");
    }
}
