package com.jkm.hsy.user.service;


import com.google.common.base.Optional;
import com.jkm.hsy.user.entity.PcUserPassport;

import java.util.Date;

/**
 * Created by huangwei on 6/3/15.
 */
public interface PcUserPassportService {

    /**
     * 新建token
     *
     * @param auid
     * @return
     */
    PcUserPassport generateToken(long auid);

    /**
     * 根据token获取
     *
     * @param token
     * @return
     */
    Optional<PcUserPassport> getByToken(String token);

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
     * @param uid
     */
    void invalidatePassport(long uid);

    /**
     * 登出
     *
     * @param id
     */
    void logout(long id);
}
