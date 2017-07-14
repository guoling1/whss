package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.helper.AdminConsts;
import com.jkm.hsy.user.dao.PcUserPassportDao;
import com.jkm.hsy.user.entity.PcUserPassport;
import com.jkm.hsy.user.help.PcTokenHelper;
import com.jkm.hsy.user.service.PcUserPassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by huangwei on 5/27/16.
 */
@Service
public class PcUserPassportServiceImpl implements PcUserPassportService {

    @Autowired
    private PcUserPassportDao pcUserPassportDao;

    /**
     * {@inheritDoc}
     *
     * @param uid
     * @return
     */
    @Override
    public PcUserPassport generateToken(final long uid) {
        final PcUserPassport pcUserPassport = new PcUserPassport();
        pcUserPassport.setUid(uid);
        pcUserPassport.setExpireTime(System.currentTimeMillis() + AdminConsts.TOKEN_EXPIRE_MILLIS);
        pcUserPassport.setStatus(EnumAdminUserStatus.NORMAL.getCode());
        pcUserPassport.setLoginStatus(EnumBoolean.TRUE.getCode());
        pcUserPassport.setLastLoginDate(new Date());
        final PcUserPassport tokenInDB = this.pcUserPassportDao.selectByUid(uid);
        if (null != tokenInDB) {
            pcUserPassport.setToken(tokenInDB.getToken());
            this.pcUserPassportDao.update(pcUserPassport);
        } else {
            pcUserPassport.setToken(PcTokenHelper.generateToken(uid));
            this.pcUserPassportDao.add(pcUserPassport);
        }
        return pcUserPassport;
    }

    /**
     * {@inheritDoc}
     *
     * @param token
     * @return
     */
    @Override
    public Optional<PcUserPassport> getByToken(final String token) {
        return Optional.fromNullable(this.pcUserPassportDao.selectByToken(token));
    }

    /**
     * {@inheritDoc}
     *
     * @param tokenId
     */
    @Override
    public void refreshToken(final long tokenId) {
        final long expireTime = System.currentTimeMillis() + AdminConsts.getAdminConfig().tokenExpireMillis();
        this.pcUserPassportDao.updateExpireTime(tokenId, expireTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param uid
     */
    @Override
    public void invalidatePassport(final long uid) {
        this.pcUserPassportDao.updateStatus(uid, EnumBoolean.FALSE.getCode());
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     */
    @Override
    public void logout(final long id) {
        this.pcUserPassportDao.markLogout(id, EnumBoolean.FALSE.getCode());
    }

}
