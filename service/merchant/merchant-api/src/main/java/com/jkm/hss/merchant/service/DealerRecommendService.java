package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.DealerRecommend;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.helper.request.DealerRecommendRequest;
import com.jkm.hss.merchant.helper.request.RecommendRequest;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public interface DealerRecommendService {
    /**
     * 初始化
     * @param dealerRecommend
     */
    int insert(DealerRecommend dealerRecommend);

    /**
     * 按分页查询好友列表
     * @return
     */
    RecommendAndMerchant selectRecommend(DealerRecommendRequest dealerRecommendRequest);

}
