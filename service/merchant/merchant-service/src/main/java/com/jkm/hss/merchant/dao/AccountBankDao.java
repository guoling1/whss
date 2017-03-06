package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AccountBank;
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
     * 全部设置为不是默认银行卡
     * @return
     */
    int reset(@Param("accountId") long accountId);

    /**
     * 获取默认银行卡信息
     * @param accountId
     * @return
     */
    AccountBank getDefault(@Param("accountId") long accountId);
    /**
     * 获取信用卡信息
     * @param accountId
     * @return
     */
    AccountBank getCreditCard(@Param("accountId") long accountId);

    /**
     * 是否有信用卡
     * @param accountId
     * @return
     */
    int isHasCreditCard(@Param("accountId") long accountId);
    /**
     * 查询银行卡列表
     * @param accountId
     * @return
     */
    List<AccountBank> selectAllByAccountId(@Param("accountId") long accountId);

}
