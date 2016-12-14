package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerPassportDao;
import com.jkm.hss.dealer.entity.DealerPassport;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import com.jkm.hss.dealer.enums.EnumPassportStatus;
import com.jkm.hss.dealer.enums.EnumPassportType;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.dealer.service.DealerPassportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@Service
public class DealerPassportServiceImpl implements DealerPassportService {

    @Autowired
    private DealerPassportDao dealerPassportDao;
    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param passportType
     * @param loginStatus
     * @return
     */
    @Override
    @Transactional
    public DealerPassport createPassport(final long dealerId, final EnumPassportType passportType, final EnumLoginStatus loginStatus) {
        final DealerPassport dealerPassport = new DealerPassport();
        dealerPassport.setStatus(EnumPassportStatus.VALID.getId());
        dealerPassport.setType(passportType.getId());
        dealerPassport.setExpireTime(System.currentTimeMillis() + DealerConsts.TOKEN_EXPIRE_MILLIS);
        dealerPassport.setLoginStatus(loginStatus.getId());
        dealerPassport.setDealerId(dealerId);
        final DealerPassport dealerPassportInDB = this.dealerPassportDao.selectByDealerId(dealerId);
        if (null == dealerPassportInDB) {
            this.dealerPassportDao.insert(dealerPassport);
        } else {
            this.dealerPassportDao.updateByDealerId(dealerPassport);
        }

        return dealerPassport;
    }

    /**
     * {@inheritDoc}
     *
     * @param token
     * @param passportType
     * @return
     */
    @Override
    public Optional<DealerPassport> getByToken(final String token, final EnumPassportType passportType) {
        return Optional.fromNullable(this.dealerPassportDao.selectByToken(token, passportType.getId()));
    }

    /**
     * {@inheritDoc}
     *
     * @param passportId
     * @return
     */
    @Override
    public int markAsLogin(final long passportId) {
        return this.dealerPassportDao.updateLoginStatusById(passportId, EnumLoginStatus.LOGIN.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param passportId
     * @return
     */
    @Override
    public int refreshToken(final long passportId) {
        return this.dealerPassportDao.updateExpireTimeById(passportId, System.currentTimeMillis() + DealerConsts.TOKEN_EXPIRE_MILLIS);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public int markAsLogout(final long dealerId) {
        return this.dealerPassportDao.updateLoginStatusByDealerId(dealerId, EnumLoginStatus.LOGOUT.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public Optional<DealerPassport> selectByDealerId(final long dealerId) {
        return Optional.fromNullable(this.dealerPassportDao.selectByDealerId(dealerId));
    }
}
