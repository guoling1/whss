package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;

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
     * 获取最新信用卡信息
     * @param accountId
     * @return
     */
    AccountBank getCreditCard(long accountId);

    /**
     * 查询信用卡列表
     * @param accountId
     * @return
     */
    List<AccountBank> selectCreditCardList(long accountId);


    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    Optional<AccountBank> selectById(long id);

    /**
     * 我的银行卡列表
     * @param accountId
     * @return
     */
    List<AccountBank> selectAll(long accountId);

    /**
     * 修改支行信息
     * @param continueBankInfoRequest
     * @return
     */
    int updateBranchInfo(ContinueBankInfoRequest continueBankInfoRequest);
}
