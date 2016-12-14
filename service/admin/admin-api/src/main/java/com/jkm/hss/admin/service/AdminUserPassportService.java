package com.jkm.hss.admin.service;


import com.google.common.base.Optional;
import com.jkm.hss.admin.entity.AdminUserPassport;

/**
 * Created by huangwei on 6/3/15.
 */
public interface AdminUserPassportService {
    /**
     * 新建token
     *
     * @param auid
     * @return
     */
    AdminUserPassport generateToken(long auid);

    /**
     * 根据token获取
     *
     * @param token
     * @return
     */
    Optional<AdminUserPassport> getByToken(String token);

    /**
     * 刷新token过期时间
     *
     * @param tokenId
     * @return
     */
    void refreshToken(long tokenId);

    /**
     * 使passport失效
     *
     * @param auid
     */
    void invalidatePassport(long auid);

}
