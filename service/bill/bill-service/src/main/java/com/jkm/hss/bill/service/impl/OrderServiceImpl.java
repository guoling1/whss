package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.FrozenRecordService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.helper.WithdrawParams;
import com.jkm.hss.bill.helper.requestparam.PaymentSdkQueryPayOrderByOrderNoRequest;
import com.jkm.hss.bill.helper.requestparam.PaymentSdkQueryRefundOrderByOrderNoRequest;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.helper.responseparam.PaymentSdkQueryPayOrderByOrderNoResponse;
import com.jkm.hss.bill.helper.responseparam.PaymentSdkQueryRefundOrderByOrderNoResponse;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.GeTuiResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private PayService payService;
    @Autowired
    private HttpClientFacade httpClientFacade;

    /**
     * {@inheritDoc}
     *
     * @param order
     */
    @Override
    @Transactional
    public void add(final Order order) {
        this.orderDao.insert(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @param merchantId
     * @param settleType
     */
    @Override
    @Transactional
    public long createPlayMoneyOrderByPayOrder(final long payOrderId, final long merchantId, final String settleType) {
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        // 查询支付单对应的提现单是否存在
        final Optional<Order> playMoneyOrderOptional = this.getByPayOrderId(payOrderId);
        Preconditions.checkState(!playMoneyOrderOptional.isPresent(), "支付单[{}]，对应的打款单已经存在，不可以重复生成",
                payOrderId);
        final Order payOrder = this.getByIdWithLock(payOrderId).get();
        Preconditions.checkState(payOrder.isPaySuccess());
        final Account account = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
        Preconditions.checkState(payOrder.getTradeAmount().subtract(payOrder.getPoundage()).compareTo(account.getAvailable()) <= 0);
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(payOrderId);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(payOrder.getTradeAmount().subtract(payOrder.getPoundage()));
        playMoneyOrder.setRealPayAmount(playMoneyOrder.getTradeAmount());
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(payOrder.getPayChannelSign());
        playMoneyOrder.setPayer(merchant.getAccountId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(payOrder.getAppId());
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSS, merchantId, payOrder.getPayChannelSign());
        playMoneyOrder.setPoundage(merchantWithdrawPoundage);
        playMoneyOrder.setGoodsName(merchant.getMerchantName());
        playMoneyOrder.setGoodsDescribe(merchant.getMerchantName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param shop
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    @Override
    @Transactional
    public long createPlayMoneyOrder(final AppBizShop shop, final BigDecimal amount,
                                     final String appId, final int channel, final String settleType) {
        final Account account = this.accountService.getByIdWithLock(shop.getAccountID()).get();
        Preconditions.checkState(account.getAvailable().compareTo(amount) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(amount);
        playMoneyOrder.setRealPayAmount(amount);
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(channel);
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(appId);
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, shop.getId(), channel);
        Preconditions.checkState(amount.compareTo(merchantWithdrawPoundage) > 0, "提现金额必须大于提现手续费");
        playMoneyOrder.setPoundage(merchantWithdrawPoundage);
        playMoneyOrder.setGoodsName(shop.getName());
        playMoneyOrder.setGoodsDescribe(shop.getName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(DateTimeUtil.generateT1SettleDate(new Date()));
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), amount);
        this.accountService.increaseFrozenAmount(account.getId(), amount);
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(amount);
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("手动提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), amount,
                "手动提现", EnumAccountFlowType.DECREASE);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     * @param dealer
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    @Override
    public long createDealerPlayMoneyOrder(Dealer dealer, BigDecimal amount, String appId, int channel, String settleType, BigDecimal withdrawFee) {
        final Account account = this.accountService.getByIdWithLock(dealer.getAccountId()).get();
        Preconditions.checkState(account.getAvailable().compareTo(amount) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(amount);
        playMoneyOrder.setRealPayAmount(amount);
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(channel);
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(appId);
        //手续费
        playMoneyOrder.setPoundage(withdrawFee);
        playMoneyOrder.setGoodsName(dealer.getProxyName());
        playMoneyOrder.setGoodsDescribe(dealer.getProxyName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), amount);
        this.accountService.increaseFrozenAmount(account.getId(), amount);
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(amount);
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("手动提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), amount,
                "手动提现", EnumAccountFlowType.DECREASE);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantInfo
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    @Override
    public long createMerchantPlayMoneyOrder(MerchantInfo merchantInfo, BigDecimal amount, String appId, int channel, String settleType, BigDecimal withdrawFee) {
        final Account account = this.accountService.getByIdWithLock(merchantInfo.getAccountId()).get();
        Preconditions.checkState(account.getAvailable().compareTo(amount) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(amount);
        playMoneyOrder.setRealPayAmount(amount);
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(channel);
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(appId);
        //手续费
        playMoneyOrder.setPoundage(withdrawFee);
        playMoneyOrder.setGoodsName(merchantInfo.getMerchantName());
        playMoneyOrder.setGoodsDescribe(merchantInfo.getMerchantName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), amount);
        this.accountService.increaseFrozenAmount(account.getId(), amount);
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(amount);
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("手动提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), amount,
                "提现", EnumAccountFlowType.DECREASE);
        return playMoneyOrder.getId();
    }


    /**
     * {@inheritDoc}
     *
     * @param withdrawParams
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long createWithdrawOrder(final WithdrawParams withdrawParams) {
        final Account account = this.accountService.getByIdWithLock(withdrawParams.getAccountId()).get();
        Preconditions.checkState(account.getAvailable().compareTo(withdrawParams.getWithdrawAmount()) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(withdrawParams.getWithdrawAmount());
        playMoneyOrder.setRealPayAmount(withdrawParams.getWithdrawAmount());
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(withdrawParams.getChannel());
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(withdrawParams.getAppId());
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, withdrawParams.getAccountId(), withdrawParams.getChannel());
        Preconditions.checkState(withdrawParams.getWithdrawAmount().compareTo(merchantWithdrawPoundage) > 0, "提现金额必须大于提现手续费");
        playMoneyOrder.setPoundage(merchantWithdrawPoundage);
        playMoneyOrder.setGoodsName(withdrawParams.getNote());
        playMoneyOrder.setGoodsDescribe(withdrawParams.getNote());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(withdrawParams.getSettleType());
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), withdrawParams.getWithdrawAmount());
        this.accountService.increaseFrozenAmount(account.getId(), withdrawParams.getWithdrawAmount());
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(withdrawParams.getWithdrawAmount());
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(playMoneyOrder.getOrderNo());
        accountFlow.setRefundOrderNo("");
        accountFlow.setIncomeAmount(new BigDecimal("0.00"));
        accountFlow.setOutAmount(withdrawParams.getWithdrawAmount());
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setAfterAmount(account.getAvailable().subtract(accountFlow.getOutAmount()));
        accountFlow.setChangeTime(new Date());
        accountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        accountFlow.setRemark("提现出账");
        this.accountFlowService.add(accountFlow);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @return
     */
    @Override
    public int update(final Order order) {
        return this.orderDao.update(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param remark
     * @return
     */
    @Override
    public int updateRemark(final long id, final String remark) {
        return this.orderDao.updateRemark(id, remark);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateStatus(final long id, final int status, final String remark) {
        return this.orderDao.updateStatus(id, status, remark);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateSettleStatus(final long id, final int status) {
        return this.orderDao.updateSettleStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @param successSettleTime
     * @return
     */
    @Override
    @Transactional
    public int markSettleSuccess(final long id, final int status, final Date successSettleTime) {
        return this.orderDao.markSettleSuccess(id, status, successSettleTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param refundAmount
     * @param refundStatus
     * @return
     */
    @Override
    public int updateRefundInfo(final long id, final BigDecimal refundAmount, final EnumOrderRefundStatus refundStatus) {
        return this.orderDao.updateRefundInfo(id, refundAmount.toPlainString(), refundStatus.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getById(final long id) {
        return Optional.fromNullable(this.orderDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.orderDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param tradeType
     * @return
     */
    @Override
    public Optional<Order> getByOrderNoAndTradeType(final String orderNo, int tradeType) {
        return Optional.fromNullable(this.orderDao.selectByOrderNoAndTradeType(orderNo, tradeType));
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @return
     */
    @Override
    public Optional<Order> getByPayOrderId(final long payOrderId) {
        return Optional.fromNullable(this.orderDao.selectByPayOrderId(payOrderId));
    }

    @Override
    public List<MerchantTradeResponse> selectOrderListByPage(OrderTradeRequest req) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderNo",req.getOrderNo());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("merchantName",req.getMerchantName());
        map.put("orderNo",req.getOrderNo());
        map.put("payType",req.getPayType());
        map.put("settleStatus",req.getSettleStatus());
        map.put("status",req.getStatus());
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        map.put("sn",req.getSn());
        map.put("proxyName",req.getProxyName());
        map.put("proxyName1",req.getProxyName1());
        map.put("businessOrderNo",req.getBusinessOrderNo());
        map.put("payChannelSign",req.getPayChannelSign());
        map.put("markCode",req.getMarkCode());
        map.put("appId",req.getAppId());
        map.put("globalId",req.getGlobalId());
        map.put("shortName",req.getShortName());
        map.put("proxyNameHsy",req.getProxyNameHsy());
        map.put("proxyNameHsy1",req.getProxyNameHsy1());
        List<MerchantTradeResponse> list = this.orderDao.selectOrderList(map);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }
            }
        }
        return list;
    }



    public List<MerchantTradeResponse> selectOrderList(OrderTradeRequest req) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderNo",req.getOrderNo());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("merchantName",req.getMerchantName());
        map.put("orderNo",req.getOrderNo());
        map.put("payType",req.getPayType());
        map.put("settleStatus",req.getSettleStatus());
        map.put("status",req.getStatus());
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        map.put("sn",req.getSn());
        map.put("proxyName",req.getProxyName());
        map.put("proxyName1",req.getProxyName1());
        map.put("businessOrderNo",req.getBusinessOrderNo());
        map.put("payChannelSign",req.getPayChannelSign());
        map.put("markCode",req.getMarkCode());
        map.put("appId",req.getAppId());
        map.put("globalId",req.getGlobalId());
        map.put("shortName",req.getShortName());
        map.put("proxyNameHsy",req.getProxyNameHsy());
        map.put("proxyNameHsy1",req.getProxyNameHsy1());
        List<MerchantTradeResponse> list = orderDao.downloadOrderList(map);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }
            }
        }
        return list;
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public static String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hss" + File.separator + "trade" + File.separator + "record";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    /**
     * 下载Excele
     * @param
     * @param baseUrl
     * @return
     */
    @Override
    @Transactional
    public String downloadExcel(OrderTradeRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<PaymentSdkQueryPayOrderByOrderNoResponse> queryPayOrderByOrderNo(final String orderNo) {

        final PaymentSdkQueryPayOrderByOrderNoRequest paymentSdkQueryPayOrderByOrderNoRequest = new PaymentSdkQueryPayOrderByOrderNoRequest();
        paymentSdkQueryPayOrderByOrderNoRequest.setOrderNo(orderNo);
        try {
            final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_QUERY_PAY_ORDER_URL, SdkSerializeUtil.convertObjToMap(paymentSdkQueryPayOrderByOrderNoRequest));
            final List<PaymentSdkQueryPayOrderByOrderNoResponse> responses = JSONArray.parseArray(content, PaymentSdkQueryPayOrderByOrderNoResponse.class);
            return responses;
        } catch (final Throwable e) {
            log.error("查询网关异常");
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrderNo
     * @return
     */
    @Override
    public List<PaymentSdkQueryRefundOrderByOrderNoResponse> queryRefundOrderByOrderNo(final String refundOrderNo) {
        final PaymentSdkQueryRefundOrderByOrderNoRequest paymentSdkQueryRefundOrderByOrderNoRequest = new PaymentSdkQueryRefundOrderByOrderNoRequest();
        paymentSdkQueryRefundOrderByOrderNoRequest.setOrderNo(refundOrderNo);
        try {
            final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_QUERY_REFUND_ORDER_URL, SdkSerializeUtil.convertObjToMap(paymentSdkQueryRefundOrderByOrderNoRequest));
            final List<PaymentSdkQueryRefundOrderByOrderNoResponse> responses = JSONArray.parseArray(content, PaymentSdkQueryRefundOrderByOrderNoResponse.class);
            return responses;
        } catch (final Throwable e) {
            log.error("查询网关异常");
        }
        return Collections.emptyList();
    }


    @Override
    public MerchantTradeResponse selectOrderListByPageAll(OrderTradeRequest req) {
        if("hsy".equals(req.getAppId())){
            MerchantTradeResponse list = orderDao.selectOrderListHsy(req.getOrderNo());
            if (list!=null){

                if (list.getAppId().equals("hss")){
                    String hss="好收收";
                    list.setAppId(hss);
                }
                if (list.getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.setAppId(hsy);
                }

                if (list.getMobile()!=null&&!"".equals(list.getMobile())){
                    list.setMobile(MerchantSupport.decryptMobile(list.getMobile()));
                }
                if (list.getBankNo()!=null&&!"".equals(list.getBankNo())){
                    list.setBankNo(MerchantSupport.decryptBankCard(list.getBankNo()));
                }
                if (list.getReserveMobile()!=null&&!"".equals(list.getReserveMobile())){
                    list.setReserveMobile(MerchantSupport.decryptMobile(list.getReserveMobile()));
                }
                if (list.getIdentity()!=null&&!"".equals(list.getIdentity())){
                    list.setIdentity(MerchantSupport.decryptIdentity(list.getIdentity()));
                }
                if (list.getPayChannelSign()!=0) {
                    list.setPayChannelSigns(EnumPayChannelSign.idOf(list.getPayChannelSign()).getName());
                }

                if (list.getPayType()!=null&&!list.getPayType().equals("")) {
                    if (list.getPayChannelSign()!=0) {
                        list.setPayType(EnumPayChannelSign.idOf(list.getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }
                if (list.getLevel()==1){
                    list.setProxyName(list.getProxyName());
                }
                if (list.getLevel()==2){
                    list.setProxyName1(list.getProxyName());
                    String proxyName = dealerService.selectProxyName(list.getFirstLevelDealerId());
                    list.setProxyName(proxyName);
                }

            }
            return list;

        }
        MerchantTradeResponse list = orderDao.selectOrderListByPageAll(req.getOrderNo());
        if (list!=null){

                if (list.getAppId().equals("hss")){
                    String hss="好收收";
                    list.setAppId(hss);
                }
                if (list.getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.setAppId(hsy);
                }

                if (list.getMobile()!=null&&!"".equals(list.getMobile())){
                    list.setMobile(MerchantSupport.decryptMobile(list.getMobile()));
                }
                if (list.getBankNo()!=null&&!"".equals(list.getBankNo())){
                    list.setBankNo(MerchantSupport.decryptBankCard(list.getBankNo()));
                }
                if (list.getReserveMobile()!=null&&!"".equals(list.getReserveMobile())){
                    list.setReserveMobile(MerchantSupport.decryptMobile(list.getReserveMobile()));
                }
                if (list.getIdentity()!=null&&!"".equals(list.getIdentity())){
                    list.setIdentity(MerchantSupport.decryptIdentity(list.getIdentity()));
                }
                if (list.getPayChannelSign()!=0) {
                    list.setPayChannelSigns(EnumPayChannelSign.idOf(list.getPayChannelSign()).getName());
                }

                if (list.getPayType()!=null&&!list.getPayType().equals("")) {
                    if (list.getPayChannelSign()!=0) {
                        list.setPayType(EnumPayChannelSign.idOf(list.getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }
                if (list.getLevel()==1){
                    list.setProxyName(list.getProxyName());
                }
                if (list.getLevel()==2){
                    list.setProxyName1(list.getProxyName());
                    String proxyName = dealerService.selectProxyName(list.getFirstLevelDealerId());
                    list.setProxyName(proxyName);
                }

            }
        return list;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public Optional<Order> getByOrderNo(final String orderNo) {
        return Optional.fromNullable(this.orderDao.selectByOrderNo(orderNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param requestParam
     * @return
     */
    @Override
    public PageModel<Order> queryMerchantPayOrders(final QueryMerchantPayOrdersRequestParam requestParam) {
        final PageModel<Order> pageModel = new PageModel<>(requestParam.getPageNo(), requestParam.getPageSize());
        requestParam.setOffset(pageModel.getFirstIndex());
        requestParam.setCount(pageModel.getPageSize());
        final long count = this.orderDao.selectCountMerchantPayOrders(requestParam);
        final List<Order> orders = this.orderDao.selectMerchantPayOrders(requestParam);
        pageModel.setCount(count);
        pageModel.setRecords(orders);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param businessOrderNo
     * @return
     */
    @Override
    public Optional<Order> getByBusinessOrderNo(final String businessOrderNo) {
        return Optional.fromNullable(this.orderDao.selectByBusinessOrderNo(businessOrderNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param serviceType
     * @return
     */
    @Override
    public BigDecimal getTotalTradeAmountByAccountId(final long accountId, final String appId, final int serviceType) {
        final BigDecimal totalAmount = this.orderDao.selectTotalTradeAmountByAccountId(accountId, appId, serviceType);
        return null == totalAmount ? new BigDecimal("0.00") : totalAmount;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<String> getCheckedOrderNosByOrderNos(final List<String> orderNos) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return Collections.emptyList();
        }
        return this.orderDao.selectCheckedOrderNosByOrderNos(orderNos);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @return
     */
    @Override
    public long getPageOrdersCountByAccountId(final long accountId, final String appId, final Date date) {
        return this.orderDao.selectPageOrdersCountByAccountId(accountId, appId, date);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Order> getPageOrdersByAccountId(final long accountId, final String appId, final int offset,
                                                final int count, final Date date) {
        return this.orderDao.selectPageOrdersByAccountId(accountId, appId, offset, count, date);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param payChannelSigns
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public long getOrderCountByParam(final long accountId, final String appId, final List<Integer> payChannelSigns, final Date startTime, final Date endTime) {
        return this.orderDao.selectOrderCountByParam(accountId, appId, payChannelSigns, startTime, endTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Order> getOrdersByParam(final long accountId, final String appId, final int offset,
                                                final int count, final List<Integer> payChannelSigns, final Date startTime, final Date endTime) {
        return this.orderDao.selectOrdersByParam(accountId, appId, offset, count, payChannelSigns, startTime, endTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<Order> getByOrderNos(List<String> orderNos) {
        if(CollectionUtils.isEmpty(orderNos)){
            return Collections.EMPTY_LIST;
        }
        return this.orderDao.getByOrderNos(orderNos);
    }

    @Override
    public List<MerchantTradeResponse> getOrderList(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = orderDao.getOrderList(req);
//        List<MerchantTradeResponse> list2 = orderService.getOrderList(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }

                list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }
                }
//                if (list.get(i).getLevel()==1){
//                    list.get(i).setProxyName(list.get(i).getProxyName());
//                }
//                if (list.get(i).getLevel()==2){
//                    list.get(i).setProxyName1(list.get(i).getProxyName());
//                    String proxyName = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
//                    list.get(i).setProxyName(proxyName);
//                }

            }
        }


        return list;
    }

    @Override
    public String  amountCount(OrderTradeRequest req) {
        String res = this.orderDao.amountCount(req);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleT1UnSettlePayOrder(final List<Integer> channelList) {
        final String format = DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd);
        final List<Long> orderIds = this.getT1PaySuccessAndUnSettleOrderIds(DateFormatUtil.parse(format, DateFormatUtil.yyyy_MM_dd), EnumProductType.HSS.getId(), channelList);
        log.info("hss-T1-定时处理提现, 订单[{}]", orderIds);
        if (!CollectionUtils.isEmpty(orderIds)) {
            for (int i = 0; i < orderIds.size(); i++) {
                final long orderId = orderIds.get(i);
                final JSONObject requestParam = new JSONObject();
                requestParam.put("orderId", orderId);
                MqProducer.produce(requestParam, MqConfig.MERCHANT_WITHDRAW_T1, 1000 * i);
            }
        }
    }


    /**
     * {@inheritDoc}
     *
     * @param settleDate
     * @param appId
     * @return
     */
    @Override
    public List<Long> getT1PaySuccessAndUnSettleOrderIds(final Date settleDate, final String appId, final List<Integer> channelList) {
        return this.orderDao.selectT1PaySuccessAndUnSettleOrderIds(settleDate, appId, channelList);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void t1WithdrawByOrderId(final long orderId) {
        log.info("订单[{}], T1发起结算提现", orderId);
        final Order order = this.getByIdWithLock(orderId).get();
        if (order.isPaySuccess() && order.isDueSettle()) {
            final Optional<SettleAccountFlow> decreaseSettleAccountFlowOptional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(),
                    order.getPayee(), EnumAccountFlowType.DECREASE.getId());
            Preconditions.checkState(!decreaseSettleAccountFlowOptional.isPresent(), "订单[{}], 在T1发起结算提现时,出现已结算的记录!!", orderId);
            final SettleAccountFlow increaseSettleAccountFlow = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(),
                    order.getPayee(), EnumAccountFlowType.INCREASE.getId()).get();
            final SettlementRecord settlementRecord = this.settlementRecordService.getByIdWithLock(increaseSettleAccountFlow.getSettlementRecordId()).get();
            if (settlementRecord.isWaitWithdraw()) {
                final MerchantInfo merchant = this.merchantInfoService.getByAccountId(order.getPayee()).get();
                final Pair<Integer, String> withdrawPair = this.withdrawService.merchantWithdrawBySettlementRecord(merchant.getId(),
                        settlementRecord.getId(), order.getSn(), order.getPayChannelSign());
                if (withdrawPair.getLeft() != -1) {
                    log.info("订单[{}], T1发起结算提现, 手续费-入账可用余额", orderId);
                    this.dealerAndMerchantPoundageSettleImpl(order, increaseSettleAccountFlow.getId());
                    return;
                }
                log.error("订单[{}],在T1发起结算提现时， 提现失败异常", orderId, order.getStatus(), order.getSettleStatus());
                return;
            }
        }
        log.error("订单[{}],在T1发起结算提现时，状态错误!!!，订单状态[{}], 结算状态[{}]", orderId, order.getStatus(), order.getSettleStatus());
    }

    @Override
    public List<WithdrawResponse> withdrawList(WithdrawRequest req) {
        if (req.getWithdrawStatus().equals("提现中")){
            req.setStatus(5);
        }
        if (req.getWithdrawStatus().equals("提现成功")){
            req.setStatus(6);
        }
        List<WithdrawResponse> list = this.orderDao.withdrawList(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getMerchantName()!=null&&!list.get(i).getMerchantName().equals("")){
                    list.get(i).setUserType("商户");
                }
                if (list.get(i).getProxyName()!=null&&!list.get(i).getProxyName().equals("")){
                    list.get(i).setUserType("代理商");
                }
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
                if (list.get(i).getSuccessSettleTime()!=null&&!list.get(i).getSuccessSettleTime().equals("")){
                    String dates = sdf.format(list.get(i).getSuccessSettleTime());
                    list.get(i).setSuccessTime(dates);
                }
                if (list.get(i).getStatus()==5){
                    list.get(i).setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
                }
                if (list.get(i).getStatus()==6){
                    list.get(i).setWithdrawStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getValue());
                    String dates = sdf.format(list.get(i).getUpdateTime());
                    list.get(i).setUpdateTimes(dates);
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
            }
        }
        return list;
    }

    /**
     * 提现下载
     * @param req
     * @return
     */
    public List<WithdrawResponse> withdrawList1(WithdrawRequest req) {
        List<WithdrawResponse> list = this.orderDao.withdrawList1(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getMerchantName()!=null&&!list.get(i).getMerchantName().equals("")){
                    list.get(i).setUserType("商户");
                }
                if (list.get(i).getProxyName()!=null&&!list.get(i).getProxyName().equals("")){
                    list.get(i).setUserType("代理商");
                }
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
                if (list.get(i).getSuccessSettleTime()!=null&&!list.get(i).getSuccessSettleTime().equals("")){
                    String dates = sdf.format(list.get(i).getSuccessSettleTime());
                    list.get(i).setSuccessTime(dates);
                }
                if (list.get(i).getStatus()==5){
                    list.get(i).setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
                }
                if (list.get(i).getStatus()==6){
                    list.get(i).setWithdrawStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getValue());
                    String dates = sdf.format(list.get(i).getUpdateTime());
                    list.get(i).setUpdateTimes(dates);
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
            }
        }
        return list;
    }

    @Override
    public int getNo(WithdrawRequest req) {
        return this.orderDao.getNo(req);
    }

    @Override
    public WithdrawResponse withdrawAmount(WithdrawRequest req) {
        WithdrawResponse response = this.orderDao.withdrawAmount(req);
        return response;
    }

    @Override
    public WithdrawResponse withdrawDetail(long idd,String createTimes) {
        WithdrawResponse response = orderDao.withdrawDetail(idd,createTimes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (response!=null){
            response.setLocation(response.getBelongProvinceName()+response.getBelongCityName());
            if (response.getLevel()==2){
                MerchantInfoResponse res = dealerService.getProxyName(response.getFirstLevelDealerId());
                response.setProxyNames(res.getProxyName());
                response.setMarkCode(res.getMarkCode());
            }
            if (response.getCreateTime()!=null&&!response.getCreateTime().equals("")){
                String dates = sdf.format(response.getCreateTime());
                response.setCreateTimes(dates);
            }
            if (response.getSuccessSettleTime()!=null&&!response.getSuccessSettleTime().equals("")){
                String dates = sdf.format(response.getSuccessSettleTime());
                response.setSuccessTime(dates);
            }
            if (response.getStatus()==5){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
            }
            if (response.getPayChannelSign()!=0) {
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
        }
        return response;
    }

    @Override
    public WithdrawResponse withdrawDetails(long idm,String createTimes) {
        WithdrawResponse response = this.orderDao.withdrawDetails(idm,createTimes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (response!=null){
            response.setMobile(MerchantSupport.decryptMobile(response.getMobile()));
            if (response.getProvinceCode()!=null&&!("").equals(response.getProvinceCode())){
                if (response.getProvinceCode().equals("110000")||response.getProvinceCode().equals("120000")||response.getProvinceCode().equals("310000")||response.getProvinceCode().equals("500000")){
                    response.setLocationM(response.getProvinceName()+response.getCountyName());
                }else {
                    response.setLocationM(response.getProvinceName()+response.getCityName()+response.getCountyName());
                }
            }
            if (response.getFirstDealerId()>0 ){
                MerchantInfoResponse res = dealerService.getInfo(response.getFirstDealerId());
                response.setProxyNames(res.getProxyName());
                response.setMarkCode(res.getMarkCode());
            }
            if (response.getSecondDealerId()>0){
                MerchantInfoResponse res1 = dealerService.getInfo1(response.getSecondDealerId());
                response.setProxyName1(res1.getProxyName());
                response.setMarkCode1(res1.getMarkCode());
            }
            if (response.getCreateTime()!=null&&!response.getCreateTime().equals("")){
                String dates = sdf.format(response.getCreateTime());
                response.setCreateTimes(dates);
            }
            if (response.getSuccessSettleTime()!=null&&!response.getSuccessSettleTime().equals("")){
                String dates = sdf.format(response.getSuccessSettleTime());
                response.setSuccessTime(dates);
            }
            if (response.getStatus()==5){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
            }
            if (response.getStatus()==6){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getValue());
            }
            if (response.getPayChannelSign()!=0) {
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
        }
        return response;
    }

    @Override
    public List<PlayResponse> getPlayMoney(String orderNo) {
        List<PlayResponse> list = this.orderDao.getPlayMoney(orderNo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getRequestTime()!=null){
                    String dates = sdf.format(list.get(i).getRequestTime());
                    list.get(i).setRequestTimes(dates);
                }
                if (list.get(i).getFinishTime()!=null){
                    String dates = sdf.format(list.get(i).getFinishTime());
                    list.get(i).setFinishTimes(dates);
                }
                list.get(i).setStatusValue(EnumPlayStatus.of(list.get(i).getStatus()).getValue());

                list.get(i).setPlayMoneyChannels(EnumChannel.of(list.get(i).getPlayMoneyChannel()).getValue());


            }
        }
        return list;
    }

    @Override
    public List<MerchantTradeResponse> getTrade(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = new ArrayList<MerchantTradeResponse>();
        if("hss".equals(req.getAppId())){
            list = this.orderDao.getTrade(req);
        }
        if("hsy".equals(req.getAppId())){
            list = this.orderDao.getHsyTrade(req);
        }
//        List<MerchantTradeResponse> list = this.orderDao.getTrade(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimed(dates);
                }
                if (list.get(i).getPaySuccessTime()!=null){
                    String dates = sdf.format(list.get(i).getPaySuccessTime());
                    list.get(i).setPaySuccessTimes(dates);
                }

                list.get(i).setStatusValue(EnumOrderStatus.of(list.get(i).getStatus()).getValue());

                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }

            }
        }
        return list;
    }

    @Override
    public List<MerchantTradeResponse> getTradeFirst(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = new ArrayList<MerchantTradeResponse>();
        if("hss".equals(req.getAppId())){
            list = this.orderDao.getTradeFirst(req);
        }
        if("hsy".equals(req.getAppId())){
            list = this.orderDao.getHsyTradeFirst(req);
        }
//        List<MerchantTradeResponse> list = this.orderDao.getTradeFirst(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){

                list.get(i).setSettleStat(EnumSettleStatus.of(list.get(i).getSettleStatus()).getValue());

                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimed(dates);
                }
                if (list.get(i).getPaySuccessTime()!=null){
                    String dates = sdf.format(list.get(i).getPaySuccessTime());
                    list.get(i).setPaySuccessTimes(dates);
                }
                list.get(i).setStatusValue(EnumOrderStatus.of(list.get(i).getStatus()).getValue());

                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
//                if (list.get(i).getPayChannelSign()!=0) {
//                    list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
//                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }
            }
        }
        return list;
    }

    @Override
    public int listCount(OrderTradeRequest req) {
        if("hss".equals(req.getAppId())){
            return this.orderDao.listCount(req);
        }
        if("hsy".equals(req.getAppId())){
            return this.orderDao.listHsyCount(req);
        }
        return 0;
    }

    @Override
    public int listFirstCount(OrderTradeRequest req) {
        if("hss".equals(req.getAppId())){
            return this.orderDao.listFirstCount(req);
        }
        if("hsy".equals(req.getAppId())){
            return this.orderDao.listHsyFirstCount(req);
        }
        return 0;
    }

    @Override
    public String downLoad(WithdrawRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = excelSheet(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO excelSheet(WithdrawRequest req,String baseUrl) {
        List<WithdrawResponse> list = withdrawList1(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("withdraw");
        heads.add("提现单号");
        heads.add("账户名称");
        heads.add("用户类型");
        heads.add("业务订单号");
        heads.add("提现金额");
        heads.add("手续费");
        heads.add("提现状态");
        heads.add("渠道名称");
        heads.add("打款流水号");
        heads.add("提现时间");
        heads.add("成功时间");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getOrderNo());
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getUserType());
                columns.add(list.get(i).getBusinessOrderNo());
                columns.add(String.valueOf(list.get(i).getTradeAmount()));
                columns.add(String.valueOf(list.get(i).getPoundage()));
                columns.add(list.get(i).getWithdrawStatus());
                columns.add(list.get(i).getPayChannelName());
                columns.add(list.get(i).getSn());
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }

                if (list.get(i).getUpdateTime()!= null && !"".equals(list.get(i).getUpdateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getUpdateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }

                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }


    /**
     * {@inheritDoc}
     *
     * @param orderNos
     * @return
     */
    @Override
    public Map<String, BigDecimal> getTradeAmountAndFeeByOrderNoList(final List<String> orderNos) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return Collections.emptyMap();
        }
        return this.orderDao.selectTradeAmountAndFeeByOrderNoList(orderNos);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNos
     * @param offset
     * @param pageSize
     * @return
     */
    @Override
    public List<Order> getOrderByOrderNos(final List<String> orderNos, final int offset, final int pageSize) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return Collections.emptyList();
        }
        return this.orderDao.selectByAppParam(orderNos, offset, pageSize);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param payChannelSigns
     * @param sDate
     * @param eDate
     * @return
     */
    @Override
    public AppStatisticsOrder statisticsByParam(final long accountId, final String appId, final ArrayList<Integer> payChannelSigns,
                                                final String sDate, final String eDate) {
        return this.orderDao.statisticsByParam(accountId, appId, payChannelSigns, sDate, eDate);
    }

    @Override
    public String getRefundOrder(String orderNo) {
        return this.orderDao.getRefundOrder(orderNo);
    }

    @Override
    public List<ProfitRefundResponse> getProfitRefundList(String orderNo) {
        List<ProfitRefundResponse> list = this.orderDao.getProfitRefundList(orderNo);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getSplitDate()!= null && !"".equals(list.get(i).getSplitDate())){
                    String st = df.format(list.get(i).getSplitDate());
                    list.get(i).setSplitDates(st);
                }
            }
        }
        return list;
    }

    @Override
    public List<SplitAccountRefundRecord> splitAccountRefundList(String orderNo) {
        List<SplitAccountRefundRecord> list = this.orderDao.splitAccountRefundList(orderNo);
        return list;
    }

    /**
     * {@inheritDoc}
     *
     * @param businessOrderNo
     * @return
     */
    @Override
    public int getCountByBusinessOrder(final String businessOrderNo) {
        return this.orderDao.selectCountByBusinessOrder(businessOrderNo);
    }

    @Override
    public List<QueryOrderResponse> queryOrderList(QueryOrderRequest req) {
        if (req.getPaymentMethod()>0){
            req.setPayChannelSign(EnumPayChannelSign.getIdListByPaymentChannel(req.getPaymentMethod()));
        }else {
            req.setPayChannelSign(null);
        }

        List<QueryOrderResponse> list = this.orderDao.queryOrderList(req);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null && !"".equals(list.get(i).getCreateTime())){
                    String st = df.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(st);
                }
                if (list.get(i).getPaySuccessTime()!=null && !"".equals(list.get(i).getPaySuccessTime())){
                    String st = df.format(list.get(i).getPaySuccessTime());
                    list.get(i).setPaySuccessTimes(st);
                }
                if (list.get(i).getPayChannelSign()>0) {
                    list.get(i).setPaymentMethod(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                }
                if (list.get(i).getStatus()==1) {
                    list.get(i).setStatusValue(EnumBusinessOrderStatus.DUE_PAY.getValue());
                }
                if (list.get(i).getStatus()==2) {
                    list.get(i).setStatusValue(EnumBusinessOrderStatus.PAY_FAIL.getValue());
                }
                if (list.get(i).getStatus()==3) {
                    list.get(i).setStatusValue(EnumBusinessOrderStatus.PAY_SUCCESS.getValue());
                }

            }
        }
        return list;
    }

    @Override
    public int queryOrderListCount(QueryOrderRequest req) {
        return this.orderDao.queryOrderListCount(req);
    }

    @Override
    public String getOrderCount(QueryOrderRequest req) {
        String list = this.orderDao.getOrderCount(req);
        return list;
    }

    @Override
    public String getOrderCount1(QueryOrderRequest req) {
        String list = this.orderDao.getOrderCount1(req);
        return list;
    }

    @Override
    public String getAmountCount(OrderTradeRequest req) {

        if("hss".equals(req.getAppId())){
            String list = this.orderDao.getAmountCount(req);
            return list;
        }
        if("hsy".equals(req.getAppId())){
            String list = this.orderDao.getAmountCount1(req);
            return list;
        }

        return null;
    }

    @Override
    public String getAmountCount1(OrderTradeRequest req) {

        if("hss".equals(req.getAppId())){
            String list = this.orderDao.getPoundageCount(req);
            return list;
        }
        if("hsy".equals(req.getAppId())){
            String list = this.orderDao.getPoundageCount1(req);
            return list;
        }

        return null;
    }

    @Override
    public List<AchievementStatisticsResponse> getAchievement(QueryOrderRequest req) {
        List<AchievementStatisticsResponse> list = this.orderDao.getAchievement(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if ("".equals(list.get(i).getTradeCount())||list.get(i).getTradeCount()==null){
                    list.get(i).setTradeCount("0");
                }
                if ("".equals(list.get(i).getVaildTradeUserCount())||list.get(i).getVaildTradeUserCount()==null){
                    list.get(i).setVaildTradeUserCount("0");
                }
                if ("".equals(list.get(i).getTradeTotalCount())||list.get(i).getTradeTotalCount()==null){
                    list.get(i).setTradeTotalCount("0");
                }
                if ("".equals(list.get(i).getTradeTotalAmount())||list.get(i).getTradeTotalAmount()==null){
                    list.get(i).setTradeTotalAmount("0");
                }
            }
        }
        return list;
    }

    @Override
    public int getAchievementCount(QueryOrderRequest req) {
        return this.orderDao.getAchievementCount(req);
    }

    @Override
    public String downloadAchievement(QueryOrderRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateAchievement(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public List<AchievementStatisticsResponse> getDaiLiAchievement(QueryOrderRequest req) {
        List<AchievementStatisticsResponse> list = this.orderDao.getDaiLiAchievement(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if ("".equals(list.get(i).getTradeCount())||list.get(i).getTradeCount()==null){
                    list.get(i).setTradeCount("0");
                }
                if ("".equals(list.get(i).getVaildTradeUserCount())||list.get(i).getVaildTradeUserCount()==null){
                    list.get(i).setVaildTradeUserCount("0");
                }
                if ("".equals(list.get(i).getTradeTotalCount())||list.get(i).getTradeTotalCount()==null){
                    list.get(i).setTradeTotalCount("0");
                }
                if ("".equals(list.get(i).getTradeTotalAmount())||list.get(i).getTradeTotalAmount()==null){
                    list.get(i).setTradeTotalAmount("0");
                }
            }
        }
        return list;
    }

    @Override
    public int getDaiLiAchievementCount(QueryOrderRequest req) {
        return this.orderDao.getDaiLiAchievementCount(req);
    }

    @Override
    public String downloadDaiLiAchievement(QueryOrderRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateDaiLiAchievement(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateDaiLiAchievement(QueryOrderRequest req, String baseUrl) {
        List<AchievementStatisticsResponse> list = downloadeDaiLi(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("Achievement");
        heads.add("报单员登录名");
        heads.add("报单员姓名");
        heads.add("日期");
        heads.add("当日有效商户数");
        heads.add("当日5元以上交易笔数");
        heads.add("当日名下商户交易总笔数");
        heads.add("当日名下商户交易总额");
        datas.add(heads);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getUsername());
                columns.add(list.get(i).getRealname());
//                if (!"".equals(list.get(i).getCreateTime())&&list.get(i).getCreateTime()!=null){
//                    String checkedTime = sdf.format(date);
//                    columns.add(checkedTime);
//                }else {
//                    columns.add("--");
//                }
                columns.add(req.getCreateTime());
                columns.add(list.get(i).getVaildTradeUserCount());
                columns.add(list.get(i).getTradeCount());
                columns.add(list.get(i).getTradeTotalCount());
                columns.add(list.get(i).getTradeTotalAmount());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    private List<AchievementStatisticsResponse> downloadeDaiLi(QueryOrderRequest req) {
        List<AchievementStatisticsResponse> list = this.orderDao.downloadeDaiLi(req);
        return list;
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateAchievement(QueryOrderRequest req, String baseUrl) {
        List<AchievementStatisticsResponse> list = downloadeYJ(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("Achievement");
        heads.add("报单员登录名");
        heads.add("报单员姓名");
        heads.add("日期");
        heads.add("当日有效商户数");
        heads.add("当日5元以上交易笔数");
        heads.add("当日名下商户交易总笔数");
        heads.add("当日名下商户交易总额");
        datas.add(heads);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getUsername());
                columns.add(list.get(i).getRealname());
                if (!"".equals(list.get(i).getCreateTime())&&list.get(i).getCreateTime()!=null){
                    String checkedTime = sdf.format(date);
                    columns.add(checkedTime);
                }else {
                    columns.add("--");
                }
                columns.add(list.get(i).getVaildTradeUserCount());
                columns.add(list.get(i).getTradeCount());
                columns.add(list.get(i).getTradeTotalCount());
                columns.add(list.get(i).getTradeTotalAmount());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    private List<AchievementStatisticsResponse> downloadeYJ(QueryOrderRequest req) {
        List<AchievementStatisticsResponse> list = this.orderDao.downloadeYJ(req);
        return list;
    }


    @Override
    public String downLoadHsyMerchantTrade(OrderTradeRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = downLoadHsyMerchantTrades(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";

    }

    @Override
    public String downLoadHsyMerchantTrade1(OrderTradeRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = downLoadHsyMerchantTrades1(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO downLoadHsyMerchantTrades(OrderTradeRequest req,String baseUrl) {
        List<MerchantTradeResponse> list = downLoadHsyMerchantTrades(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("trade");
        heads.add("业务方");
        heads.add("交易订单号");
        heads.add("收款商户名称");
        heads.add("所属一级");
        heads.add("所属二级");
        heads.add("业务订单号");
        heads.add("支付金额");
        heads.add("手续费");
        heads.add("交易状态");
        heads.add("支付流水号");
        heads.add("支付方式");
        heads.add("结算状态");
        heads.add("交易时间");
        heads.add("成功时间");
        heads.add("结算周期");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getAppId());
                columns.add(list.get(i).getOrderNo());
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(list.get(i).getBusinessOrderNo());
                columns.add(list.get(i).getTradeAmount());
                columns.add(list.get(i).getPoundage());
                columns.add(list.get(i).getStatusValue());
                columns.add(list.get(i).getSn());
                columns.add(list.get(i).getPayType());
                columns.add(list.get(i).getSettleStat());
                columns.add(list.get(i).getCreateTimed());
                columns.add(list.get(i).getPaySuccessTimes());
                columns.add(list.get(i).getSettleType());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    private ExcelSheetVO downLoadHsyMerchantTrades1(OrderTradeRequest req,String baseUrl) {
        List<MerchantTradeResponse> list = downLoadHsyMerchantTrades1(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("trade");
        heads.add("业务方");
        heads.add("交易订单号");
        heads.add("收款商户名称");
        heads.add("所属一级");
        heads.add("所属二级");
        heads.add("业务订单号");
        heads.add("支付金额");
        heads.add("手续费");
        heads.add("交易状态");
        heads.add("支付流水号");
        heads.add("支付方式");
        heads.add("结算状态");
        heads.add("交易时间");
        heads.add("成功时间");
        heads.add("结算周期");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getAppId());
                columns.add(list.get(i).getOrderNo());
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(list.get(i).getBusinessOrderNo());
                columns.add(list.get(i).getTradeAmount());
                columns.add(list.get(i).getPoundage());
                columns.add(list.get(i).getStatusValue());
                columns.add(list.get(i).getSn());
                columns.add(list.get(i).getPayType());
                columns.add(list.get(i).getSettleStat());
                columns.add(list.get(i).getCreateTimed());
                columns.add(list.get(i).getPaySuccessTimes());
                columns.add(list.get(i).getSettleType());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    /**
     * 一级导出
     * @param req
     * @return
     */
    public List<MerchantTradeResponse> downLoadHsyMerchantTrades1(OrderTradeRequest req) {

        List<MerchantTradeResponse> list = new ArrayList<MerchantTradeResponse>();
        if("hss".equals(req.getAppId())){
            list = this.orderDao.getTradeFirst1(req);
        }
        if("hsy".equals(req.getAppId())){
            list = this.orderDao.getHsyTradeFirst1(req);
        }
//        List<MerchantTradeResponse> list = this.orderDao.getTradeFirst(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){

                list.get(i).setSettleStat(EnumSettleStatus.of(list.get(i).getSettleStatus()).getValue());

                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimed(dates);
                }
                if (list.get(i).getPaySuccessTime()!=null){
                    String dates = sdf.format(list.get(i).getPaySuccessTime());
                    list.get(i).setPaySuccessTimes(dates);
                }
                list.get(i).setStatusValue(EnumOrderStatus.of(list.get(i).getStatus()).getValue());

                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
//                if (list.get(i).getPayChannelSign()!=0) {
//                    list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
//                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }
            }
        }
        return list;
    }

    /**
     * 二级导出
     * @param req
     * @return
     */
    public List<MerchantTradeResponse> downLoadHsyMerchantTrades(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = new ArrayList<MerchantTradeResponse>();
        if("hss".equals(req.getAppId())){
            list = this.orderDao.getTrade1(req);
        }
        if("hsy".equals(req.getAppId())){
            list = this.orderDao.getHsyTrade1(req);
        }
//        List<MerchantTradeResponse> list = this.orderDao.getTrade(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimed(dates);
                }
                if (list.get(i).getPaySuccessTime()!=null){
                    String dates = sdf.format(list.get(i).getPaySuccessTime());
                    list.get(i).setPaySuccessTimes(dates);
                }

                list.get(i).setStatusValue(EnumOrderStatus.of(list.get(i).getStatus()).getValue());

                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        list.get(i).setPayType(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }

                }

            }
        }
        return list;
    }

    @Override
    public void save(GeTuiResponse geTuiResponse) {
        this.orderDao.save(geTuiResponse);
    }


    /**
     * 手续费由待结算入余额
     *
     * @param order
     * @param settleFlowId   收钱商户的待结算流水ID
     */
    private void dealerAndMerchantPoundageSettleImpl(final Order order, final long settleFlowId) {
        final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByOrderNo(order.getOrderNo());
        if (!CollectionUtils.isEmpty(flows)) {
            for (SettleAccountFlow settleAccountFlow : flows) {
                if (settleAccountFlow.getId() == settleFlowId
                        || EnumAccountUserType.COMPANY.getId() == settleAccountFlow.getAccountUserType()) {
                    continue;
                }
                Preconditions.checkState(EnumAccountFlowType.INCREASE.getId() == settleAccountFlow.getType(),
                        "订单[{}],在T1发起结算提现时, 手续费入账到余额，出现已结算的待结算流水", order.getId());
                if (EnumAccountUserType.MERCHANT.getId() == settleAccountFlow.getAccountUserType()) {
                    final MerchantInfo merchant = this.merchantInfoService.getByAccountId(settleAccountFlow.getAccountId()).get();
                    final long settlementRecordId = this.payService.generateMerchantSettlementRecord(settleAccountFlow.getAccountId(),
                            merchant, order.getSettleTime(), settleAccountFlow.getIncomeAmount());
                    this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlow.getId(), settlementRecordId);
                    //待结算--可用余额
                    this.payService.merchantRecordedAccount(settleAccountFlow.getAccountId(), settleAccountFlow.getIncomeAmount(), order, settlementRecordId, settleAccountFlow.getRemark());
                }
                if (EnumAccountUserType.DEALER.getId() == settleAccountFlow.getAccountUserType()) {
                    final Dealer dealer = this.dealerService.getByAccountId(settleAccountFlow.getAccountId()).get();
                    final long settlementRecordId = this.payService.generateDealerSettlementRecord(settleAccountFlow.getAccountId(),
                            dealer, order.getSettleTime(), settleAccountFlow.getIncomeAmount());
                    this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlow.getId(), settlementRecordId);
                    //待结算--可用余额
                    this.payService.dealerRecordedAccount(settleAccountFlow.getAccountId(), settleAccountFlow.getIncomeAmount(), order, settlementRecordId);
                }
            }
        }
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet(OrderTradeRequest req,String baseUrl) {
        List<MerchantTradeResponse> list = selectOrderList(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("trade");
        heads.add("业务方");
        heads.add("业务订单号");
        heads.add("交易订单号");
        heads.add("支付流水号");
        heads.add("交易日期");
        heads.add("收款商户名称");
        heads.add("商户编号");
        heads.add("所属分公司");
        heads.add("所属一级");
        heads.add("所属二级");
        heads.add("支付金额");
        heads.add("手续费率");
//        heads.add("手续费");
        heads.add("订单状态");
        heads.add("结算状态");
        heads.add("支付方式");
        heads.add("支付渠道");
        heads.add("渠道信息");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getAppId());
                columns.add(list.get(i).getBusinessOrderNo());
                columns.add(list.get(i).getOrderNo());
                columns.add(list.get(i).getSn());
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getMarkCode());
                columns.add(list.get(i).getDealerBelong());
                if (!"".equals(list.get(i).getProxyNameHsy())||!"".equals(list.get(i).getProxyNameHsy1())){
                    columns.add(list.get(i).getProxyNameHsy());
                    columns.add(list.get(i).getProxyNameHsy1());
                    log.debug(list.get(i).getProxyNameHsy());
                }
                log.debug(list.get(i).getProxyNameHsy());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(String.valueOf(list.get(i).getTradeAmount()));
                columns.add(String.valueOf(list.get(i).getPayRate()));
                columns.add(EnumOrderStatus.of(list.get(i).getStatus()).getValue());
                columns.add(EnumSettleStatus.of(list.get(i).getSettleStatus()).getValue());

                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")) {
                    if (list.get(i).getPayChannelSign()!=0) {
                        columns.add(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getPaymentChannel().getValue());
                    }
                } else {
                    columns.add("");
                }
                if (list.get(i).getPayChannelSign()!=0) {
                    columns.add(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                columns.add(list.get(i).getRemark());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    @Override
    public int selectOrderListCount(OrderTradeRequest req) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderNo",req.getOrderNo());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("merchantName",req.getMerchantName());
        map.put("orderNo",req.getOrderNo());
        map.put("payType",req.getPayType());
        map.put("settleStatus",req.getSettleStatus());
        map.put("status",req.getStatus());
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        map.put("sn",req.getSn());
        map.put("proxyName",req.getProxyName());
        map.put("proxyName1",req.getProxyName1());
        map.put("businessOrderNo",req.getBusinessOrderNo());
        map.put("payChannelSign",req.getPayChannelSign());
        map.put("markCode",req.getMarkCode());
        map.put("appId",req.getAppId());
        map.put("globalId",req.getGlobalId());
        map.put("shortName",req.getShortName());
        map.put("proxyNameHsy",req.getProxyNameHsy());
        map.put("proxyNameHsy1",req.getProxyNameHsy1());
        return orderDao.selectOrderListCount(map);
    }



}
