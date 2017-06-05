package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.entity.BankBranch;
import com.jkm.hsy.user.entity.AppBizDistrict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/23.
 */
@Repository
public interface BankBranchDao {
    /**
     *根据支行名称和查询条件查询
     * @param bankName
     * @param contions
     * @return
     */
    List<BankBranch> findByBankName(@Param("bankName") String bankName,@Param("contions") String contions,@Param("provinceName") String provinceName,@Param("cityName") String cityName);

    /**
     * 查询银行名称
     * @return
     */
    List<AppBizBankBranchResponse> getBankName(@Param("bankName") String bankName);

    public List<AppBizDistrict> findDistrictByParentCode(AppBizDistrict appBizDistrict);
}
