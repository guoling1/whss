package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.requestparam.DistributeQrCodeRequest;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.BossDistributeQRCodeRecordResponse;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.jkm.hss.dealer.entity.*;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.DealerOfFirstDealerResponse;
import com.jkm.hss.dealer.helper.response.DistributeRecordResponse;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.product.enums.EnumProductType;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
public interface DealerService {
    /**
     * 商户入账计算
     */
    Map<String, Pair<Long,BigDecimal>> merchantAmount(OrderRecord orderRecord);

    /**
     * 分润接口 , 好收收分润， 好收银分润
     */
    Map<String, Triple<Long, BigDecimal, BigDecimal>> shallProfit(EnumProductType type, String orderNo, BigDecimal tradeAmount,
                                                                  int channelSign, long merchantId);

    /**
     * 添加代理商
     *
     * @param dealer
     */
    void add(Dealer dealer);

    /**
     * 更新登录时间
     *
     * @param dealerId
     */
    void updateLoginDate(long dealerId);

    /**
     * 创建二级代理商
     *
     * @param request
     * @return
     */
    long createSecondDealer(SecondLevelDealerAddRequest request, long firstLevelDealerId);

    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int update(Dealer dealer);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<Dealer> getById(long id);


    /**
     * 按ids查询
     *
     * @param ids
     * @return
     */
    List<Dealer> getByIds(List<Long> ids);

    /**
     * 按账户id查询
     *
     * @param accountId
     * @return
     */
    Optional<Dealer> getByAccountId(long accountId);

    /**
     * 按账户id查询
     *
     * @param accountIds
     */
    List<Dealer> getByAccountIds(List<Long> accountIds);


    /**
     * 按mobile查询
     *
     * @param mobile
     * @return
     */
    Optional<Dealer> getByMobile(String mobile);

    /**
     * 按mobile查询
     *
     * @param mobile
     * @return
     */
    Optional<Dealer> getByMobileUnIncludeNow(String mobile, long dealerId);

    /**
     *查询所有的一级经销商
     *
     * @return
     */
    List<Dealer> getAllFirstLevelDealers();

    /**
     * 查询某一级经销商下的所有二级经销商
     *
     * @param firstLevelDealerId
     * @return
     */
    List<Dealer> getSecondLevelDealersByFirstLevelDealerId(long firstLevelDealerId);

    /**
     * 查询某二级经销商下的所有三级经销商
     *
     * @param secondLevelDealerId
     * @return
     */
    List<Dealer> getThirdLevelDealersBySecondLevelDealerId(long secondLevelDealerId);

    /**
     * 查询代理商名称是否重复
     *
     * @param proxyMame
     * @return
     */
    long selectByProxyNameAndOemType(String proxyMame,int oemType);

    /**
     * 查询代理商名称是否重复
     *
     * @param proxyMame
     * @return
     */
    long getByProxyNameUnIncludeNow(String proxyMame, int oemType, long dealerId);

    /**
     * 按手机号和名称模糊匹配
     *
     * @param condition
     * @return
     */
    List<Dealer> findByCondition(String condition, long dealerId, int level);

    /**
     * 一级代理商分配码段
     *
     * @param dealerId  一级代理商id
     * @param toDealerId  码段要分配给代理商的id
     * @param startCode  开始二维码
     * @param endCode  结束二维码
     * @return
     */
    List<DistributeQRCodeRecord> distributeQRCode(long dealerId, long toDealerId, String startCode, String endCode);

    /**
     * 当前一级代理商下未分配额二维码
     *
     * @param dealerId
     * @return
     */
    long getUnDistributeCodeCount(long dealerId);

    /**
     * 当前一级代理商下未分配额二维码
     *
     * @param dealerId
     * @return
     */
    List<Pair<QRCode,QRCode>> getUnDistributeCode(long dealerId);


    /**
     * 查询代理商某一二维码范围下的未分配的二维码个数
     *
     * @param startCode
     * @param endCode
     * @param dealerId
     * @return
     */
    int getUnDistributeCodeCountByRangeCode(String startCode, String endCode, long dealerId);

    /**
     * 校验此码段范围的二维码是否可以分配
     *
     * @param startCode
     * @param endCode
     * @return
     */
    boolean checkRangeQRCode(String startCode, String endCode);

