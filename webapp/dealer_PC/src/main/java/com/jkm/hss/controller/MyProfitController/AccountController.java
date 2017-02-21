package com.jkm.hss.controller.MyProfitController;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.service.DealerWithdrawService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.helper.request.DealerWithdrawRequest;
import com.jkm.hss.helper.request.FlowDetailsSelectRequest;
import com.jkm.hss.helper.response.AccountInfoResponse;
import com.jkm.hss.helper.response.DealerInfoResponse;
import com.jkm.hss.helper.response.FlowDetailsSelectResponse;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-02-14.
 */
@Slf4j
@RequestMapping(value = "/daili/account")
@RestController
public class AccountController extends BaseController{

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private DealerWithdrawService dealerWithdrawService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 獲取代理信息，
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dealerInfo", method = RequestMethod.POST)
    public CommonResponse getDealerInfo(){
        final Dealer dealer = this.getDealer().get();

        DealerInfoResponse response = new DealerInfoResponse();
        response.setDealerInfo(dealer.getProxyName());
        response.setDealerLeavel(dealer.getLevel());

        return CommonResponse.objectResponse(1, "success", response);
    }

    /**
     * 獲取帳戶詳情，
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public CommonResponse withdraw(){

        final Dealer dealer = this.getDealer().get();
        final Account account = this.accountService.getById(dealer.getAccountId()).get();
        AccountInfoResponse response = new AccountInfoResponse();
        response.setTotalAmount(account.getTotalAmount());
        response.setDueSettleAmount(account.getDueSettleAmount());
        response.setAvailable(account.getAvailable());
        response.setBankName(dealer.getBankName());
        final String bankNo = DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard());
        response.setBankNo("尾号" + bankNo.substring(bankNo.length() - 4 , bankNo.length()));
        response.setFee(1);
        response.setMobile( dealer.getPlainBankMobile( DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile())));

        return CommonResponse.objectResponse(1, "SUCCESS", response);

    }

    /**
     * 代理商资金账户变更记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/flowDetails", method = RequestMethod.POST)
    public CommonResponse flowDetails(@RequestBody final FlowDetailsSelectRequest request){

        try{
            final Dealer dealer = this.getDealer().get();
            PageModel<AccountFlow> pageModel = this.accountFlowService.selectByParam(request.getPageNo(), request.getPageSize(),
                    dealer.getAccountId(), request.getFlowSn(), request.getType(), request.getBeginDate(), request.getEndDate());

            final List<AccountFlow> records = pageModel.getRecords();

            List<FlowDetailsSelectResponse> list = Lists.transform(records, new Function<AccountFlow, FlowDetailsSelectResponse>() {
                @Override
                public FlowDetailsSelectResponse apply(AccountFlow input) {
                    FlowDetailsSelectResponse response = new FlowDetailsSelectResponse();
                    response.setFlowSn(input.getOrderNo());
                    response.setCreateTime(DateFormatUtil.format(input.getCreateTime(),DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
                    response.setBeforeAmount(input.getBeforeAmount().toString());
                    response.setAfterAmount(input.getAfterAmount().toString());
                    response.setIncomeAmount(input.getIncomeAmount().toString());
                    response.setOutAmount(input.getOutAmount().toString());
                    response.setRemark("");
                    return response;
                }
            });

            PageModel<FlowDetailsSelectResponse> model = new PageModel<>(pageModel.getPageNO(),pageModel.getPageSize());
            model.setCount(pageModel.getCount());
            model.setRecords(list);

            return CommonResponse.objectResponse(1, "success", model);
        }catch (final Throwable throwable){
            log.error("获取账户流水异常：" + throwable.getMessage());
        }

        return CommonResponse.simpleResponse(-1 , "fail");
    }

    /**
     * 提现
     *
     * @param withdrawRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public CommonResponse withdraw(@RequestBody final DealerWithdrawRequest withdrawRequest) {

        log.info(super.getDealerId() + ">>>>>申请提现， 提现金额：" + withdrawRequest.getAmount());
        try{
            final Dealer dealer = this.getDealer().get();

            if (dealer.getBankName() == null || dealer.getBankName().equals("")){
                return CommonResponse.simpleResponse(-1, "资料不全，请联系上级添加开户行身份证号后提现");
            }
            if (dealer.getIdCard() == null || dealer.getIdCard().equals("")){
                return CommonResponse.simpleResponse(-1, "资料不全，请联系上级添加开户行身份证号后提现");
            }

            final String mobile = DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile());

           final Pair<Integer, String> pair = smsAuthService.checkVerifyCode(mobile, withdrawRequest.getCode(), EnumVerificationCodeType.WITHDRAW_DEALER);

            if (1 != pair.getLeft()) {
                return CommonResponse.simpleResponse(-1, pair.getRight());
            }

            if ( (new BigDecimal(withdrawRequest.getAmount()).compareTo( new BigDecimal("1.1")) == -1)){
                return CommonResponse.simpleResponse(-1, "提现金额不得小于500");
            }

            final Optional<Account> accountOptional = this.accountService.getById(dealer.getAccountId());
            if (!accountOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "账户不存在");
            }
            final Account account = accountOptional.get();
            if (account.getAvailable().compareTo(new BigDecimal(withdrawRequest.getAmount())) < 0) {
                return CommonResponse.simpleResponse(-1, "可用余额不足");
            }
            if (EnumPayChannelSign.YG_WEIXIN.getId() != withdrawRequest.getChannel()
                    && EnumPayChannelSign.YG_ZHIFUBAO.getId() != withdrawRequest.getChannel()
                    && EnumPayChannelSign.YG_UNIONPAY.getId() != withdrawRequest.getChannel()) {
                return CommonResponse.simpleResponse(-1, "提现方式错误");
            }
            final Pair<Integer, String> withdraw = this.dealerWithdrawService.withdraw(account.getId(), withdrawRequest.getAmount(),
                    withdrawRequest.getChannel(), "dealerWithdraw");
            if (0 == withdraw.getLeft()) {
                return CommonResponse.simpleResponse(1, "提现受理成功");
            }

            return CommonResponse.simpleResponse(-1, "提现失败");
        }catch (final Throwable throwable){

            log.error("提现异常：" + throwable.getMessage());
        }

        return  CommonResponse.simpleResponse(-1, "提现失败");
    }

    /**
     * 发送提现验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
    public CommonResponse sendVerifyCode() {

        final Dealer dealer = this.getDealer().get();
        final String mobile = DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile());

        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }

        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.WITHDRAW_DEALER);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(mobile)
                    .uid(dealer.getId() + "")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.WITHDRAW_CODE_DEALER)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

}
