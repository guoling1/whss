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
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.merchant.enums.EnumUpgradeRecordType;
import com.jkm.hss.merchant.enums.EnumUpgradeResult;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.RecommendService;
import com.jkm.hss.merchant.service.UpgradeRecordService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
     * @param totalTradeMoney
     */
    @Transactional
    @Override
    public void toUpgradeByRecommend(long merchantId, BigDecimal totalTradeMoney) {
        try {
            MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
            if(merchantInfo!=null){
                if(merchantInfo.getStatus()==EnumMerchantStatus.PASSED.getId()){
                    Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(merchantInfo.getProductId());
                    if(upgradeRecommendRulesOptional.isPresent()){
                        if(totalTradeMoney.compareTo(upgradeRecommendRulesOptional.get().getInviteStandard())>=0){
                            //①更改该商户状态
                            merchantInfoDao.updateStatus(EnumMerchantStatus.FRIEND.getId(),merchantId);
                            //②计算好友是否升级
                            List<Recommend> recommends = recommendService.selectRecommend(merchantId);
                            if(recommends.size()>0){
                                for(int i=0;i<recommends.size();i++){
                                    MerchantInfo mi = merchantInfoDao.selectById(recommends.get(i).getRecommendMerchantId());
//                                    if(mi.){
//
//                                    }
                                }
                            }
                            //判断直接好友或间接好友是否需要升级
                            UpgradeRecord upgradeRecord = new UpgradeRecord();
                            upgradeRecord.setMerchantId(merchantId);
                            upgradeRecord.setStatus(EnumStatus.NORMAL.getId());
                            upgradeRecord.setType(EnumUpgradeRecordType.RECOMMEND.getId());
                            upgradeRecord.setUpgradeResult(EnumUpgradeResult.SUCCESS.getId());
                            upgradeRecordService.insert(upgradeRecord);
                        }else{
                            log.info("该商户刷的额度不够");
                        }
                    }else{
                        log.info("推荐好友，没有此升级配置");
                    }
                }else{
                    log.info("商户状态是{}，此状态下不算升级");
                }
            }else{
                log.info("推荐好友，没有此商户");
            }
        }catch (Exception e){
            log.error("系统出错",e);
        }
    }

}
