package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.entity.BankBranch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    /**
     * 新增联行号
     * @param bankName
     * @param province
     * @param city
     * @param branchName
     * @param branchCode
     */
    void addBankCode(String bankName, String province, String city, String branchName, String branchCode);

    /**
     * 联行号管理
     * @param map
     * @return
     */
    List<AppBizBankBranchResponse> getUnionInfo(Map map);
}
