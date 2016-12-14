package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.DealerPassport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Repository
public interface DealerPassportDao {

    /**
     * 插入
     *
     * @param dealerPassport
     */
    void insert(DealerPassport dealerPassport);

    /**
     * 更新
     *
     * @param dealerPassport
     * @return
     */
    int updateByDealerId(DealerPassport dealerPassport);

    /**
     * 更新登录状态
     *
     * @param id
     * @param loginStatus
     * @return
     */
    int updateLoginStatusById(@Param("id") long id, @Param("loginStatus") int loginStatus);

    /**
     * 更新过期时间
     *
     * @param id
     * @param expireTime
     * @return
     */
    int updateExpireTimeById(@Param("id") long id, @Param("expireTime") long expireTime);

    /**
     * 更新登录状态
     *
     * @param dealerId
     * @param loginStatus
     * @return
     */
    int updateLoginStatusByDealerId(@Param("dealerId") long dealerId, @Param("loginStatus") int loginStatus);

    /**
     * 按dealerId查询
     *
     * @param dealerId
     * @return
     */
    DealerPassport selectByDealerId(@Param("dealerId") long dealerId);

    /**
     * 按token查询
     *
     * @param token
     * @param type
     * @return
     */
    DealerPassport selectByToken(@Param("token") String token, @Param("type") int type);
}
