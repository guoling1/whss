package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;

import com.jkm.hss.merchant.dao.MerchantPassportDao;
import com.jkm.hss.merchant.entity.MerchantPassport;
import com.jkm.hss.merchant.enums.EnumLoginStatus;
import com.jkm.hss.merchant.enums.EnumPassportStatus;
import com.jkm.hss.merchant.enums.EnumPassportType;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.service.MerchantPassportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@Service
public class MerchantPassportServiceImpl implements MerchantPassportService {

    @Autowired
    private MerchantPassportDao merchantPassportDao;
    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param passportType
     * @param loginStatus
     * @return
     */
    @Override
    @Transactional
    public MerchantPassport createPassport(final long merchantId, final EnumPassportType passportType, final EnumLoginStatus loginStatus) {
        final MerchantPassport merchantPassport = new MerchantPassport();
        merchantPassport.setStatus(EnumPassportStatus.VALID.getId());
        merchantPassport.setType(passportType.getId());
        merchantPassport.setExpireTime(System.currentTimeMillis() + MerchantConsts.TOKEN_EXPIRE_MILLIS);
        merchantPassport.setLoginStatus(loginStatus.getId());
        merchantPassport.setMerchantId(merchantId);
        final MerchantPassport merchantPassportInDB = this.merchantPassportDao.selectByMerchantId(merchantId);
        if (null == merchantPassportInDB) {
            this.merchantPassportDao.insert(merchantPassport);
        } else {
            this.merchantPassportDao.updateByMerchantId(merchantPassport);
        }

        return merchantPassport;
    }

    /**
     * {@inheritDoc}
     *
     * @param token
     * @param passportType
     * @return
     */
    @Override
    public Optional<MerchantPassport> getByToken(final String token, final EnumPassportType passportType) {
        return Optional.fromNullable(this.merchantPassportDao.selectByToken(token, passportType.getId()));
    }

    /**
     * {@inheritDoc}
     *
     * @param passportId
     * @return
     */
    @Override
    public int markAsLogin(final long passportId) {
        return this.merchantPassportDao.updateLoginStatusById(passportId, EnumLoginStatus.LOGIN.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param passportId
     * @return
     */
    @Override
    public int refreshToken(final long passportId) {
        return this.merchantPassportDao.updateExpireTimeById(passportId, System.currentTimeMillis() + MerchantConsts.TOKEN_EXPIRE_MILLIS);
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @return
     */
    @Override
    public int markAsLogout(final long merchantId) {
        return this.merchantPassportDao.updateLoginStatusByMerchantId(merchantId, EnumLoginStatus.LOGOUT.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @return
     */
    @Override
    public Optional<MerchantPassport> selectByMerchantId(final long merchantId) {
        return Optional.fromNullable(this.merchantPassportDao.selectByMerchantId(merchantId));
    }
}
