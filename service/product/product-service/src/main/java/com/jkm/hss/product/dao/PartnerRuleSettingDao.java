package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.PartnerRuleSetting;
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
    List<PartnerRuleSetting> selectAllByProductId(@Param("productId") long productId);
}
