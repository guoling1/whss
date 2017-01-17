package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.DailyProfitDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
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
    DailyProfitDetail selectByFirstDealerIdAndTypeAndProfitDate(@Param("dealerId") long dealerId,
                                                                @Param("type") int type, @Param("profitDate")String profitDate);

    /**
     * 更新
     * @param dailyProfitDetail
     */
    void update(DailyProfitDetail dailyProfitDetail);

    /**
     * 查询
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param index
     * @param pageSize
     * @return
     */
    List<DailyProfitDetail> selectSecondByParam(@Param("beginProfitDate") Date beginProfitDate, @Param("dealerName") String dealerName,
                                                @Param("endProfitDate") Date endProfitDate, @Param("index") int index, @Param("pageSize") int pageSize);

    /**
     * 查询
     * @param beginProfitDate
     * @param firstDealerName
     * @param endProfitDate
     * @param index
     * @param pageSize
     * @return
     */
    List<DailyProfitDetail> selectFirstByParam(@Param("beginProfitDate") Date beginProfitDate, @Param("firstDealerName") String firstDealerName,
                                               @Param("endProfitDate") Date endProfitDate, @Param("index") int index, @Param("pageSize") int pageSize);

    /**
     * 查询
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param index
     * @param pageSize
     * @return
     */
    List<DailyProfitDetail> selectCompanyByParam(@Param("beginProfitDate") Date beginProfitDate, @Param("dealerName") String dealerName,
                                                 @Param("endProfitDate") Date endProfitDate, @Param("index") int index, @Param("pageSize") int pageSize);

    /**
     * 查询
     * @param id
     * @return
     */
    DailyProfitDetail selectById(long id);


    DailyProfitDetail selectBySecondDealerIdAndTypeAndProfitDate(@Param("dealerId") long dealerId,
                                                                 @Param("type") int type, @Param("profitDate")String profitDate);
}
