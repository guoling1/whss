package com.jkm.hss.controller.admin;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.DistributeRangeQRCodeRequest;
import com.jkm.hss.helper.response.FirstLevelDealerAddResponse;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerAddRequest;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerUpdateRequest;
import com.jkm.hss.dealer.service.DealerRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.request.AdminUserLoginRequest;
import com.jkm.hss.helper.request.DistributeQRCodeRequest;
import com.jkm.hss.helper.response.DistributeQRCodeResponse;
import com.jkm.hss.helper.response.DistributeRangeQRCodeResponse;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Controller
@RequestMapping(value = "/admin/user")
public class AdminController extends BaseController {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerRateService dealerRateService;

    @Autowired
    private BankCardBinService bankCardBinService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductChannelDetailService productChannelDetailService;

    /**
     * 登录
     *
     * @param adminUserLoginRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody final AdminUserLoginRequest adminUserLoginRequest, final HttpServletResponse response) {
        final String _username = StringUtils.trimToEmpty(adminUserLoginRequest.getUsername());
        final String _password = StringUtils.trimToEmpty(adminUserLoginRequest.getPassword());
        if (!(_username.length() >= 4 && _username.length() <= 16)) {
            return CommonResponse.simpleResponse(-1, "用户名长度4-16位");
        }
        if (!(_password.length() >= 6 && _password.length() <= 32)) {
            return CommonResponse.simpleResponse(-1, "密码长度6-32位");
        }

        final Optional<AdminUser> userOptional = this.adminUserService.getAdminUserByName(_username);
        if (!userOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "用户名不存在");
        }

        if (!userOptional.get().isActive()) {
            return CommonResponse.simpleResponse(-1, "用户被禁用");
        }

        final Optional<AdminUserPassport> tokenOptional = this.adminUserService.login(_username, _password);
        if (!tokenOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "用户名或密码错误");
        }

        CookieUtil.setSessionCookie(response, ApplicationConsts.ADMIN_COOKIE_KEY, tokenOptional.get().getToken(),
                ApplicationConsts.getApplicationConfig().domain(), (int)(DealerConsts.TOKEN_EXPIRE_MILLIS / 1000));
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登录成功");
    }

    /**
     * 退出登录
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public CommonResponse logout(final HttpServletResponse response) {
        this.adminUserService.logout(getAdminUser().getId());
        CookieUtil.deleteCookie(response, ApplicationConsts.ADMIN_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登出成功");
    }

    /**
     * 给一级代理商分配码段
     *
     * @param distributeQRCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeQRCode", method = RequestMethod.POST)
    public CommonResponse distributeQRCode(@RequestBody DistributeQRCodeRequest distributeQRCodeRequest) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeQRCodeRequest.getDealerId());
        if(!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        if (distributeQRCodeRequest.getCount() <= 0) {
            return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
        }
        final long unDistributeCodeCount = this.dealerService.getUnDistributeCodeCount(distributeQRCodeRequest.getDealerId());
        if (unDistributeCodeCount > 0) {
            return CommonResponse.simpleResponse(-1, "当前一级代理存在未分配的二维码，不可以分配");
        }
        final Dealer dealer = dealerOptional.get();
        final Triple<Integer, String, QRCode> resultTriple = this.adminUserService.distributeQRCode(super.getAdminUser().getId(),
                distributeQRCodeRequest.getDealerId(), distributeQRCodeRequest.getCount());
        if (1 != resultTriple.getLeft()) {
            return CommonResponse.simpleResponse(-1, resultTriple.getMiddle());
        }
        final QRCode latestQRCode = resultTriple.getRight();
        final DistributeQRCodeResponse distributeQRCodeResponse = new DistributeQRCodeResponse();
        distributeQRCodeResponse.setDealerId(distributeQRCodeRequest.getDealerId());
        distributeQRCodeResponse.setName(dealer.getProxyName());
        distributeQRCodeResponse.setMobile(dealer.getMobile());
        distributeQRCodeResponse.setDistributeDate(latestQRCode.getCreateTime());
        distributeQRCodeResponse.setCount(distributeQRCodeRequest.getCount());
        distributeQRCodeResponse.setStartCode(String.valueOf(Long.valueOf(latestQRCode.getCode()) - distributeQRCodeRequest.getCount() + 1));
        distributeQRCodeResponse.setEndCode(latestQRCode.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distributeQRCodeResponse);
    }

    /**
     * 按码段范围给一级代理商分配码段
     *
     * @param distributeRangeQRCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeRangeQRCode", method = RequestMethod.POST)
    public CommonResponse distributeRangeQRCode(@RequestBody DistributeRangeQRCodeRequest distributeRangeQRCodeRequest) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeRangeQRCodeRequest.getDealerId());
        if(!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final long unDistributeCodeCount = this.dealerService.getUnDistributeCodeCount(distributeRangeQRCodeRequest.getDealerId());
        if (unDistributeCodeCount > 0) {
            return CommonResponse.simpleResponse(-1, "当前一级代理存在未分配的二维码，不可以分配");
        }
        final boolean checkResult = this.dealerService.checkRangeQRCode(distributeRangeQRCodeRequest.getStartCode(),
                distributeRangeQRCodeRequest.getEndCode());
        if (!checkResult) {
            return CommonResponse.simpleResponse(-1, "请检查二维码是不是连续！！");
        }
        final Dealer dealer = dealerOptional.get();
        this.adminUserService.distributeRangeQRCode(super.getAdminUser().getId(), distributeRangeQRCodeRequest.getDealerId(),
                distributeRangeQRCodeRequest.getStartCode(), distributeRangeQRCodeRequest.getEndCode());
        final DistributeRangeQRCodeResponse distribute = new DistributeRangeQRCodeResponse();
        distribute.setDealerId(dealer.getId());
        distribute.setName(dealer.getProxyName());
        distribute.setMobile(dealer.getMobile());
        distribute.setDistributeDate(new Date());
        distribute.setCount((int) (Long.valueOf(distributeRangeQRCodeRequest.getEndCode()) - Long.valueOf(distributeRangeQRCodeRequest.getStartCode()) + 1));
        distribute.setStartCode(distributeRangeQRCodeRequest.getStartCode());
        distribute.setEndCode(distributeRangeQRCodeRequest.getEndCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distribute);
    }



    /**
     * 添加一级代理商
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addFirstDealer", method = RequestMethod.POST)
    public CommonResponse addFirstDealer(@RequestBody final FirstLevelDealerAddRequest firstLevelDealerAddRequest) {
        if(!ValidateUtils.isMobile(firstLevelDealerAddRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "代理手机号格式错误");
        }
        final String bankCard = firstLevelDealerAddRequest.getBankCard();
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankCard);
        if (!bankCardBinOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "结算卡格式错误");
        }
        if(!ValidateUtils.isMobile(firstLevelDealerAddRequest.getBankReserveMobile())) {
            return CommonResponse.simpleResponse(-1, "银行预留手机号格式错误");
        }
        final long proxyNameCount = this.dealerService.getByProxyName(firstLevelDealerAddRequest.getName());
        if (proxyNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "代理商名字已经存在");
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByMobile(firstLevelDealerAddRequest.getMobile());
        if (dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商手机号已经注册");
        }
        final FirstLevelDealerAddRequest.Product productParam = firstLevelDealerAddRequest.getProduct();
        final long productId = productParam.getProductId();
        final Optional<Product> productOptional = this.productService.selectById(productId);
        if (!productOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "产品不存在");
        }
        final Product product = productOptional.get();
        final List<ProductChannelDetail> productChannelDetails = this.productChannelDetailService.selectByProductId(productId);
        final Map<Integer, ProductChannelDetail> integerProductChannelDetailImmutableMap =
                Maps.uniqueIndex(productChannelDetails, new Function<ProductChannelDetail, Integer>() {
            @Override
            public Integer apply(ProductChannelDetail input) {
                return input.getChannelTypeSign();
            }
        });
        final List<FirstLevelDealerAddRequest.Channel> channelParams = productParam.getChannels();
        for (FirstLevelDealerAddRequest.Channel channelParam : channelParams) {
            final CommonResponse commonResponse = this.checkChannel(channelParam, integerProductChannelDetailImmutableMap, product);
            if (1 != commonResponse.getCode()) {
                return commonResponse;
            }
        }
        final long dealerId = this.dealerService.createFirstDealer(firstLevelDealerAddRequest);
        final FirstLevelDealerAddResponse firstLevelDealerAddResponse = new FirstLevelDealerAddResponse();
        firstLevelDealerAddResponse.setDealerId(dealerId);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", firstLevelDealerAddResponse);
    }

    /**
     * 更新一级代理商
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateDealer", method = RequestMethod.POST)
    public CommonResponse updateDealer(@RequestBody FirstLevelDealerUpdateRequest request) {
        if(!ValidateUtils.isMobile(request.getMobile())) {
            return CommonResponse.simpleResponse(-1, "代理手机号格式错误");
        }
        final String bankCard = request.getBankCard();
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankCard);
        if (!bankCardBinOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "结算卡格式错误");
        }
        if(!ValidateUtils.isMobile(request.getBankReserveMobile())) {
            return CommonResponse.simpleResponse(-1, "银行预留手机号格式错误");
        }
        final long proxyNameCount = this.dealerService.getByProxyNameUnIncludeNow(request.getName(), request.getDealerId());
        if (proxyNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "代理商名字已经存在");
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByMobileUnIncludeNow(request.getMobile(), request.getDealerId());
        if (dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商手机号已经注册");
        }
        final FirstLevelDealerUpdateRequest.Product productParam = request.getProduct();
        final long productId = productParam.getProductId();
        final Optional<Product> productOptional = this.productService.selectById(productId);
        if (!productOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "产品不存在");
        }
        final Product product = productOptional.get();
        final List<ProductChannelDetail> productChannelDetails = this.productChannelDetailService.selectByProductId(productId);
        final Map<Integer, ProductChannelDetail> integerProductChannelDetailImmutableMap =
                Maps.uniqueIndex(productChannelDetails, new Function<ProductChannelDetail, Integer>() {
                    @Override
                    public Integer apply(ProductChannelDetail input) {
                        return input.getChannelTypeSign();
                    }
                });
        final List<FirstLevelDealerUpdateRequest.Channel> channelParams = productParam.getChannels();
        for (FirstLevelDealerUpdateRequest.Channel channelParam : channelParams) {
            final CommonResponse commonResponse = this.checkChannel(channelParam, integerProductChannelDetailImmutableMap, product);
            if (1 != commonResponse.getCode()) {
                return commonResponse;
            }
        }
        this.dealerService.updateDealer(request);
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("dealerId", request.getDealerId()).build();
    }

    private CommonResponse checkChannel(final FirstLevelDealerUpdateRequest.Channel paramChannel,
                                        final Map<Integer, ProductChannelDetail> integerProductChannelDetailImmutableMap,
                                        final Product product) {
        if (paramChannel.getChannelType() == EnumPayChannelSign.YG_WEIXIN.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_WEIXIN.getId());
            final BigDecimal weixinMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weixinMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal weixinMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (weixinMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal weiXinSettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weiXinSettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || weiXinSettleRate.compareTo(weixinMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
            final BigDecimal weixinWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (weixinWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || weixinWithdrawFee.compareTo(weixinMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_ZHIFUBAO.getId());
            final BigDecimal alipayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal alipayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (alipayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal alipaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || alipaySettleRate.compareTo(alipayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal alipayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (alipayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) <= 0
                    || alipayWithdrawFee.compareTo(alipayMerchantWithdrawFee) >= 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }

        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_YINLIAN.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_YINLIAN.getId());
            final BigDecimal quickPayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal quickPayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (quickPayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal quickPaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || quickPaySettleRate.compareTo(quickPayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal quickPayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (quickPayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) <= 0
                    || quickPayWithdrawFee.compareTo(quickPayMerchantWithdrawFee) >= 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        }
        return CommonResponse.simpleResponse(1, "");
    }

    private CommonResponse checkChannel(final FirstLevelDealerAddRequest.Channel paramChannel,
                                        final Map<Integer, ProductChannelDetail> integerProductChannelDetailImmutableMap,
                                        final Product product) {
        if (paramChannel.getChannelType() == EnumPayChannelSign.YG_WEIXIN.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_WEIXIN.getId());
            final BigDecimal weixinMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weixinMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal weixinMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (weixinMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal weiXinSettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weiXinSettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || weiXinSettleRate.compareTo(weixinMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
            final BigDecimal weixinWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (weixinWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || weixinWithdrawFee.compareTo(weixinMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_ZHIFUBAO.getId());
            final BigDecimal alipayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal alipayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (alipayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal alipaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || alipaySettleRate.compareTo(alipayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal alipayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (alipayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || alipayWithdrawFee.compareTo(alipayMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }

        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_YINLIAN.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_YINLIAN.getId());
            final BigDecimal quickPayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal quickPayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (quickPayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal quickPaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || quickPaySettleRate.compareTo(quickPayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal quickPayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (quickPayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || quickPayWithdrawFee.compareTo(quickPayMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        }
        return CommonResponse.simpleResponse(1, "");
    }
}
