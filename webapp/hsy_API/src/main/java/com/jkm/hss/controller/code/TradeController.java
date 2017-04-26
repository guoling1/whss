package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.HSYTradeService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.StaticCodePayRequest;
import com.jkm.hss.helper.request.WithdrawRequest;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.service.HsyCmbcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private HsyCmbcService hsyCmbcService;
    /**
     * 静态码支付
     *
     * @param payRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scReceipt", method = RequestMethod.POST)
    public CommonResponse staticCodeReceipt(@RequestBody final StaticCodePayRequest payRequest) throws UnsupportedEncodingException {
        EnumPayChannelSign.idOf(payRequest.getPayChannel());
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(payRequest.getMerchantId()).get(0);
        final Pair<Integer, String> resultPair = this.hsyTradeService.receipt(payRequest.getTotalFee(),
                payRequest.getPayChannel(), shop.getId(), EnumAppType.HSY.getId(), payRequest.getMemberId());
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
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
        if (EnumPayChannelSign.YG_WECHAT.getId() != withdrawRequest.getChannel()
                && EnumPayChannelSign.YG_ALIPAY.getId() != withdrawRequest.getChannel()
                && EnumPayChannelSign.YG_UNIONPAY.getId() != withdrawRequest.getChannel()) {
            return CommonResponse.simpleResponse(-1, "提现方式错误");
        }
        final Pair<Integer, String> withdraw = this.hsyTradeService.withdraw(account.getId(), withdrawRequest.getAmount(),
                withdrawRequest.getChannel(), withdrawRequest.getAppId());
        if (0 == withdraw.getLeft()) {
            return CommonResponse.simpleResponse(0, "提现受理成功");
        }

        return CommonResponse.simpleResponse(-1, "提现失败");
    }

    /**
     * url支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "pay")
    public CommonResponse notifyPay(final HttpServletRequest httpServletRequest) {
        final String orderNo = httpServletRequest.getParameter("o_n");
        final String sign = httpServletRequest.getParameter("sign");
        log.info("订单[{}],请求支付", orderNo);
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(orderNo);
        Preconditions.checkState(orderOptional.isPresent(), "订单不存在");
        final Order order = orderOptional.get();
        Preconditions.checkState(order.isCorrectSign(sign), "`签名错误");
        final String payInfo = order.getPayInfo();
        Preconditions.checkState(!StringUtils.isEmpty(payInfo), "支付要素不能为空");
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
        log.info("订单[{}], 通道[{}],支付要素是[{}]", order.getId(), payChannelSign.getCode(), payInfo);
        switch (payChannelSign.getPaymentChannel()) {
            case WECHAT_PAY:
                final String[] wechatPayInfoArray = payInfo.split("\\|");
                Preconditions.checkState(wechatPayInfoArray.length == 6, "缺少支付要素");
                return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                        .addParam("appId", wechatPayInfoArray[0])
                        .addParam("timeStamp", wechatPayInfoArray[1])
                        .addParam("nonceStr", wechatPayInfoArray[2])
                        .addParam("package", wechatPayInfoArray[3])
                        .addParam("signType", wechatPayInfoArray[4])
                        .addParam("paySign", wechatPayInfoArray[5])
                        .addParam("orderId", order.getId())
                        .build();
            case ALIPAY:
                final String[] alipayPayInfoArray = payInfo.split("\\|");
                Preconditions.checkState(alipayPayInfoArray.length == 1, "缺少支付要素");
                return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                        .addParam("tradeNO", alipayPayInfoArray[0])
                        .addParam("orderId", order.getId())
                        .build();
                default:
                    log.error("订单[{}], 通道[{}]，支付渠道错误", order.getId(), payChannelSign.getCode());
                    return CommonResponse.simpleResponse(-1, "支付渠道错误");
        }
    }

    /**
     * 支付成功页
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "success/{id}")
    public String paySuccessPage(final Model model, @PathVariable("id") long id) {
        final Optional<Order> orderOptional = this.orderService.getById(id);
        if(!orderOptional.isPresent()){
            return "/500.jsp";
        }else{
            final Order order = orderOptional.get();
            model.addAttribute("sn", order.getSn());
            model.addAttribute("money", order.getRealPayAmount().toPlainString());
            return "/success.jsp";
        }
    }

    /**
     * url支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "test")
    public void test(final HttpServletRequest httpServletRequest) {
        hsyCmbcService.merchantBindChannel(106,99);
    }
}
