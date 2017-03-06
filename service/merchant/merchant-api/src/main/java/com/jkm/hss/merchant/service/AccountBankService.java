package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.AccountBank;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/6.
 */
public interface AccountBankService {
    /**
     * 是否有银行卡信息
     * @param accountId
     * @return
     */
    int isHasAccountBank(long accountId);
    /**
     * 初始化银行卡账户
     * @return
     */
    int initAccountBank(long merchantId,long accountId);
    /**
     * 新增
     * @param accountBank
     * @return
     */
    int insert(AccountBank accountBank);
    /**
     * 修改
     * @param accountBank
     * @return
     */
    int update(AccountBank accountBank);

    /**
     * 设置为默认银行卡
     * @param id
     * @return
     */
    int setDefault(long id);

    /**
     * 全部设置为不是默认银行卡
     * @return
     */
    int reset(long accountId);

    /**
     * 获取默认银行卡信息
     * @param accountId
     * @return
     */
    AccountBank getDefault(long accountId);
    /**
     * 获取信用卡信息
     * @param accountId
     * @return
     */
    AccountBank getCreditCard(long accountId);

    /**
     * 是否有信用卡
     * @param accountId
     * @return
     */
    int isHasCreditCard(long accountId);
    /**
     * 查询银行卡列表
     * @param accountId
     * @return
     */
    List<AccountBank> selectAllByAccountId(long accountId);
}
