package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.BankBranchDao;
import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.entity.BankBranch;
import com.jkm.hss.merchant.service.BankBranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/2/23.
 */
@Slf4j
@Service
public class BankBranchServiceImpl implements BankBranchService{
    @Autowired
    private BankBranchDao bankBranchDao;
    /**
     * 根据支行名称和查询条件查询
     *
     * @param bankName
     * @param contions
     * @return
     */
    @Override
    public List<BankBranch> findByBankName(String bankName, String contions,String provinceName,String cityName) {
        return bankBranchDao.findByBankName(bankName,contions,provinceName,cityName);
    }

    @Override
    public List<AppBizBankBranchResponse> getBankName(String bankName) {
        List<AppBizBankBranchResponse> list = this.bankBranchDao.getBankName(bankName);
        return list;
    }

    @Override
    public void addBankCode(String bankName, String province, String city, String branchName, String branchCode, String belongCityName, String belongProvinceName) {
        Map map = new HashMap();
        map.put("bankName",bankName);
        map.put("province",province);
        map.put("city",city);
        map.put("branchName",branchName);
        map.put("branchCode",branchCode);
        map.put("belongCityName",belongCityName);
        map.put("belongProvinceName",belongProvinceName);
        this.bankBranchDao.addBankCode(map);
    }

    @Override
    public List<AppBizBankBranchResponse> getUnionInfo(Map map) {
        List<AppBizBankBranchResponse> list = this.bankBranchDao.getUnionInfo(map);
        return list;
    }

    @Override
    public int getUnionInfoCount(Map map) {
        return this.bankBranchDao.getUnionInfoCount(map);
    }

    @Override
    public List<AppBizBankBranchResponse> getBranch(String districtCode, String bankName, String branchName) {
        List<AppBizBankBranchResponse> list = this.bankBranchDao.getBranch(districtCode,bankName,branchName);
        return list;
    }

    @Override
    public void updateBranch(String branchName,Long sid, String branchCode, String districtCode) {
        this.bankBranchDao.updateBranch(branchName,sid,branchCode,districtCode);
    }
}
