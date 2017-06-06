package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.entity.BankBranch;

import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/2/23.
 */
public interface BankBranchService {
    /**
     * 根据支行名称和查询条件查询
     * @param bankName
     * @param contions
     * @return
     */
    List<BankBranch> findByBankName(String bankName,String contions,String provinceName,String cityName);

    /**
     * 查询银行名称
     * @return
     */
    List<AppBizBankBranchResponse> getBankName(String bankName);

    /**
     * 新增联行号
     * @param bankName
     * @param province
     * @param city
     * @param branchName
     * @param branchCode
     */
    void addBankCode(String bankName, String province, String city, String branchName, String branchCode, String belongCityName, String belongProvinceName);

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
     * 支行信息
     * @param districtCode
     * @param bankName
     * @param branchName
     * @return
     */
    List<AppBizBankBranchResponse> getBranch(String districtCode, String bankName, String branchName);

    /**
     * 补填联行号
     * @param sid
     */
    void updateBranch(Long sid, String branchCode, String districtCode);
}
