package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.helper.response.PartnerRuleSettingResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/4.
 */
@Repository
public interface PartnerRuleSettingDao{
    /**
     * 新增
     * @param partnerRuleSetting
     */
    void insert(PartnerRuleSetting partnerRuleSetting);
    /**
     * 修改
     * @param partnerRuleSetting
     */
    void update(PartnerRuleSetting partnerRuleSetting);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    PartnerRuleSetting selectById(@Param("id") long id);
    /**
     * 根据产品编码查询升级规则
     * @return
     */
    List<PartnerRuleSettingResponse> selectAllByProductId(@Param("productId") long productId);
    /**
     * 升级降费率升级页
     * @return
     */
    List<PartnerRuleSettingResponse> selectAllItemByProductId(@Param("dealerId") long dealerId,@Param("productId") long productId);

    /**
     * 查询
     * @param productId
     * @param channelSign
     * @return
     */
    PartnerRuleSetting selectByProductIdAndChannelSign(@Param("productId") long productId, @Param("channelSign") int channelSign);

    /**
     * 根据产品编码和通道编码查询
     * @param productId
     * @param channelTypeSign
     * @return
     */
    PartnerRuleSetting selectByProductIdAndChannelTypeSign(@Param("productId") long productId,@Param("channelTypeSign") int channelTypeSign);
}
