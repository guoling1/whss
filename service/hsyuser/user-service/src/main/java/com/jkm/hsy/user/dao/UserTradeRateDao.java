package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserTradeRate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Repository
public interface UserTradeRateDao {
    /**
     * 新增
     * @param userTradeRate
     */
    void insert(UserTradeRate userTradeRate);

    /**
     * 根据法人编码和政策类型查询
     * @return
     */
    UserTradeRate selectByUserIdAndPolicyType(@Param("userId") long userId,@Param("policyType") String policyType);
}
