package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.jkm.hss.dealer.entity.DailyProfitDetail;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerAddRequest;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerUpdateRequest;
import com.jkm.hss.dealer.helper.requestparam.ListDealerRequest;
import com.jkm.hss.dealer.helper.requestparam.SecondLevelDealerAddRequest;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.entity.TradeRecord;
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
     * 分润接口
     */
    Map<String, Triple<Long, BigDecimal, String>> shallProfit(OrderRecord orderRecord);

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
    long getByProxyName(String proxyMame);

    /**
     * 查询代理商名称是否重复
     *
     * @param proxyMame
     * @return
     */
    long getByProxyNameUnIncludeNow(String proxyMame, long dealerId);

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
     * @param count  个数
     * @return
     */
    DistributeQRCodeRecord distributeQRCode(long dealerId, long toDealerId, int count);

    /**
     * 当前一级代理商下未分配额二维码
     *
     * @param dealerId
     * @return
     */
    long getUnDistributeCodeCount(long dealerId);

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
}
