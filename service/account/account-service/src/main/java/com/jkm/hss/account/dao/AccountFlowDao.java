package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.AccountFlow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface AccountFlowDao {

    /**
     * 插入
     *
     * @param accountFlow
     */
    void insert(AccountFlow accountFlow);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    AccountFlow selectById(@Param("id") long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    AccountFlow selectByOrderNoAndAccountIdAndType(@Param("orderNo") String orderNo, @Param("accountId") long accountId, @Param("type") int type);

    /**
     * 分业查询
     * @param firstIndex
     * @param pageSize
     * @param accountId
     * @param flowSn
     * @param type
     * @param beginTime
     * @param endTime
     * @return
     */
    List<AccountFlow> selectByParam(@Param("firstIndex") Integer firstIndex, @Param("pageSize") Integer pageSize, @Param("accountId") Long accountId,
                                    @Param("flowSn") String flowSn, @Param("type") Integer type, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 分页查询
     * @param accountId
     * @param flowSn
     * @param type
     * @param beginTime
     * @param endTime
     * @return
     */
    long selectCountByParam(@Param("accountId") Long accountId, @Param("flowSn") String flowSn, @Param("type") Integer type,
                            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 按流水号查询
     *
     * @param flowNo
     * @return
     */
    int selectCountByFlowNo(@Param("flowNo") String flowNo);

    /**
     * 商户流水分页
     * @param firstIndex
     * @param pageSize
     * @param accountId
     * @return
     */
    List<AccountFlow> selectByParamToMerchantFlow(@Param("firstIndex") int firstIndex, @Param("pageSize") int pageSize, @Param("accountId") long accountId);

    /**
     * 商户流水分页
     *
     * @param accountId
     * @return
     */
    long selectCountByParamToMerchantFlow(@Param("accountId") long accountId);
}