    /**
     * 添加一级代理
     *
     * @param firstLevelDealerAddRequest
     * @return
     */
    long createFirstDealer(FirstLevelDealerAddRequest firstLevelDealerAddRequest);

    /**
     * 查询分配给二级代理是的二维码记录
     *
     * @param dealerId
     * @return
     */
    List<DistributeQRCodeRecord> getDistributeToSecondDealerQRCode(long dealerId);

    /**
     * 查询分配给自己是的二维码记录
     *
     * @return
     */
    List<DistributeQRCodeRecord> getDistributeToSelfQRCode(long dealerId);

    /**
     * 查找我的门店
     *
     * @param dealerId
     * @return
     */
    List<MerchantInfo> getMyMerchants(long dealerId);

    /**
     * 查询我发展的二级代理
     *
     * @param dealerId
     */
    List<Triple<Dealer, DistributeCodeCount, ActiveCodeCount>> getMyDealers(long dealerId);

    /**
     * 查询我发展的二级代理的详细信息
     *
     * @param dealerId
     * @param secondLevelDealerId
     */
    Triple<Dealer, List<DistributeQRCodeRecord>, List<DealerChannelRate>> getMyDealerDetail(long dealerId, long secondLevelDealerId);

    /**
     * 一级代理发展的二级代理的个数
     *
     * @param firstDealerId 一级代理商id
     * @return
     */
    int getMyDealerCount(long firstDealerId);

    /**
     * 查询代理商（非加密）
     *
     * @param dealerId
     * @return
     */
    Optional<Dealer> getPlainDealer(long dealerId);

    /**
     * 代理商列表
     *
     * @param listDealerRequest
     * @return
     */
    PageModel<Dealer> listDealer(ListDealerRequest listDealerRequest);

    /**
     * 更新一级代理商
     *
     * @param request
     */
    void updateDealer(FirstLevelDealerUpdateRequest request);

    /**
     * 写入markCode
     * @param markCode
     * @param dealerId
     * @return
     */
    int updateMarkCode(String markCode, long dealerId);

    //==============================此处为对代理商进行重构=============================

    /**
     * 添加一级代理
     *
     * @param firstLevelDealerAdd2Request
     * @return
     */
    long createFirstDealer2(FirstLevelDealerAdd2Request firstLevelDealerAdd2Request);


    /**
     * 添加二级代理
     *
     * @param secondLevelDealerAdd2Request
     * @return
     */
    long createSecondDealer2(SecondLevelDealerAdd2Request secondLevelDealerAdd2Request,long dealerId);
    /**
     * 添加代理商
     *
     * @param dealer
     */
    void add2(Dealer dealer);

    /**
     * 一级代理商列表
     *
     * @param listFirstDealerRequest
     * @return
     */
    PageModel<FirstDealerResponse> listFirstDealer(ListFirstDealerRequest listFirstDealerRequest);
    /**
     * 二级代理商列表
     *
     * @param listSecondDealerRequest
     * @return
     */
    PageModel<SecondDealerResponse> listSecondDealer(ListSecondDealerRequest listSecondDealerRequest);

    /**
     * 写入markCode和inviteCode
     * @param markCode
     * @param inviteCode
     * @param dealerId
     * @return
     */
    int updateMarkCodeAndInviteCode(String markCode, String inviteCode, long dealerId);

    /**
     * 更新一级代理商
     *
     * @param request
     */
    void updateDealer2(FirstLevelDealerUpdate2Request request);
    /**
     * 更新一级代理商
     *
     * @param request
     */
    void updateSecondDealer(SecondLevelDealerUpdate2Request request);
    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int update2(Dealer dealer);

    /**
     * 更新或新增好收收代理商配置信息
     *
     * @param request
     */
    void addOrUpdateHssDealer(HssDealerAddOrUpdateRequest request);
    /**
     * 更新或新增好收收分公司配置信息
     *
     * @param request
     */
    void addOrUpdateHssOem(HssOemAddOrUpdateRequest request);
    /**
     * 更新或新增好收银代理商配置信息
     *
     * @param request
     */
    void addOrUpdateHsyDealer(HsyDealerAddOrUpdateRequest request);

    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int updateRecommendBtnAndTotalProfitSpace(Dealer dealer);
    /**
     * 更新
     *
     * @param dealer
     * @return
     */
    int updateInviteBtn(Dealer dealer);

