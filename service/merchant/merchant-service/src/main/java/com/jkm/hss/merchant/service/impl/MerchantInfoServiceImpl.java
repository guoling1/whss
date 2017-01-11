package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.GlobalID;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.UpgradeRecord;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.merchant.enums.EnumUpgradeRecordType;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.product.entity.UpgradePayRecord;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.enums.EnumUpgradePayResult;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradePayRecordService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<MerchantInfo> getAll(MerchantInfo merchantInfo) {
        return this.merchantInfoDao.getAll(merchantInfo);
    }

//    @Override
//    public long updateRecord(RequestMerchantInfo requestMerchantInfo) {
//        return this.merchantInfoDao.updateRecord(requestMerchantInfo);
//    }

    @Override
    public long regByWxPub(MerchantInfo merchantInfo) {
        merchantInfoDao.insertSelective(merchantInfo);
        QRCode qrCode = qrCodeService.initMerchantCode(merchantInfo.getId());
        merchantInfo.setCode(qrCode.getCode());
        merchantInfoDao.updateBySelective(merchantInfo);
        return merchantInfo.getId();
    }

    @Override
    public long regByWx(MerchantInfo merchantInfo) {
        merchantInfoDao.insertSelective(merchantInfo);
        QRCode qrCode = qrCodeService.initMerchantCode(merchantInfo.getId(),merchantInfo.getFirstDealerId(),merchantInfo.getSecondMerchantId());
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
        log.info("merchantId:{}",merchantId);
        try {
            MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
            if(merchantInfo!=null&&merchantInfo.getStatus()==EnumMerchantStatus.FRIEND.getId()){//已经激活，不再调用
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
                                int friendCount = recommendService.selectDirectCount(recommends.get(i).getRecommendMerchantId());
                                if(mi.getLevel()==0){//普通
                                    log.info("当前级别是普通");
                                    int needNum = upgradeRules.get(0).getPromotionNum();
                                    if(friendCount>=needNum){
                                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(merchantInfo.getProductId(),EnumUpGradeType.CLERK.getId());
                                        if(upgradeRulesOptional.isPresent()){
                                            merchantInfoDao.toUpgrade(recommends.get(i).getRecommendMerchantId(),upgradeRulesOptional.get().getType(),
                                                    upgradeRulesOptional.get().getWeixinRate(),upgradeRulesOptional.get().getAlipayRate(),upgradeRulesOptional.get().getFastRate());
                                        }
                                    }
                                }
                                if(mi.getLevel()==1){//店员
                                    log.info("当前级别是店员");
                                    int needNum = upgradeRules.get(1).getPromotionNum();
                                    if(friendCount>=needNum){
                                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(merchantInfo.getProductId(),EnumUpGradeType.SHOPOWNER.getId());
                                        if(upgradeRulesOptional.isPresent()){
                                            merchantInfoDao.toUpgrade(recommends.get(i).getRecommendMerchantId(),upgradeRulesOptional.get().getType(),
                                                    upgradeRulesOptional.get().getWeixinRate(),upgradeRulesOptional.get().getAlipayRate(),upgradeRulesOptional.get().getFastRate());
                                        }
                                    }
                                }
                                if(mi.getLevel()==2){//店长
                                    log.info("当前级别是店长");
                                    int needNum = upgradeRules.get(2).getPromotionNum();
                                    if(friendCount>=needNum){
                                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(merchantInfo.getProductId(),EnumUpGradeType.BOSS.getId());
                                        if(upgradeRulesOptional.isPresent()){
                                            merchantInfoDao.toUpgrade(recommends.get(i).getRecommendMerchantId(),upgradeRulesOptional.get().getType(),
                                                    upgradeRulesOptional.get().getWeixinRate(),upgradeRulesOptional.get().getAlipayRate(),upgradeRulesOptional.get().getFastRate());
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
                Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectById(upgradePayRecord.getUpgradeRulesId());
                if(upgradeRulesOptional.isPresent()){
                    merchantInfoDao.toUpgrade(upgradePayRecord.getMerchantId(),upgradeRulesOptional.get().getType(),upgradeRulesOptional.get().getWeixinRate(),upgradeRulesOptional.get().getAlipayRate(),upgradeRulesOptional.get().getFastRate());
                    //判断直接好友或间接好友是否需要升级
                    UpgradeRecord upgradeRecord = new UpgradeRecord();
                    upgradeRecord.setMerchantId(upgradePayRecord.getMerchantId());
                    upgradeRecord.setStatus(EnumStatus.NORMAL.getId());
                    upgradeRecord.setType(EnumUpgradeRecordType.RECHARGE.getId());
                    upgradeRecord.setLevel(upgradeRulesOptional.get().getType());
                    upgradeRecordService.insert(upgradeRecord);
                }else{
                    log.info("没有此合伙人{}",upgradePayRecord.getUpgradeRulesId());
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

}
