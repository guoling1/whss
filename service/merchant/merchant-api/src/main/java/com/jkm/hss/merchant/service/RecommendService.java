package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public interface RecommendService {
    /**
     * 初始化
     * @param recommend
     */
    int insert(Recommend recommend);

    /**
     * 修改
     * @param recommend
     * @return
     */
    int update(Recommend recommend);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    Optional<Recommend> selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<Recommend> selectAll();
    /**
     * 我推广的好友
     * @return
     */
    RecommendAndMerchant myRecommend(long merchantId);

    /**
     * 我的真实好友数量（验证通过并且消费达标）
     * @param merchantId
     * @return
     */
    int selectFriendCount(long merchantId);
}
