package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserTradeRate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 修改
     * @param userTradeRate
     */
    void update(UserTradeRate userTradeRate);

    /**
     * 根据法人编码和政策类型查询
     * @return
     */
    UserTradeRate selectByUserIdAndPolicyType(@Param("userId") long userId,@Param("policyType") String policyType);

    /**
     * 查询法人支付信息
     * @param userId
     * @return
     */
    List<UserTradeRate> selectAllByUserId(@Param("userId") long userId);

    /**
     * 根据用户编码删除
     * @param userId
     */
    void deleteByUserId(@Param("userId") long userId);
}
