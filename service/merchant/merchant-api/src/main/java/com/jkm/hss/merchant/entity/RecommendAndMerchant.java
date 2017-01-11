package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.PageModel;
import lombok.Data;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Data
public class RecommendAndMerchant{
    /**
     * 直接好友数
     */
    private int directCount;
    /**
     * 间接好友数
     */
    private int indirectCount;
    /**
     * 好友列表
     */
    private List<RecommendShort> recommends;
}
