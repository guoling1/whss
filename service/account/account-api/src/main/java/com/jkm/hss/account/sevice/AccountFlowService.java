package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface AccountFlowService {
    /**
     * 插入
     *
     * @param accountFlow
     */
    void add(AccountFlow accountFlow);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<AccountFlow> getById(long id);

    /**
     * 按交易订单号,账户id查询
     *
     * @param orderNo
     * @param accountId
     * @param type
     * @return
     */
    Optional<AccountFlow> getByOrderNoAndAccountIdAndType(String orderNo, long accountId, int type);

    /**
     * 添加流水
     *
     * @param account
     * @param orderNo  交易订单号
     * @param changeAmount  变动金额
     * @param remark  备注
     * @param type 变更方向
     */
    void addAccountFlow(Account account, String orderNo, BigDecimal changeAmount, String remark, EnumAccountFlowType type);
}
