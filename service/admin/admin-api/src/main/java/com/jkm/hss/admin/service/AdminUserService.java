package com.jkm.hss.admin.service;

import com.google.common.base.Optional;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.entity.AdminUserPassport;
import com.jkm.hss.admin.entity.QRCode;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Date;
import java.util.List;

/**
 * Created by huangwei on 6/3/15.
 */
public interface AdminUserService {
    /**
     * 创建一个后台用户
     *
     * @param adminUser
     * @return
     */
    void createUser(AdminUser adminUser);

    /**
     *
     *
     * @param adminUser
     * @return
     */
    int update(AdminUser adminUser);

    /**
     * 根据auid获取
     *
     * @param auid
     * @return
     */
    Optional<AdminUser> getAdminUserById(long auid);

    /**
     * 根据用户名获取
     *
     * @param username
     * @return
     */
    Optional<AdminUser> getAdminUserByName(final String username);

    /**
     * 禁用用户
     *
     * @param auid
     */
    void disableUser(final long auid);

    /**
     * 激活用户
     *
     * @param auid
     */
    void activeUser(final long auid);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    Optional<AdminUserPassport> login(final String username, final String password);

    /**
     * 登出
     *
     * @param auid
     */
    void logout(final long auid);

    /**
     * 给一级代理商分配码段
     *
     * @param count
     * @param firstLevelDealerId
     * @param count
     * @return
     */
    Triple<Integer, String, List<Pair<QRCode, QRCode>>> distributeQRCode(long adminId, long firstLevelDealerId, int count);

    /**
     * 给一级代理商分配指定范围的码段
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     */
    List<Pair<QRCode, QRCode>> distributeRangeQRCode(long dealerId, String startCode, String endCode);
}
