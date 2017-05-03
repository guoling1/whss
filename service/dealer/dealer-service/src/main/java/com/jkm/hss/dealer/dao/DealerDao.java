package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.*;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.DealerOfFirstDealerResponse;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
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
     * 按accountIds查询
     *
     * @param accountIds
     * @return
     */
    List<Dealer> selectByAccountIds(@Param("accountIds") List<Long> accountIds);

    /**
     * 按ids查询
     *
     * @param ids
     * @return
     */
    List<Dealer> selectByIds(@Param("ids") List<Long> ids);


    /**
     * 按mobile和oemType查询
     *
     * @param mobile
     * @return
     */
    Dealer getByMobile(@Param("mobile") String mobile);

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
    long selectByProxyNameAndOemType(@Param("name") String name,@Param("oemType") int oemType);

    /**
     * 按代理名称查询
     * @param proxyMame
     * @param dealerId
     * @return
     */
    long selectByProxyNameUnIncludeNow(@Param("proxyMame") String proxyMame,@Param("oemType") int oemType, @Param("dealerId") long dealerId);

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


    /**
     * 写入markCode
     * @param markCode
     * @param dealerId
     * @return
     */
    int updateMarkCode(@Param("markCode") String markCode, @Param("dealerId") long dealerId);


//==============================此处为对二级代理商进行重构=============================

    /**
     * 查询一级代理个数
     *
     * @param listFirstDealerRequest
     * @return
     */
    Long selectFirstDealerCountByPageParams(ListFirstDealerRequest listFirstDealerRequest);
    /**
     * 分页查询一级代理
     *
     * @param listFirstDealerRequest
     * @return
     */
    List<FirstDealerResponse> selectFirstDealersByPageParams(ListFirstDealerRequest listFirstDealerRequest);
    /**
     * 查询二级代理个数
     *
     * @param listFirstDealerRequest
     * @return
     */
    int selectSecondDealerCountByPageParams(ListSecondDealerRequest listFirstDealerRequest);
    /**
     * 分页查询二级代理
     *
     * @param listFirstDealerRequest
     * @return
     */
    List<SecondDealerResponse> selectSecondDealersByPageParams(ListSecondDealerRequest listFirstDealerRequest);

    /**
     * 写入markCode和inviteCode
     * @param markCode
     * @param inviteCode
     * @param dealerId
     * @return
     */
    int updateMarkCodeAndInviteCode(@Param("markCode") String markCode, @Param("inviteCode") String inviteCode, @Param("dealerId") long dealerId);
    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int update2(Dealer dealer);

    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int updateRecommendBtnAndTotalProfitSpace(Dealer dealer);
    /**
     * 更新推广开关
     *
     * @param dealer
     * @return
     */
    int updateInviteBtn(Dealer dealer);

    /**
     * 按inviteCode查询
     *
     * @param inviteCode
     * @return
     */
    Dealer selectByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     * 修改密码
     * @param loginPwd
     * @param dealerId
     * @return
     */
    int updatePwd(@Param("loginPwd") String loginPwd, @Param("dealerId") long dealerId);


    /**
     * 【代理商后台】查询二级代理个数
     *
     * @param listFirstDealerRequest
     * @return
     */
    int selectSecondDealerCountByPage(SecondDealerSearchRequest listFirstDealerRequest);
    /**
     * 【代理商后台】分页查询二级代理
     *
     * @param secondDealerSearchRequest
     * @return
     */
    List<SecondDealerResponse> selectSecondDealersByPage(SecondDealerSearchRequest secondDealerSearchRequest);

    /**
     * 按登陆名称查询
     *
     * @param name
     * @return
     */
    long selectByLoginName(@Param("name") String name);
    /**
     * 按登陆名称查询
     * @param loginName
     * @param dealerId
     * @return
     */
    long selectByLoginNameUnIncludeNow(@Param("loginName") String loginName, @Param("dealerId") long dealerId);

    /**
     * 根据产品类型和手机号或代理商名称模糊查询
     * @param dealerOfFirstDealerRequest
     * @return
     */
    List<DealerOfFirstDealerResponse> selectListOfFirstDealer(DealerOfFirstDealerRequest dealerOfFirstDealerRequest);

    /**
     *
     * @param loginName
     * @return
     */
    Dealer getDealerByLoginName(@Param("loginName") String loginName);

    String selectProxyName(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询代理商名称和编码
     * @param firstLevelDealerId
     * @return
     */
    MerchantInfoResponse getProxyName(int firstLevelDealerId);

    /**
     * 查询一级代理商的编码和名称
     * @param firstDealerId
     * @return
     */
    MerchantInfoResponse getInfo(@Param("firstDealerId") long firstDealerId);

    /**
     * 查询二级代理商编码和名称
     * @param secondDealerId
     * @return
     */
    MerchantInfoResponse getInfo1(@Param("secondDealerId") long secondDealerId);

    /**
     * 查询一级代理商下的所有的商户
     * @param req
     * @return
     */
    List<QueryMerchantResponse> dealerMerchantList(QueryMerchantRequest req);

    /**
     * 查询一级代理商下的所有的商户总数
     * @param req
     * @return
     */
    int dealerMerchantCount(QueryMerchantRequest req);

    /**
     * 查询二级代理商下的所有的商户
     * @param req
     * @return
     */
    List<QueryMerchantResponse> dealerMerchantSecondList(QueryMerchantRequest req);

    /**
     * 查询二级代理商下的所有的商户总数
     * @param req
     * @return
     */
    int dealerMerchantSecondCount(QueryMerchantRequest req);

    /**
     * 根据markCode查询代理商信息
     * @param markCode
     * @return
     */
    Dealer getDealerByMarkCode(@Param("markCode") String markCode);

    /**
     * 所有代理商
     * @return
     */
    List<Dealer> selectAllDealers();

    /**
     * 分公司账户列表查询
     * @param req
     * @return
     */
    List<BranchAccountResponse> getBranch(BranchAccountRequest req);

    /**
     * 分公司账户列表总数查询
     * @param req
     * @return
     */
    int getBranchCount(BranchAccountRequest req);
}
