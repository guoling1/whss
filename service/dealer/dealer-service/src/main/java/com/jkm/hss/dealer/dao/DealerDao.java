package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.requestparam.ListDealerRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Repository
public interface DealerDao {

    /**
     * 插入
     *
     * @param dealer
     */
    void insert(Dealer dealer);

    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int update(Dealer dealer);

    /**
     * 更新登录时间
     *
     * @param dealerId
     */
    void updateLoginDate(@Param("dealerId") long dealerId);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Dealer selectById(@Param("id") long id);

    /**
     * 按accountId查询
     *
     * @param accountId
     * @return
     */
    Dealer selectByAccountId(@Param("accountId") long accountId);

    /**
     * 按ids查询
     *
     * @param ids
     * @return
     */
    List<Dealer> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 按mobile查询
     *
     * @param mobile
     * @return
     */
    Dealer selectByMobile(@Param("mobile") String mobile);

    /**
     * 按mobile查询
     *
     * @param mobile
     * @param dealerId
     * @return
     */
    Dealer selectByMobileUnIncludeNow(@Param("mobile") String mobile, @Param("dealerId") long dealerId);

    /**
     *查询所有的一级经销商
     *
     * @return
     */
    List<Dealer> selectAllFirstLevelDealers();

    /**
     * 查询某一级经销商下的所有二级经销商
     *
     * @param firstLevelDealerId
     * @return
     */
    List<Dealer> selectSecondLevelDealersByFirstLevelDealerId(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询某二级经销商下的所有三级经销商
     *
     * @param secondLevelDealerId
     * @return
     */
    List<Dealer> selectThirdLevelDealersBySecondLevelDealerId(@Param("secondLevelDealerId") long secondLevelDealerId);


    /**
     * 按代理名称查询
     *
     * @param name
     * @return
     */
    long selectByProxyName(@Param("name") String name);

    /**
     * 按代理名称查询
     * @param proxyMame
     * @param dealerId
     * @return
     */
    long selectByProxyNameUnIncludeNow(@Param("proxyMame") String proxyMame, @Param("dealerId") long dealerId);

    /**
     * 按手机号和名称模糊匹配
     *
     * @param condition
     * @return
     */
    List<Dealer> selectByCondition(@Param("condition") String condition, @Param("dealerId") long dealerId, @Param("level") int level);

    /**
     * 一级代理发展的二级代理的个数
     *
     * @param firstDealerId
     * @return
     */
    int selectMyDealerCount(@Param("firstDealerId") long firstDealerId);

    /**
     * 查询个数
     *
     * @param listDealerRequest
     * @return
     */
    int selectDealerCountByPageParams(ListDealerRequest listDealerRequest);

    /**
     * 分页查询dealer
     *
     * @param listDealerRequest
     * @return
     */
    List<Dealer> selectDealersByPageParams(ListDealerRequest listDealerRequest);
}
