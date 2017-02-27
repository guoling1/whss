package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.BankBranchDao;
import com.jkm.hss.merchant.entity.BankBranch;
import com.jkm.hss.merchant.service.BankBranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
