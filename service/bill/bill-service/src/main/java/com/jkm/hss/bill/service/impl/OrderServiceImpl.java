package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.FrozenRecordService;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        playMoneyOrder.setSettleTime(DateTimeUtil.getSettleDate());
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
        this.accountFlowService.addAccountFlow(account.getId(), "", amount,
                "手动提现", EnumAccountFlowType.DECREASE);
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
        List<MerchantTradeResponse> list = orderDao.selectOrderList(map);
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

    @Override
    public MerchantTradeResponse selectOrderListByPageAll(OrderTradeRequest req) {
        MerchantTradeResponse list = orderDao.selectOrderListByPageAll(req.getOrderNo());
        if(list != null){
            long payee = list.getPayee();
            MerchantTradeResponse lists = orderDao.getMerchantAll(payee);
            list.setCreateTimes(lists.getCreateTime());
            if (lists.getMobile()!=null&&!"".equals(lists.getMobile())){
                list.setMobile(MerchantSupport.decryptMobile(lists.getMobile()));
            }
            if (lists.getBankNo()!=null&&!"".equals(lists.getBankNo())){
                list.setBankNo(MerchantSupport.decryptBankCard(lists.getBankNo()));
            }
            if (lists.getReserveMobile()!=null&&!"".equals(lists.getReserveMobile())){
                list.setReserveMobile(MerchantSupport.decryptMobile(lists.getReserveMobile()));
            }
            if (lists.getIdentity()!=null&&!"".equals(lists.getIdentity())){
                list.setIdentity(MerchantSupport.decryptIdentity(lists.getIdentity()));
            }
            list.setId(lists.getId());
            list.setName(lists.getName());
            list.setMerchantName(lists.getMerchantName());
            if (lists!=null){
                list.setMerchantName(lists.getMerchantName());
                MerchantTradeResponse result = orderDao.getDealerAll(lists.getDealerId());
                    if (result!=null){
                        if (result.getLevel()==1){
                            list.setProxyName(result.getProxyName());
                        }
                        if (result.getLevel()==1){
                            MerchantTradeResponse res = orderDao.getProxyName1(result.getFirstLevelDealerId());
                            if (res!=null){
                                list.setProxyName1(res.getProxyName());
                            }
                        }
                    }
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
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet(OrderTradeRequest req,String baseUrl) {
        List<MerchantTradeResponse> list = orderDao.selectOrderListTrade(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("trade");
        heads.add("订单号");
        heads.add("交易日期");
        heads.add("商户名称");
        heads.add("所属一级代理");
        heads.add("所属二级代理");
        heads.add("支付金额");
        heads.add("手续费率");
        heads.add("手续费");
        heads.add("订单状态");
        heads.add("结算状态");
        heads.add("支付方式");
        heads.add("支付渠道");
        heads.add("备注信息");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getOrderNo());
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(String.valueOf(list.get(i).getTradeAmount()));
                if (list.get(i).getPayRate()==null){
                    String x = " ";
                    columns.add(x);
                }else {
                    columns.add(String.valueOf(list.get(i).getPayRate()));
                }
                if (list.get(i).getPoundage()==null){
                    String x = " ";
                    columns.add(x);
                }else {
                    columns.add(String.valueOf(list.get(i).getPoundage()));
                }
                if (list.get(i).getStatus()==1){
                    columns.add("待支付");
                }
                if (list.get(i).getStatus()==3){
                    columns.add("支付失败");
                }
                if (list.get(i).getStatus()==4){
                    columns.add("支付成功");
                }
                if (list.get(i).getStatus()==5){
                    columns.add("提现中");
                }
                if (list.get(i).getStatus()==6){
                    columns.add("提现成功");
                }
                if (list.get(i).getStatus()==7){
                    columns.add("充值成功");
                }
                if (list.get(i).getStatus()==8){
                    columns.add("充值失败");
                }
                if (list.get(i).getSettleStatus()==1){
                    columns.add("未结算");
                }
                if (list.get(i).getSettleStatus()==2){
                    columns.add("结算中");
                }
                if (list.get(i).getSettleStatus()==3){
                    columns.add("已结算");
                }

                if ("S".equals(list.get(i).getPayType())){
                    columns.add("微信扫码");
                }
                if ("N".equals(list.get(i).getPayType())){
                    columns.add("微信二维码");

                }
                if ("H".equals(list.get(i).getPayType())){
                    columns.add("微信H5收银台");
                }
                if ("B".equals(list.get(i).getPayType())){
                    columns.add("快捷收款");
                }
                if ("Z".equals(list.get(i).getPayType())){
                    columns.add("支付宝扫码");
                }
                if (list.get(i).getPayChannelSign()==101){
                    columns.add("阳光微信扫码");
                }
                if (list.get(i).getPayChannelSign()==102){
                    columns.add("阳光支付宝扫码");
                }
                if (list.get(i).getPayChannelSign()==103){
                    columns.add("阳光银联支付");
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
        return orderDao.selectOrderListCount(map);
    }



}
