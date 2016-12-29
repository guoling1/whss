package com.jkm.hss.admin.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.hss.admin.dao.AdminUserDao;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.entity.CodeQueryResponse;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.service.AdminUserPassportService;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.admin.service.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Created by huangwei on 5/27/16.
 */
@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private AdminUserPassportService adminUserPassportService;

    /**
     * {@inheritDoc}
     *
     * @param adminUser
     */
    @Override
    public void createUser(final AdminUser adminUser) {
        final String salt = AdminUserSupporter.generateSalt();
        final String password = AdminUserSupporter.encryptPassword(salt, adminUser.getPassword());
        adminUser.setSalt(salt);
        adminUser.setPassword(password);
        adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
        adminUser.setMobile(AdminUserSupporter.encryptMobile(adminUser.getMobile()));
        this.adminUserDao.insert(adminUser);
    }

    /**
     * {@inheritDoc}
     *
     * @param adminUser
     * @return
     */
    @Override
    public int update(final AdminUser adminUser) {
        final String password = AdminUserSupporter.encryptPassword(adminUser.getSalt(), adminUser.getPassword());
        adminUser.setPassword(password);
        adminUser.setMobile(AdminUserSupporter.encryptMobile(adminUser.getMobile()));
        return this.adminUserDao.update(adminUser);
    }

    /**
     * {@inheritDoc}
     *
     * @param auid
     * @return
     */
    @Override
    public Optional<AdminUser> getAdminUserById(final long auid) {
        return Optional.fromNullable(adminUserDao.selectById(auid));
    }

    /**
     * {@inheritDoc}
     *
     * @param username
     * @return
     */
    @Override
    public Optional<AdminUser> getAdminUserByName(final String username) {
        return Optional.fromNullable(adminUserDao.selectByUsername(username));
    }

    @Override
    public void disableUser(long auid) {

    }

    @Override
    public void activeUser(long auid) {

    }

    /**
     * {@inheritDoc}
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Optional<AdminUserPassport> login(final String username, final String password) {
        final Optional<AdminUser> adminUserOptional = getAdminUserByName(username);
        if (adminUserOptional.isPresent()) {
            final AdminUser adminUser = adminUserOptional.get();
            if (AdminUserSupporter.isPasswordCorrect(adminUser, password)) {
                final AdminUserPassport adminUserToken = adminUserPassportService.generateToken(adminUser.getId());
                return Optional.of(adminUserToken);
            }
        }

        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     *
     * @param auid
     */
    @Override
    public void logout(final long auid) {
        adminUserPassportService.invalidatePassport(auid);
    }

    /**
     * {@inheritDoc}
     *
     * @param adminId
     * @param firstLevelDealerId
     * @param count
     * @return
     */
    @Override
    @Transactional
    synchronized public Triple<Integer, String, List<Pair<QRCode, QRCode>>> distributeQRCode(final long adminId,
                                                                                             final long firstLevelDealerId, final int count) {
        final List<Long> unDistributeList = this.qrCodeService.getUnDistributeCode();
        log.info("未分配的二维码数量[{}]", unDistributeList.size());
        if (unDistributeList.size() < count) {
            return Triple.of(0, "未分配的二维码数量[" + unDistributeList.size() + "]小于要分配的个数[" + count+ "]", null);
        }
        final List<Long> ids = unDistributeList.subList(0, count);
        final int updateCount = this.qrCodeService.markCodeToDealer(firstLevelDealerId, ids);
        log.info("分配二维码数量[{}], 实际要分配数量[{}]", updateCount, count);
        Preconditions.checkState(updateCount == count, "mark code to dealer error!!!");
        final List<QRCode> codes = this.qrCodeService.getByIds(ids);
        final List<Pair<QRCode, QRCode>> pairs = this.qrCodeService.getPairQRCodeList(codes);
        return Triple.of(1, "分配成功", pairs);
    }

    /**
     * {@inheritDoc}
     *
     *
     * @param firstLevelDealerId
     * @param startCode
     * @param endCode
     */
    @Override
    @Transactional
    synchronized public List<Pair<QRCode, QRCode>> distributeRangeQRCode(final long firstLevelDealerId,
                                                                         final String startCode, final String endCode) {
        final List<Long> codeIds = this.qrCodeService.getUnDistributeCodeByRangeCode(startCode, endCode);
        if (CollectionUtils.isEmpty(codeIds)) {
            return Collections.emptyList();
        }
        final int updateCount = this.qrCodeService.markCodeToDealer(firstLevelDealerId, codeIds);
        Preconditions.checkState(updateCount == codeIds.size(), "mark code to dealer error!!!");
        final List<QRCode> codes = this.qrCodeService.getByIds(codeIds);
        final List<Pair<QRCode, QRCode>> pairs = this.qrCodeService.getPairQRCodeList(codes);
        return pairs;
    }

    @Override
    public CodeQueryResponse getCode(final String code) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getCode(code);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getProxyName(long firstLevelDealerId) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getProxyName(firstLevelDealerId);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getProxyName1(long secondLevelDealerId) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getProxyName1(secondLevelDealerId);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getMerchantName(long merchantId) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getMerchantName(merchantId);
        return codeQueryResponse;
    }

}
