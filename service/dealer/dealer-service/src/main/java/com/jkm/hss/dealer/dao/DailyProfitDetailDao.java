package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.DailyProfitDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-28.
 */
@Repository
public interface DailyProfitDetailDao {

    /**
     * 初始化
     * @param dailyProfitDetail
     */
    void init(DailyProfitDetail dailyProfitDetail);

    /**
     * 查询分润详情 to商户
     * @param dealerId
     * @return
     */
    List<DailyProfitDetail> selectByFirstDealerId(@Param("dealerId") long dealerId);

    /**
     * 查询分润详情 to商户
     * @param dealerId
     * @return
     */
    List<DailyProfitDetail> selectBySecondDealerId(@Param("dealerId") long dealerId);

    /**
     *  查询分润详情 to二级
     * @param dealerId
     * @return
     */
    List<DailyProfitDetail> selectToSecondDealer(@Param("dealerId") long dealerId);

    /**
     * 查询记录
     * @param dealerId
     * @return
     */
    DailyProfitDetail selectByFirstDealerIdAndType(@Param("dealerId") long dealerId, @Param("type") int type);

    /**
     * 更新
     * @param dailyProfitDetail
     */
    void update(DailyProfitDetail dailyProfitDetail);

    /**
     * 查询
     * @param dealerId
     * @param dealerName
     * @param profitDate
     * @param index
     * @param pageSize
     * @return
     */
    List<DailyProfitDetail> selectSecondByParam(@Param("dealerId") long dealerId, @Param("dealerName") String dealerName,
                                                @Param("profitDate") String profitDate, @Param("index") int index, @Param("pageSize") int pageSize);

    /**
     * 查询
     * @param dealerId
     * @param dealerName
     * @param profitDate
     * @param index
     * @param pageSize
     * @return
     */
    List<DailyProfitDetail> selectFirstByParam(@Param("dealerId") long dealerId, @Param("dealerName") String dealerName,
                                               @Param("profitDate") String profitDate, @Param("index") int index, @Param("pageSize") int pageSize);
}
