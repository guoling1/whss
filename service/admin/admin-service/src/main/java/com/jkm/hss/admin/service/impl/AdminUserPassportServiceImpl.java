package com.jkm.hss.admin.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.admin.dao.AdminUserPassportDao;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.helper.AdminConsts;
import com.jkm.hss.admin.service.AdminUserPassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangwei on 5/27/16.
 */
@Service
public class AdminUserPassportServiceImpl implements AdminUserPassportService {

    @Autowired
    private AdminUserPassportDao adminUserPassportDao;

    /**
     * {@inheritDoc}
     *
     * @param auid
     * @return
     */
    @Override
    public AdminUserPassport generateToken(final long auid) {
        final AdminUserPassport adminUserToken = new AdminUserPassport();
        adminUserToken.setAuid(auid);
        adminUserToken.setExpireTime(System.currentTimeMillis() + AdminConsts.TOKEN_EXPIRE_MILLIS);
        adminUserToken.setStatus(EnumAdminUserStatus.NORMAL.getCode());

        final AdminUserPassport tokenInDB = this.adminUserPassportDao.selectByAuid(auid);
        if (tokenInDB == null) {
            this.adminUserPassportDao.save(adminUserToken);
        } else {
            this.adminUserPassportDao.update(adminUserToken);
        }

        return adminUserToken;
    }

    /**
     * {@inheritDoc}
     *
     * @param token
     * @return
     */
    @Override
    public Optional<AdminUserPassport> getByToken(final String token) {
        return Optional.fromNullable(this.adminUserPassportDao.selectByToken(token));
    }

    /**
     * {@inheritDoc}
     *
     * @param tokenId
     */
    @Override
    public void refreshToken(final long tokenId) {
        final long expireTime = System.currentTimeMillis() + AdminConsts.getAdminConfig().tokenExpireMillis();
        this.adminUserPassportDao.updateExpireTime(tokenId, expireTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param auid
     */
    @Override
    public void invalidatePassport(final long auid) {
        this.adminUserPassportDao.updateStatus(auid, EnumBoolean.FALSE.getCode());
    }
}
