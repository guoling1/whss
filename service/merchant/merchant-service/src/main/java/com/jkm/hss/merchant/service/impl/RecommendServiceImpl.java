package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.RecommendDao;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.entity.RecommendShort;
import com.jkm.hss.merchant.enums.EnumRecommendType;
import com.jkm.hss.merchant.helper.request.RecommendRequest;
import com.jkm.hss.merchant.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Service
public class RecommendServiceImpl implements RecommendService{
    @Autowired
    private RecommendDao recommendDao;
    /**
     * 初始化
     *
     * @param recommend
     */
    @Override
    public int insert(Recommend recommend) {
        return recommendDao.insert(recommend);
    }

    /**
     * 修改
     *
     * @param recommend
     * @return
     */
    @Override
    public int update(Recommend recommend) {
        return recommendDao.update(recommend);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Recommend> selectById(long id) {
        return Optional.fromNullable(recommendDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<Recommend> selectAll() {
        return recommendDao.selectAll();
    }


    /**
     * 我的真实好友数量（验证通过并且消费达标）
     *
     * @param merchantId
     * @return
     */
    @Override
    public int selectFriendCount(long merchantId) {
        return recommendDao.selectFriendCount(merchantId);
    }

    /**
     * 推荐我的人
     *
     * @param merchantId
     * @return
     */
    @Override
    public List<Recommend> selectRecommend(long merchantId) {
        return recommendDao.selectRecommend(merchantId);
    }

    /**
     * 按分页查询好友列表
     *
     * @param recommendRequest
     * @return
     */
    @Override
    public List<RecommendShort> selectRecommendByPage(RecommendRequest recommendRequest) {
        return recommendDao.selectRecommendByPage(recommendRequest);
    }

    /**
     * 按分页查询好友列表总条数
     *
     * @param recommendRequest
     * @return
     */
    @Override
    public int selectRecommendCount(RecommendRequest recommendRequest) {
        return recommendDao.selectRecommendCount(recommendRequest);
    }

    /**
     * 间接好友数量
     *
     * @param merchantId
     * @return
     */
    @Override
    public int selectIndirectCount(long merchantId) {
        return recommendDao.selectIndirectCount(merchantId);
    }

    /**
     * 直接好友数量
     *
     * @param merchantId
     * @return
     */
    @Override
    public int selectDirectCount(long merchantId) {
        return recommendDao.selectDirectCount(merchantId);
    }

}
