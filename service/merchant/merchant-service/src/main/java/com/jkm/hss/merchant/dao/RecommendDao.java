package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.entity.RecommendShort;
import com.jkm.hss.merchant.helper.request.RecommendRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Repository
public interface RecommendDao {
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
    Recommend selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<Recommend> selectAll();

    /**
     * 我的好友
     * @return
     */
    List<RecommendShort> myRecommend(@Param("merchantId") long merchantId);

    /**
     * 我的真实好友数量（验证通过并且消费达标并且是直接好友）
     * @param merchantId
     * @return
     */
    int selectFriendCount(@Param("merchantId") long merchantId);
    /**
     * 推荐我的人
     * @return
     */
    List<Recommend> selectRecommend(@Param("merchantId") long merchantId);

    /**
     * 按分页查询好友列表
     * @return
     */
    List<RecommendShort> selectRecommendByPage(RecommendRequest recommendRequest);
    /**
     * 按分页查询超级合伙人客户
     * @return
     */
    List<RecommendShort> selectSuperRecommendByPage(RecommendRequest recommendRequest);
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
    int selectIndirectCount(@Param("merchantId") long merchantId);

    /**
     * 直接好友数量
     * @param merchantId
     * @return
     */
    int selectDirectCount(@Param("merchantId") long merchantId);

    /**
     * 查询商户的直接好友
     * @param merchantId
     * @return
     */
    List<Recommend> selectDirectFriend(@Param("merchantId") long merchantId);
}
