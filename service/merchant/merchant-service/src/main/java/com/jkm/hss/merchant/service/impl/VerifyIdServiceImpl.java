package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.aliyun.service.VerifyID4ElementService;
import com.jkm.hss.merchant.dao.VerifyID4ElementRecordDao;
import com.jkm.hss.merchant.entity.VerifyID4ElementRecord;
import com.jkm.hss.merchant.enums.EnumVerifyID4ElementRecordStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.VerifyIdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Slf4j
@Service
public class VerifyIdServiceImpl implements VerifyIdService {

    @Autowired
    private VerifyID4ElementRecordDao verifyID4ElementRecordDao;

    @Autowired
    private VerifyID4ElementService verifyID4ElementService;

    /**
     * {@inheritDoc}
     *
     * @param verifyID4ElementRecord
     */
    @Override
    @Transactional
    public void add(final VerifyID4ElementRecord verifyID4ElementRecord) {
        this.verifyID4ElementRecordDao.insert(verifyID4ElementRecord);
    }

    /**
     * 更新
     *
     * @param verifyID4ElementRecord
     * @return
     */
    @Override
    public int update(final VerifyID4ElementRecord verifyID4ElementRecord) {
        return this.verifyID4ElementRecordDao.update(verifyID4ElementRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param remark
     * @return
     */
    @Override
    @Transactional
    public int markToIneffective(final long id, final String remark) {
        return this.verifyID4ElementRecordDao.markToIneffective(id, remark);
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile
     * @return
     */
    @Override
    public int markToIneffective(final String mobile) {
        final Optional<VerifyID4ElementRecord> recordByMobile = this.getRecordByMobile(mobile);
        if (recordByMobile.isPresent()) {
            final VerifyID4ElementRecord verifyID4ElementRecord = recordByMobile.get();
            return this.markToIneffective(verifyID4ElementRecord.getId(), "审核不通过");
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile
     * @return
     */
    @Override
    public Optional<VerifyID4ElementRecord> getRecordByMobile(final String mobile) {
        return Optional.fromNullable(this.verifyID4ElementRecordDao.selectRecordByMobile(mobile));
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile   注册手机号
     * @param bankcard 银行卡号
     * @param idCard   身份证
     * @param bankReserveMobile  银行预留手机号
     * @param realName  姓名
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> verifyID(final String mobile, final String bankcard, final String idCard,
                                          final String bankReserveMobile, final String realName) {
        final Optional<VerifyID4ElementRecord> recordOptional = this.getRecordByMobile(mobile);
        long verifyID4ElementRecordId = 0;
        if (!recordOptional.isPresent()) {
            final VerifyID4ElementRecord verifyID4ElementRecord = new VerifyID4ElementRecord();
            verifyID4ElementRecord.setMobile(mobile);
            verifyID4ElementRecord.setBankCard(MerchantSupport.encryptBankCard(bankcard));
            verifyID4ElementRecord.setRealName(realName);
            verifyID4ElementRecord.setIdCard(MerchantSupport.encryptIdenrity(idCard));
            verifyID4ElementRecord.setBankReserveMobile(MerchantSupport.encryptMobile(bankReserveMobile));
            verifyID4ElementRecord.setVerifyCount(1);
            verifyID4ElementRecord.setRemark("");
            verifyID4ElementRecord.setStatus(EnumVerifyID4ElementRecordStatus.EFFECTIVE.getId());
            this.add(verifyID4ElementRecord);
            verifyID4ElementRecordId = verifyID4ElementRecord.getId();
        } else {
            final VerifyID4ElementRecord verifyID4ElementRecord = recordOptional.get();
            if (verifyID4ElementRecord.getVerifyCount() >= VerifyID4ElementRecord.DEFAULT_CAN_RETRY_COUNT) {
                return Pair.of(-1, "校验次数达到上限，不可再进行校验");
            }
            verifyID4ElementRecord.setMobile(mobile);
            verifyID4ElementRecord.setBankCard(MerchantSupport.encryptBankCard(bankcard));
            verifyID4ElementRecord.setRealName(realName);
            verifyID4ElementRecord.setIdCard(MerchantSupport.encryptIdenrity(idCard));
            verifyID4ElementRecord.setBankReserveMobile(MerchantSupport.encryptMobile(bankReserveMobile));
            verifyID4ElementRecord.setVerifyCount(verifyID4ElementRecord.getVerifyCount() + 1);
            verifyID4ElementRecord.setRemark("");
            this.update(verifyID4ElementRecord);
            verifyID4ElementRecordId = verifyID4ElementRecord.getId();
        }
        final Pair<Integer, String> verifyResult = this.verifyID4ElementService.verify(bankcard, idCard, bankReserveMobile, realName);
        this.verifyID4ElementRecordDao.updateRemark(verifyID4ElementRecordId, verifyResult.getRight());
        return verifyResult;
    }
}
