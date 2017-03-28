package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import com.jkm.hss.merchant.helper.response.BankListResponse;

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
     * 是否有信用卡信息
     * @param accountId
     * @return
     */
    int isHasCreditBank(long accountId);
    /**
     * 初始化银行卡账户
     * @return
     */
    int initAccountBank(long merchantId,long accountId);

    /**
     * 初始化信用卡
     * @param accountId
     * @param bankNo
     * @param bankName
     * @param reserveMobile
     * @param bankBin
     * @param expiryTime
     * @return
     */
    long initCreditBankCard(long accountId,String bankNo,String bankName,String reserveMobile,String bankBin,String expiryTime);
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
     * 设置为默认信用卡（用于支付成功之后设置默认信用卡）
     * @param id
     * @return
     */
    int setDefaultCreditCard(long id);
    /**
     * 设置为默认信用卡(用于删除信用卡之后设置默认信用卡)
     * @param id
     * @return
     */
    int setDefaultCreditCardById(long id);

    /**
     * 全部设置为不是默认银行卡
     * @return
     */
    int reset(long accountId,int cardType);


    /**
     * 获取默认银行卡
     * @param accountId
     * @return
     */
    AccountBank getDefault(long accountId);
    /**
     * 获取默认信用卡
     * @param accountId
     * @return
     */
    AccountBank getDefaultCreditCard(long accountId);

    /**
     * 查询解密过的信用卡列表
     * @param accountId
     * @return
     */
    List<AccountBank> selectCreditList(long accountId);

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
    List<BankListResponse> selectAll(long accountId);

    /**
     * 修改支行信息
     * @param continueBankInfoRequest
     * @return
     */
    int updateBranchInfo(ContinueBankInfoRequest continueBankInfoRequest);
    /**
     * 删除信用卡
     * @param id
     * @return
     */
    int deleteCreditCard(long id);

    /**
     * 更改默认银行卡
     * @param merchantInfo
     * @param bankNo
     * @param reserveMobile
     * @return
     */
    int changeBankCard(MerchantInfo merchantInfo, String bankNo, String reserveMobile);
    /**
     * 更改默认银行卡（已存在银行卡）
     * @param bankId
     * @param merchantInfo
     * @param bankNo
     * @param reserveMobile
     * @return
     */
    public int updateDefaultBankCard(long bankId,MerchantInfo merchantInfo, String bankNo, String reserveMobile);
    /**
     * 是否有银行卡
     * @param accountId
     * @param bankNo
     * @return
     */
    Optional<AccountBank> isExistBankNo(long accountId,String bankNo,int cardType);
    /**
     * 无状态查询信用卡
     * @param id
     * @return
     */
    Optional<AccountBank> selectStatelessById(long id);

    /**
     * 根据信用卡号查询信用卡
     * @param accountId
     * @param bankNo
     * @return
     */
    Optional<AccountBank> selectCreditCardByBankNo(long accountId,  String bankNo);
    /**
     * 无状态查询信用卡
     * @param accountId
     * @param bankNo
     * @return
     */
    Optional<AccountBank> selectCreditCardByBankNoAndStateless(long accountId,  String bankNo);

    /**
     * 获取最新信用卡
     * @param accountId
     * @return
     */
    Optional<AccountBank> getTopCreditCard(long accountId);
}
