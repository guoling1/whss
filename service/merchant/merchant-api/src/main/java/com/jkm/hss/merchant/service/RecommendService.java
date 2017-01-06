package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.entity.RecommendShort;
import com.jkm.hss.merchant.helper.request.RecommendRequest;

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
     * 我的真实好友数量（验证通过并且消费达标）
     * @param merchantId
     * @return
     */
    int selectFriendCount(long merchantId);

    /**
     * 推荐我的人
     * @return
     */
    List<Recommend> selectRecommend(long merchantId);
    /**
     * 按分页查询好友列表
     * @return
     */
    RecommendAndMerchant selectRecommend(RecommendRequest recommendRequest);
    /**
     * 按分页查询好友列表总条数
     * @return
     */
    int selectRecommendCount(RecommendRequest recommendRequest);

    /**
     * 间接好友数量
     * @param merchantId
     * @return
     */
    int selectIndirectCount(long merchantId);

    /**
     * 直接好友数量
     * @param merchantId
     * @return
     */
    int selectDirectCount(long merchantId);

}
