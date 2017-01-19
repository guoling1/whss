package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.service.HSYTradeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.StaticCodePayRequest;
import com.jkm.hss.helper.request.WithdrawRequest;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

/**
 * Created by yulong.zhang on 2017/1/18.
 */
@Slf4j
@Controller
@RequestMapping(value = "/trade")
public class TradeController extends BaseController {
    @Autowired
    private HSYTradeService hsyTradeService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private AccountService accountService;

    /**
     * 静态码支付
     *
     * @param payRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scReceipt", method = RequestMethod.POST)
    public CommonResponse staticCodeReceipt(@RequestBody final StaticCodePayRequest payRequest) throws UnsupportedEncodingException {
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(payRequest.getShopId()).get(0);
        final Pair<Integer, String> resultPair = this.hsyTradeService.receipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), shop.getId(), EnumAppType.HSY.getId());
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8")).addParam("subMerName", shop.getName())
                    .addParam("amount", payRequest.getTotalFee()).build();
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());
    }


    /**
     * 提现
     *
     * @param withdrawRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public CommonResponse withdraw(@RequestBody final WithdrawRequest withdrawRequest) {

        final Optional<Account> accountOptional = this.accountService.getById(withdrawRequest.getAccountId());
        if (!accountOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "账户不存在");
        }
        final Account account = accountOptional.get();
        if (account.getAvailable().compareTo(new BigDecimal(withdrawRequest.getAmount())) < 0) {
            return CommonResponse.simpleResponse(-1, "可用余额不足");
        }
        if (EnumPayChannelSign.YG_WEIXIN.getId() != withdrawRequest.getChannel()
                && EnumPayChannelSign.YG_ZHIFUBAO.getId() != withdrawRequest.getChannel()
                && EnumPayChannelSign.YG_YINLIAN.getId() != withdrawRequest.getChannel()) {
            return CommonResponse.simpleResponse(-1, "提现方式错误");
        }
        final Pair<Integer, String> withdraw = this.hsyTradeService.withdraw(account.getId(), withdrawRequest.getAmount(),
                withdrawRequest.getChannel(), withdrawRequest.getAppId());
        if (0 == withdraw.getLeft()) {
            return CommonResponse.simpleResponse(0, "提现受理成功");
        }

        return CommonResponse.simpleResponse(-1, "提现失败");
    }
}
