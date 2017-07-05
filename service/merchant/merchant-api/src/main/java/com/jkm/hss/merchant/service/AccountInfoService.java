package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.AccountInfo;

import java.math.BigDecimal;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 14:07
 */
public interface AccountInfoService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    AccountInfo selectByPrimaryKey(Long id);

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(AccountInfo record);

    /**
     * 插入账户
     */
    long addNewAccount();

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective(AccountInfo record);

    /**
     * 商户入账
     * @param id
     * @param available
     * @return
     */
    int addAvailableMoney(long id, BigDecimal available);

    /**
     * 代理商入账
     * @param id
     * @param unsettled
     * @return
     */
    int addUnsettledMoney(long id, BigDecimal unsettled);

    /**
     * 提现（冻结资金）
     * @param frozenAmount
     * @param id
     * @return
     */
    int frozenMoney(BigDecimal frozenAmount,Long id);
    /**
     * 商户出账（将冻结冻结解冻）
     * @param frozenAmount
     * @param id
     * @return
     */
    int inMoney(BigDecimal frozenAmount,Long id);

    /**
     * 代付异常，退款
     * @param frozenAmount
     * @param id
     * @return
     */
    int backMoney(BigDecimal frozenAmount,Long id);

}
