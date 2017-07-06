package com.jkm.hss.controller.channel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.helper.response.PaymentChannelRequest;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.PaymentChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2017/2/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/paymentChannel")
public class PaymentChannelController extends BaseController {

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private BasicChannelService basicChannelService;

    @Autowired
    private AccountService accountService;

    /**
     * 添加通道
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResponse add(@RequestBody PaymentChannelRequest request) {
        try {
            //final Optional<BasicChannel> basicChannelOptional1 = this.basicChannelService.selectByChannelTypeSign(EnumPayChannelSign.of(request.getChannelName()).getId());
           // Preconditions.checkNotNull(!basicChannelOptional1.isPresent(), "该通道已经存在");
            //判断该通道的渠道来源是否同属一个,若一个则使用同一个人资金帐号,若不同,则为该渠道创建新的资金帐号
            final Optional<BasicChannel> basicChannelOptional =
                    this.basicChannelService.selectByChannelSource(request.getChannelSource());

            if (basicChannelOptional.isPresent()){
                //如果已经有了
                request.setAccountId(basicChannelOptional.get().getAccountId());
            }else{
                //没有,则创建
                final long accountId = this.accountService.initAccount("基本通道账户");
                request.setAccountId(accountId);
            }
            request.setChannelTypeSign(EnumPayChannelSign.of(request.getChannelName()).getId());
            if (request.getSupportWay()==1){
                request.setSupportWay(EnumChannelSupportWay.ONLY_CODE.getWay());
            }
            if (request.getSupportWay()==2){
                request.setSupportWay(EnumChannelSupportWay.ONLY_JSAPI.getWay());
            }
            if (request.getSupportWay()==3){
                request.setSupportWay(EnumChannelSupportWay.CODE_JSAPI.getWay());
            }
            request.setBasicBalanceType(EnumBalanceTimeType.of(request.getBasicBalanceType()).getType());
            request.setBasicSettleType(EnumBasicSettleType.of(request.getBasicSettleType()).getId());
            request.setStatus(EnumBasicChannelStatus.USEING.getId());
            request.setBasicTradeRate(request.getBasicTradeRate().divide(new BigDecimal(100)));
            request.setChannelCompany(EnumPayChannelSign.of(request.getChannelName()).getUpperChannel().getValue());
            this.paymentChannelService.addPaymentChannel(request);
            return CommonResponse.simpleResponse(1,"success");
        }catch (final Throwable throwable){
            log.error("添加通道失败,异常信息:" + throwable.getMessage());
        }
        return  CommonResponse.simpleResponse(-1, "fail");
    }
}
