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
     */
    void addBankCode(Map map);

    /**
     * 联行号管理
     * @param map
     * @return
     */
    List<AppBizBankBranchResponse> getUnionInfo(Map map);

    /**
     * 联行号管理总数
     * @param map
     * @return
     */
    int getUnionInfoCount(Map map);

    /**
     * 查询支行信息
     * @param districtCode
     * @return
     */
    List<AppBizBankBranchResponse> getBranch(@Param("districtCode") String districtCode,@Param("bankName") String bankName,@Param("branchName") String branchName);

    /**
     * 补填联行号
     * @param sid
     */
    void updateBranch(@Param("branchName") String branchName,@Param("sid") Long sid,@Param("branchCode") String branchCode,@Param("districtCode") String districtCode);
}
