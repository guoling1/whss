package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.entity.HsyMerchantAuditResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
@Repository
public interface HsyMerchantAuditDao {

    /**
     * 查询商户列表
     */
    List<HsyMerchantAuditResponse> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询商户详情
     */
    HsyMerchantAuditResponse getDetails(@Param("id") Long id);

    /**
     * 审核通过
     */
    void updateAuditPass(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 审核不通过
     */
    void updateAuditNotPass(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询总条数
     * @param hsyMerchantAuditRequest
     * @return
     */
    int getCount(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    HsyMerchantAuditResponse selectById(@Param("id") Long id);

    void updateAccount(@Param("accountID") Long accountID, @Param("id") Long id);

    /**
     * 查询城市(哪个区)
     * @param districtCode
     * @return
     */
    HsyMerchantAuditResponse getCode(@Param("districtCode") String districtCode);


    /**
     * 查询城市
     * @param parentCode
     * @return
     */
    HsyMerchantAuditResponse getCity(@Param("parentCode") String parentCode);

    /**
     * 审核不通过更改提交资料步骤
     */
    void stepChange(@Param("uid") int uid);

    int getUid(@Param("id") Long id);

    /**
     *
     * @param parentCode
     * @return
     */
    HsyMerchantAuditResponse getCityOnly(@Param("parentCode") String parentCode);

    AppAuUser getAccId(@Param("id") Long id);


}
