package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public long updateRecord(RequestMerchantInfo requestMerchantInfo) {
        return this.merchantInfoDao.updateRecord(requestMerchantInfo);
    }

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
        merchantInfoDao.updateBySelective(merchantInfo);
        return merchantInfo.getId();
    }

    @Override
    public long regByCode(MerchantInfo merchantInfo) {
//        Triple<Long, Long, Long> triple =  qrCodeService.getCurrentAndFirstAndSecondByCode(merchantInfo.getCode());
//        merchantInfo.setDealerId(triple.getLeft());
//        merchantInfo.setFirstDealerId(triple.getMiddle());
//        merchantInfo.setSecondDealerId(triple.getRight());
//        merchantInfo.setFirstMerchantId(0);
//        merchantInfo.setSecondMerchantId(0);
//        merchantInfo.setLevel(EnumUpGradeType.COMMON.getId());
//        merchantInfo.setHierarchy(0);
        merchantInfoDao.insertSelective(merchantInfo);
        qrCodeService.markAsActivate(merchantInfo.getCode(),merchantInfo.getId());
        return merchantInfo.getId();
    }

    @Override
    public int updatePic(MerchantInfoAddRequest merchantInfo) {
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

}
