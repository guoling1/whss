package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.DealerRecommend;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.entity.RecommendShort;
import com.jkm.hss.merchant.helper.request.DealerRecommendRequest;
import com.jkm.hss.merchant.helper.request.RecommendRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Repository
public interface DealerRecommendDao {
    /**
     * 初始化
     * @param dealerRecommend
     */
    int insert(DealerRecommend dealerRecommend);
    /**
     * 按分页查询好友列表
     * @return
     */
    List<RecommendShort> selectRecommendByPage(DealerRecommendRequest dealerRecommendRequest);
}
