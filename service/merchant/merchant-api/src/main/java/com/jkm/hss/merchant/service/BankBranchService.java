package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.BankBranch;

import java.util.List;

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
    List<BankBranch> findByBankName(String bankName,String contions);
}