    /**
     * 根据邀请码查询代理商
     * @param inviteCode
     * @return
     */
    Optional<Dealer> getDealerByInviteCode(String inviteCode);

    /**
     * 修改密码
     * @param loginPwd
     * @param dealerId
     * @return
     */
    int updatePwd(String loginPwd,long dealerId);

    /**
     * 查询登录名称名称是否重复
     *
     * @param loginName
     * @return
     */
    long getByLoginName(String loginName);
    /**
     * 查询登录名是否重复
     *
     * @param loginName
     * @return
     */
    long getByLoginNameUnIncludeNow(String loginName, long dealerId);

    /**
     * 【代理商后台】二级代理商列表
     *
     * @param secondDealerSearchRequest
     * @return
     */
    PageModel<SecondDealerResponse> listSecondDealer(SecondDealerSearchRequest secondDealerSearchRequest);
    /**
     * 【代理商后台】一级代理商列表
     *
     * @param firstDealerSearchRequest
     * @return
     */
    PageModel<FirstDealerResponse> listFirstDealer(FirstDealerSearchRequest firstDealerSearchRequest);
    /**
     * 【代理商后台】新增或修改代理商产品
     *
     * @param request
     */
    void addOrUpdateDealerProduct(DealerAddOrUpdateRequest request,long firstLevelDealerId);
    /**
     * 按码段分配二维码
     * @param type
     * @param dealerId
     * @param toDealerId
     * @param startCode
     * @param endCode
     * @return
     */
    List<DistributeQRCodeRecord> distributeQRCodeByCode(int type,String sysType, long dealerId, long toDealerId, String startCode, String endCode);

    /**
     * 按个数分配
     * @param type
     * @param dealerId
     * @param toDealerId
     * @param count
     * @return
     */
    List<DistributeQRCodeRecord> distributeQRCodeByCount(int type, String sysType, long dealerId, long toDealerId, int count);
    /**
     * 根据产品类型和手机号或代理商名称模糊查询
     * @param dealerOfFirstDealerRequest
     * @return
     */
    List<DealerOfFirstDealerResponse> selectListOfFirstDealer(DealerOfFirstDealerRequest dealerOfFirstDealerRequest);

    /**
     * 根据登录名获取代理商
     * @param loginName
     * @return
     */
    Dealer getDealerByLoginName(String loginName);
    /**
     * 【代理商后台】二维码分配记录
     * @param distributeRecordRequest
     * @return
     */
    PageModel<DistributeRecordResponse> distributeRecord(DistributeRecordRequest distributeRecordRequest, long firstLevelDealerId);
    /**
     * 【boss后台】二维码分配记录
     * @param distributeRecordRequest
     * @return
     */
    PageModel<BossDistributeQRCodeRecordResponse> distributeRecord(DistributeQrCodeRequest distributeRecordRequest);

    /**
     * 根据一级代理商id查询代理商名称
     * @param firstLevelDealerId
     * @return
     */
    String selectProxyName(long firstLevelDealerId);

    /**
     * 根据一级代理商id查询名称
     * @param firstLevelDealerId
     */
    MerchantInfoResponse getProxyName(int firstLevelDealerId);

    /**
     * 查询一级代理商的编码和名称
     * @param firstDealerId
     * @return
     */
    MerchantInfoResponse getInfo(long firstDealerId);
    /**
     * 根据代理商编码查询代理商信息
     * @param markCode
     * @return
     */
    Optional<Dealer> getDealerByMarkCode(String markCode);

    /**
     * 查询二级代理商编码和名称
     * @param secondDealerId
     * @return
     */
    MerchantInfoResponse getInfo1(long secondDealerId);

    /**
     * 查询一级代理商下的所有商户
     * @param req
     * @return
     */
    List<QueryMerchantResponse> dealerMerchantList(QueryMerchantRequest req);

    /**
     * 查询一级代理商下的所有商户总数
     * @param req
     * @return
     */
    int dealerMerchantCount(QueryMerchantRequest req);

    /**
     * 查询二级代理商下的所有商户
     * @param req
     * @return
     */
    List<QueryMerchantResponse> dealerMerchantSecondList(QueryMerchantRequest req);

    /**
     * 查询二级代理商下的所有商户总数
     * @param req
     * @return
     */
    int dealerMerchantSecondCount(QueryMerchantRequest req);

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
