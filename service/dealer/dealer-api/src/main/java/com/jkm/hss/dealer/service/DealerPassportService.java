package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerPassport;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import com.jkm.hss.dealer.enums.EnumPassportType;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
public interface DealerPassportService {


    /**
     * 新建passport
     *
     * @param dealerId
     * @param passportType
     * @param loginStatus
     * @return
     */
    DealerPassport createPassport(long dealerId, EnumPassportType passportType, EnumLoginStatus loginStatus);

    /**
     * 根据token获取passport
     *
     * @param token
     * @param passportType
     * @return
     */
    Optional<DealerPassport> getByToken(String token, EnumPassportType passportType);

    /**
     * 标记为登录
     *
     * @param passportId
     * @return
     */
    int markAsLogin(long passportId);

    /**
     * 刷新passport过期时间
     *
     * @param passportId
     * @return
     */
    int refreshToken(long passportId);

    /**
     * 标记为退出
     *
     * @param dealerId
     * @return
     */
    int markAsLogout(long dealerId);

    /**
     * 根据dealerId查询
     *
     * @param dealerId
     * @return
     */
    Optional<DealerPassport> selectByDealerId(long dealerId);
}
