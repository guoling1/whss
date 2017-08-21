package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/6.
 */
@Repository
public interface AccountBankDao {
    /**
     * 是否有银行卡信息
     * @param accountId
     * @return
     */
    int isHasAccountBank(@Param("accountId") long accountId);
    /**
     * 是否有信用卡
     * @param accountId
     * @return
     */
    int isHasCreditBank(@Param("accountId") long accountId);
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
    int setDefault(@Param("id") long id);
    /**
     * 设置为默认信用卡
     * @param id
     * @return
     */
    int setDefaultCreditCard(@Param("id") long id);
    /**
     * 设置为默认信用卡（用于删除信用卡之后设置默认信用卡）
     * @param id
     * @return
     */
    int setDefaultCreditCardById(@Param("id") long id);

    /**
     * 全部设置为不是默认银行卡
     * @return
     */
    int reset(@Param("accountId") long accountId,@Param("cardType") int cardType);

    /**
     * 获取默认储蓄卡
     * @param accountId
     * @return
     */
    AccountBank getDefault(@Param("accountId") long accountId);
    /**
     * 获取默认信用卡
     * @param accountId
     * @return
     */
    AccountBank getDefaultCreditCard(@Param("accountId") long accountId);

    /**
     * 查询信用卡列表
     * @param accountId
     * @return
     */
    List<AccountBank> selectCreditCardList(@Param("accountId") long accountId);


    /**
     * 根据主键查询
     * @param id
     * @return
     */
    AccountBank selectById(@Param("id") long id);

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
    int deleteCreditCard(@Param("id") long id);



    /**
     * 是否有银行卡
     * @param accountId
     * @param bankNo
     * @return
     */
    AccountBank selectByBankNo(@Param("accountId") long accountId,@Param("bankNo") String bankNo,@Param("cardType") int cardType);


    /**
     * 无状态查询信用卡
     * @param id
     * @return
     */
    AccountBank selectStatelessById(@Param("id") long id);
    /**
     * 无状态查询信用卡
     * @param accountId
     * @param bankNo
     * @return
     */
    AccountBank selectByBankNoAndStateless(@Param("accountId") long accountId,@Param("bankNo") String bankNo,@Param("cardType") int cardType);
    /**
     * 修改银行卡信息
     * @param accountBank
     * @return
     */
    int updateBankInfo(AccountBank accountBank);
    /**
     * 获取最新信用卡
     * @param accountId
     * @return
     */
    AccountBank getTopCreditCard(@Param("accountId") long accountId);

    /**
     * 清空cvv
     * @param id
     */
    void cleanCvv(@Param("id") long id);
    /**
     * 清空有效期
     * @param id
     */
    void cleanExpiryTime(@Param("id") long id);

    /**
     * 清空cvv和有效期
     * @param id
     */
    void cleanCvvAndExpiryTime(@Param("id") long id);
    /**
     * 根据bankNo查询
     * @param accountId
     * @return
     */
    AccountBank selectCreditListByBankNo(@Param("accountId") long accountId,@Param("bankNo") String bankNo);
}
