package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.GlobalID;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumPayMethod;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.merchant.enums.EnumUpgradeRecordType;
import com.jkm.hss.merchant.helper.request.*;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.entity.UpgradePayRecord;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.enums.EnumUpgradePayResult;
import com.jkm.hss.product.helper.response.PartnerRuleSettingResponse;
import com.jkm.hss.product.servcie.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/11/23.
 */
@Slf4j
@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {

    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UpgradeRecommendRulesService upgradeRecommendRulesService;
    @Autowired
    private UpgradeRecordService upgradeRecordService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private UpgradeRulesService upgradeRulesService;
    @Autowired
    private UpgradePayRecordService upgradePayRecordService;
    @Autowired
    private SendMsgService sendMsgService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;


//    @Override
//    public int update(MerchantInfo merchantInfo) {
//        return 0;
//    }

    @Override
    public int update(final MerchantInfoAddRequest merchantInfo) {

       return this.merchantInfoDao.update(merchantInfo);
    }

    /**
     * {@inheritDoc}
     * @param id
     * @return
     */
    @Override
    public Optional<MerchantInfo> selectById(long id) {
        return Optional.fromNullable(this.merchantInfoDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @return
     */
    @Override
    public Optional<MerchantInfo> getByAccountId(final long accountId) {
        return Optional.fromNullable(this.merchantInfoDao.selectByAccountId(accountId));
    }


    @Override
    public long insertSelective(MerchantInfo merchantInfo) {

        return merchantInfoDao.insertSelective(merchantInfo);
    }

//    private final static List<Integer> BIN_LENGTH_LIST = Lists.newArrayList(
//            9, 6, 10, 8, 7, 5, 4, 3
//    );


//    @Override
//    public Optional<MerchantInfo> getByNo(String bankNo) {
//        Preconditions.checkArgument(!Strings.isNullOrEmpty(bankNo) && bankNo.length() >= 10, "卡号不正确");
//        for (final int binLength : BIN_LENGTH_LIST) {
//            final Optional<MerchantInfo> cardBinOptional = merchantInfoDao.getByNo(bankNo.substring(0, binLength));
//            if (cardBinOptional.isPresent()) {
//                return cardBinOptional;
//            }
//        }
//
//        return Optional.absent();
//    }

    @Override
    public List<MerchantInfo> selectByDealerId(long dealerId) {
        return this.merchantInfoDao.selectByDealerId(dealerId);

    }

    @Override
    public int updateBySelective(MerchantInfo merchantInfo) {
        return this.merchantInfoDao.updateBySelective(merchantInfo);
    }

    @Override
    public List<MerchantInfo> getAll() {
        return this.merchantInfoDao.getAll();
    }

//    @Override
//    public long updateRecord(RequestMerchantInfo requestMerchantInfo) {
//        return this.merchantInfoDao.updateRecord(requestMerchantInfo);
//    }


    @Override
    public long regByWx(MerchantInfo merchantInfo) {
        merchantInfoDao.insertSelective(merchantInfo);
        QRCode qrCode = qrCodeService.initMerchantCode(merchantInfo.getId(),merchantInfo.getProductId(), EnumQRCodeSysType.HSS.getId());
        merchantInfo.setCode(qrCode.getCode());
        merchantInfo.setMarkCode(GlobalID.GetGlobalID(EnumGlobalIDType.MERCHANT, EnumGlobalIDPro.MIN,merchantInfo.getId()+""));
        merchantInfoDao.updateBySelective(merchantInfo);
        return merchantInfo.getId();
    }

    @Override
    public long regByCode(MerchantInfo merchantInfo) {
        merchantInfoDao.insertSelective(merchantInfo);
        merchantInfo.setMarkCode(GlobalID.GetGlobalID(EnumGlobalIDType.MERCHANT, EnumGlobalIDPro.MIN,merchantInfo.getId()+""));
        merchantInfoDao.updateBySelective(merchantInfo);
        qrCodeService.markAsActivate(merchantInfo.getCode(),merchantInfo.getId());
        return merchantInfo.getId();
    }

    @Override
    public int updatePic(MerchantInfoAddRequest merchantInfo) {
        merchantInfo.setAuthenticationTime(new Date());
        return this.merchantInfoDao.updatePic(merchantInfo);
    }

    @Override
    public Optional<MerchantInfo> selectByCode(String code) {
        return Optional.fromNullable(merchantInfoDao.selectByCode(code));
    }

    @Override
    public int updateStauts(int status, long id) {
        return merchantInfoDao.updateStatus(status,id);
    }

    @Override
    public List<MerchantInfo> batchGetMerchantInfo(List<Long> merchantIdList) {
        return this.merchantInfoDao.batchGetMerchantInfo(merchantIdList);
    }

    /**
     * 根据id查询
     *
     * @param mobile
     */
    @Override
    public Optional<MerchantInfo> selectByMobile(String mobile) {
        return Optional.fromNullable(this.merchantInfoDao.selectByMobile(mobile));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param status
     * @param merchantId
     * @return
     */
    @Override
    public int addAccountId(final long accountId, final int status, final long merchantId,Date checkedTime) {
        return this.merchantInfoDao.addAccountId(accountId, status, merchantId,checkedTime);
    }


    /**
     * 推荐好友，大于某个金额去升级
     *
     * @param merchantId
     */
    @Transactional
    @Override
    public void toUpgradeByRecommend(long merchantId) {
        log.info("用户升级开始。。。。。。。。。。。。");
        log.info("merchantId:{}",merchantId);
        try {
            MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
            if(merchantInfo!=null&&merchantInfo.getStatus()!=EnumMerchantStatus.FRIEND.getId()){//已经激活，不再调用
                log.info("没有升级，可以升级");
                log.info("状态是:{}",merchantInfo.getStatus());
                if(merchantInfo.getStatus()==EnumMerchantStatus.PASSED.getId()){
                    log.info("开始升级");
                    Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(merchantInfo.getProductId());
                    if(upgradeRecommendRulesOptional.isPresent()){
                        //①更改该商户状态
                        merchantInfoDao.updateStatus(EnumMerchantStatus.FRIEND.getId(),merchantId);
                        //②该商户的直接好友
                        List<Recommend> recommends = recommendService.selectDirectFriend(merchantId);
                        log.info("产品编码是{}",merchantInfo.getProductId());
                        //③每个级别需推荐人数
                        List<UpgradeRules> upgradeRules = upgradeRulesService.selectAll(merchantInfo.getProductId());
                        if(recommends.size()>0){
                            for(int i=0;i<recommends.size();i++){
                                MerchantInfo mi = merchantInfoDao.selectById(recommends.get(i).getRecommendMerchantId());
                                int friendCount = recommendService.selectFriendCount(recommends.get(i).getRecommendMerchantId());
                                if(mi.getLevel()==0){//普通
                                    log.info("当前级别是普通");
                                    int needNum = upgradeRules.get(0).getPromotionNum();
                                    if(friendCount>=needNum){
                                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(merchantInfo.getProductId(),EnumUpGradeType.CLERK.getId());
                                        if(upgradeRulesOptional.isPresent()){
                                            merchantInfoDao.toUpgrade(recommends.get(i).getRecommendMerchantId(),upgradeRulesOptional.get().getType());
                                            List<PartnerRuleSettingResponse> partnerRuleSettingResponses =  partnerRuleSettingService.selectAll(merchantInfo.getProductId());
                                            if(partnerRuleSettingResponses.size()>0){
                                                for(int j=0;j<partnerRuleSettingResponses.size();j++){
                                                    MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
                                                    merchantChannelRateRequest.setMerchantId(merchantInfo.getId());
                                                    merchantChannelRateRequest.setProductId(merchantInfo.getProductId());
                                                    merchantChannelRateRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                                    Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
                                                    if(!merchantChannelRateOptional.isPresent()){
                                                        log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"费率不存在");
                                                    }else{
                                                        if((merchantChannelRateOptional.get().getMerchantPayRate()).compareTo(partnerRuleSettingResponses.get(j).getClerkRate())>0){
                                                            MerchantUpgradeRequest merchantUpgradeRequest = new MerchantUpgradeRequest();
                                                            merchantUpgradeRequest.setProductId(merchantInfo.getProductId());
                                                            merchantUpgradeRequest.setMerchantId(merchantInfo.getId());
                                                            merchantUpgradeRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                                            merchantUpgradeRequest.setRate(partnerRuleSettingResponses.get(j).getClerkRate());
                                                            merchantChannelRateService.toUpgrade(merchantUpgradeRequest);
                                                        }else{
                                                            log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"当前费率是"+merchantChannelRateOptional.get().getMerchantPayRate()+
                                                                    "小于店员费率"+partnerRuleSettingResponses.get(j).getClerkRate()+",不升级费率");
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                                if(mi.getLevel()==1){//店员
                                    log.info("当前级别是店员");
                                    int needNum = upgradeRules.get(1).getPromotionNum();
                                    if(friendCount>=needNum){
                                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(merchantInfo.getProductId(),EnumUpGradeType.SHOPOWNER.getId());
                                        if(upgradeRulesOptional.isPresent()){
                                            merchantInfoDao.toUpgrade(recommends.get(i).getRecommendMerchantId(),upgradeRulesOptional.get().getType());
                                            List<PartnerRuleSettingResponse> partnerRuleSettingResponses =  partnerRuleSettingService.selectAll(merchantInfo.getProductId());
                                            if(partnerRuleSettingResponses.size()>0){
                                                for(int j=0;j<partnerRuleSettingResponses.size();j++){
                                                    MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
                                                    merchantChannelRateRequest.setMerchantId(merchantInfo.getId());
                                                    merchantChannelRateRequest.setProductId(merchantInfo.getProductId());
                                                    merchantChannelRateRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                                    Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
                                                    if(!merchantChannelRateOptional.isPresent()){
                                                        log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"费率不存在");
                                                    }else{
                                                        if((merchantChannelRateOptional.get().getMerchantPayRate()).compareTo(partnerRuleSettingResponses.get(j).getShopownerRate())>0){
                                                            MerchantUpgradeRequest merchantUpgradeRequest = new MerchantUpgradeRequest();
                                                            merchantUpgradeRequest.setProductId(merchantInfo.getProductId());
                                                            merchantUpgradeRequest.setMerchantId(merchantInfo.getId());
                                                            merchantUpgradeRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                                            merchantUpgradeRequest.setRate(partnerRuleSettingResponses.get(j).getShopownerRate());
                                                            merchantChannelRateService.toUpgrade(merchantUpgradeRequest);
                                                        }else{
                                                            log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"当前费率是"+merchantChannelRateOptional.get().getMerchantPayRate()+
                                                                    "小于店长费率"+partnerRuleSettingResponses.get(j).getShopownerRate()+",不升级费率");
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                                if(mi.getLevel()==2){//店长
                                    log.info("当前级别是店长");
                                    int needNum = upgradeRules.get(2).getPromotionNum();
                                    if(friendCount>=needNum){
                                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(merchantInfo.getProductId(),EnumUpGradeType.BOSS.getId());
                                        if(upgradeRulesOptional.isPresent()){
                                            merchantInfoDao.toUpgrade(recommends.get(i).getRecommendMerchantId(),upgradeRulesOptional.get().getType());
                                            List<PartnerRuleSettingResponse> partnerRuleSettingResponses =  partnerRuleSettingService.selectAll(merchantInfo.getProductId());
                                            if(partnerRuleSettingResponses.size()>0){
                                                for(int j=0;j<partnerRuleSettingResponses.size();j++){
                                                    MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
                                                    merchantChannelRateRequest.setMerchantId(merchantInfo.getId());
                                                    merchantChannelRateRequest.setProductId(merchantInfo.getProductId());
                                                    merchantChannelRateRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                                    Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
                                                    if(!merchantChannelRateOptional.isPresent()){
                                                        log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"费率不存在");
                                                    }else{
                                                        if((merchantChannelRateOptional.get().getMerchantPayRate()).compareTo(partnerRuleSettingResponses.get(j).getBossRate())>0){
                                                            MerchantUpgradeRequest merchantUpgradeRequest = new MerchantUpgradeRequest();
                                                            merchantUpgradeRequest.setProductId(merchantInfo.getProductId());
                                                            merchantUpgradeRequest.setMerchantId(merchantInfo.getId());
                                                            merchantUpgradeRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                                            merchantUpgradeRequest.setRate(partnerRuleSettingResponses.get(j).getBossRate());
                                                            merchantChannelRateService.toUpgrade(merchantUpgradeRequest);
                                                        }else{
                                                            log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"当前费率是"+merchantChannelRateOptional.get().getMerchantPayRate()+
                                                                    "小于店长费率"+partnerRuleSettingResponses.get(j).getBossRate()+",不升级费率");
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        log.info("推荐好友，没有此升级配置");
                    }
                }else{
                    log.info("商户状态是{}，此状态下不能升级",merchantInfo.getStatus());
                }
            }else{
                log.info("推荐好友，没有此商户或者已经激活，不用激活，商户编码{}",merchantId);
            }
        }catch (Exception e){
            log.error("系统出错",e);
        }
    }

    private String getNameByLevel(int level){
        String name = "";
        if(level==0){
            name="普通会员";
        }
        if(level==1){
            name="店员";
        }
        if(level==2){
            name="店长";
        }
        if(level==3){
            name="老板";
        }
        return name;
    }
    /**
     * 升级
     *
     * @param reqSn
     * @param result
     * @return
     */
    @Override
    public void toUpgrade(String reqSn, String result) {
        log.info("充值升级回调开始。。。。。。。。。。。。");
        log.info("reqSn:{}",reqSn);
        log.info("result:{}",result);
        UpgradePayRecord upgradePayRecord = upgradePayRecordService.selectByReqSn(reqSn);
        if(upgradePayRecord!=null){
            if(!EnumUpgradePayResult.SUCCESS.getId().equals(upgradePayRecord.getPayResult())){
                upgradePayRecordService.updatePayResult(result,reqSn);
                List<PartnerRuleSettingResponse> partnerRuleSettingResponses = partnerRuleSettingService.selectAll(upgradePayRecord.getProductId());
                if(partnerRuleSettingResponses.size()>0){
                    for(int j=0;j<partnerRuleSettingResponses.size();j++){
                        MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
                        merchantChannelRateRequest.setMerchantId(upgradePayRecord.getMerchantId());
                        merchantChannelRateRequest.setProductId(upgradePayRecord.getProductId());
                        merchantChannelRateRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                        Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
                        if(!merchantChannelRateOptional.isPresent()){
                            log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"费率不存在");
                        }else{
                            if(upgradePayRecord.getLevel()==EnumUpGradeType.CLERK.getId()){
                                if((merchantChannelRateOptional.get().getMerchantPayRate()).compareTo(partnerRuleSettingResponses.get(j).getClerkRate())>0){
                                    MerchantUpgradeRequest merchantUpgradeRequest = new MerchantUpgradeRequest();
                                    merchantUpgradeRequest.setProductId(upgradePayRecord.getProductId());
                                    merchantUpgradeRequest.setMerchantId(upgradePayRecord.getMerchantId());
                                    merchantUpgradeRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                    merchantUpgradeRequest.setRate(partnerRuleSettingResponses.get(j).getClerkRate());
                                    merchantChannelRateService.toUpgrade(merchantUpgradeRequest);
                                }else{
                                    log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"当前费率是"+merchantChannelRateOptional.get().getMerchantPayRate()+
                                            "小于店长费率"+partnerRuleSettingResponses.get(j).getClerkRate()+",不升级费率");
                                }
                            }else if(upgradePayRecord.getLevel()==EnumUpGradeType.SHOPOWNER.getId()){
                                if((merchantChannelRateOptional.get().getMerchantPayRate()).compareTo(partnerRuleSettingResponses.get(j).getShopownerRate())>0){
                                    MerchantUpgradeRequest merchantUpgradeRequest = new MerchantUpgradeRequest();
                                    merchantUpgradeRequest.setProductId(upgradePayRecord.getProductId());
                                    merchantUpgradeRequest.setMerchantId(upgradePayRecord.getMerchantId());
                                    merchantUpgradeRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                    merchantUpgradeRequest.setRate(partnerRuleSettingResponses.get(j).getShopownerRate());
                                    merchantChannelRateService.toUpgrade(merchantUpgradeRequest);
                                }else{
                                    log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"当前费率是"+merchantChannelRateOptional.get().getMerchantPayRate()+
                                            "小于店长费率"+partnerRuleSettingResponses.get(j).getShopownerRate()+",不升级费率");
                                }
                            }else if(upgradePayRecord.getLevel()==EnumUpGradeType.BOSS.getId()){
                                if((merchantChannelRateOptional.get().getMerchantPayRate()).compareTo(partnerRuleSettingResponses.get(j).getBossRate())>0){
                                    MerchantUpgradeRequest merchantUpgradeRequest = new MerchantUpgradeRequest();
                                    merchantUpgradeRequest.setProductId(upgradePayRecord.getProductId());
                                    merchantUpgradeRequest.setMerchantId(upgradePayRecord.getMerchantId());
                                    merchantUpgradeRequest.setChannelTypeSign(partnerRuleSettingResponses.get(j).getChannelTypeSign());
                                    merchantUpgradeRequest.setRate(partnerRuleSettingResponses.get(j).getBossRate());
                                    merchantChannelRateService.toUpgrade(merchantUpgradeRequest);
                                }else{
                                    log.info("通道"+partnerRuleSettingResponses.get(j).getChannelTypeSign()+"当前费率是"+merchantChannelRateOptional.get().getMerchantPayRate()+
                                            "小于店长费率"+partnerRuleSettingResponses.get(j).getBossRate()+",不升级费率");
                                }
                            }else{
                                log.info("升级级别错误：升级级别为"+upgradePayRecord.getLevel());
                            }
                        }

                    }
                }
                Optional<UserInfo> userInfoOptional = userInfoService.selectByMerchantId(upgradePayRecord.getMerchantId());
                if(userInfoOptional.isPresent()){//存在
                    sendMsgService.sendChargeMessage(upgradePayRecord.getAmount()+"",getNameByLevel(upgradePayRecord.getLevel()),userInfoOptional.get().getOpenId());
                }
            }
        }else{
            log.info("付费升级失败，没有查到该记录{}",reqSn);
        }
    }

    /**
     * 根据请求单号查询分润费
     *
     * @param reqSn
     * @return
     */
    @Override
    public Pair<BigDecimal, BigDecimal> getUpgradeShareProfit(String reqSn) {
        UpgradePayRecord upgradePayRecord = upgradePayRecordService.selectByReqSn(reqSn);
        if(upgradePayRecord == null){
            log.info("升级分润有误");
            return Pair.of(new BigDecimal("0.00"), new BigDecimal("0.00"));
        }else{
            Optional<UpgradeRules> upgradeRulesOptional1 = upgradeRulesService.selectByProductIdAndType(upgradePayRecord.getProductId(),upgradePayRecord.getBeforeLevel());//当前级别对应的升级费
            Optional<UpgradeRules> upgradeRulesOptional2 = upgradeRulesService.selectByProductIdAndType(upgradePayRecord.getProductId(),upgradePayRecord.getLevel());//升级后对应的升级费
            if(!upgradeRulesOptional1.isPresent()){
                log.info("======普通会员===========");
                BigDecimal left = (upgradeRulesOptional2.get().getDirectPromoteShall());
                BigDecimal right = (upgradeRulesOptional2.get().getInDirectPromoteShall());
                log.info("直接差值{}，间接差值{}",left,right);
                return Pair.of(left, right);
            }else{
                log.info("======店员、店长、老板===========");
                BigDecimal left = (upgradeRulesOptional2.get().getDirectPromoteShall()).subtract(upgradeRulesOptional1.get().getDirectPromoteShall());
                BigDecimal right = (upgradeRulesOptional2.get().getInDirectPromoteShall()).subtract(upgradeRulesOptional1.get().getInDirectPromoteShall());
                log.info("直接差值{}，间接差值{}",left,right);
                return Pair.of(left, right);
            }
        }
    }

    /**
     * 初始化推荐版本数据
     *
     * @param merchantInfo
     */
    @Override
    public void updateByCondition(MerchantInfo merchantInfo) {
            merchantInfoDao.updateByCondition(merchantInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountIds
     * @return
     */
    @Override
    public List<MerchantInfo> batchGetByAccountIds(final List<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        return this.merchantInfoDao.batchSelectByAccountIds(accountIds);
    }

    /**
     * 修改信用卡信息
     *
     * @param creditCard
     * @param creditCardName
     * @param creditCardShort
     * @param id
     * @return
     */
    @Override
    public int updateCreditCard(String creditCard, String creditCardName, String creditCardShort, long id) {
        return merchantInfoDao.updateCreditCard(creditCard,creditCardName,creditCardShort,id);
    }

    /**
     * 完善支行信息
     *
     * @param continueBankInfoRequest
     * @return
     */
    @Override
    public int updateBranchInfo(ContinueBankInfoRequest continueBankInfoRequest) {
        MerchantInfo merchantInfo = merchantInfoDao.selectById(continueBankInfoRequest.getId());
        if(merchantInfo.getProvinceCode()!=null&&!"".equals(merchantInfo.getProvinceCode())
                &&merchantInfo.getCityCode()!=null&&!"".equals(merchantInfo.getCityCode())
                &&merchantInfo.getCountyCode()!=null&&!"".equals(merchantInfo.getCountyCode())){
            return 0;
        }
        return merchantInfoDao.updateBranchInfo(continueBankInfoRequest);
    }

    /**
     * 修改认证状态
     *
     * @param isAuthen
     * @param id
     * @return
     */
    @Override
    public int toAuthen(String isAuthen, long id) {
        return merchantInfoDao.toAuthen(isAuthen,id);
    }

    /**
     * 商户切换代理
     * @param code
     * @param changeDealerRequest
     */
    @Override
    public void changeDealer(String code,ChangeDealerRequest changeDealerRequest) {
        merchantInfoDao.updateDealerInfo(changeDealerRequest);
        qrCodeService.updateDealerInfo(code,changeDealerRequest.getFirstDealerId(),changeDealerRequest.getSecondDealerId());
    }

}
