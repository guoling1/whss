package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.BindShopRequest;
import com.jkm.hss.helper.request.MerchantLoginCodeRequest;
import com.jkm.hsy.user.service.HsyShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private HsyShopService hsyShopService;

    /**
     * 店铺绑定二维码
     * @param bindShopRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bindShop", method = RequestMethod.POST)
    public CommonResponse bindShop(@RequestBody BindShopRequest bindShopRequest) {
        String code = bindShopRequest.getCode();
        log.info("获取的二维码是{}",code);
        if (StringUtils.isBlank(code)) {
            return CommonResponse.simpleResponse(-1, "二维码不能为空");
        }
        Optional<QRCode> qrCodeOptional =  qrCodeService.getByCode(code, EnumQRCodeSysType.HSY.getId());
        if(!qrCodeOptional.isPresent()){//不存在
            return CommonResponse.simpleResponse(-1, "二维码不存在");
        }
        return CommonResponse.simpleResponse(-1, "");
    }
}
