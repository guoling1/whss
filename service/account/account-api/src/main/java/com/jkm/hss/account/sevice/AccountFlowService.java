package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
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
     * @param accountId
     * @param orderNo  交易订单号
     * @param changeAmount  变动金额
     * @param remark  备注
     * @param type 变更方向
     */
    void addAccountFlow(long accountId, String orderNo, BigDecimal changeAmount, String remark, EnumAccountFlowType type);

    /**
     *  分页查询
     * @param pageNo
     * @param pageSize
     * @param flowSn
     * @param type
     * @param beginDate
     * @param endDate
     * @return
     */
    PageModel<AccountFlow> selectByParam(int pageNo, int pageSize, long accountId, String flowSn, int type, String beginDate, String endDate);
}
