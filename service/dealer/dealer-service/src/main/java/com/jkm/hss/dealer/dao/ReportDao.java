package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.STDealerRecord;
import com.jkm.hss.dealer.helper.response.DealerRegCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.dealer.helper.requestparam.DealerReportRequest;
import java.util.List;
import com.jkm.hss.dealer.entity.STDealerHistory;
/**
 * Created by wayne on 17/4/27.
 */
@Repository
public interface ReportDao {

    /**
     * 查询分润总额
     * @param accountid
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getDayProfit(@Param("accountid") long accountid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS直属商户交易额
     * @return
     */
    BigDecimal getHSSDayMertradeAmountDir(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY直属商户交易额
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getHSYDayMertradeAmountDir(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     * HSS下级代理商户交易额
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getHSSDayMertradeAmountSub(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     * HSY下级代理商户交易额
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getHSYDayMertradeAmountSub(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS直属商户注册数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSSDayregMerNumberDir(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY直属商户注册数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSYDayregMerNumberDir(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS下级代理商户注册数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSSDayregMerNumberSub(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY下级代理商户注册数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSYDayregMerNumberSub(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     * HSS直属商户审核数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSSDaycheckMerNumberDir(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS下级代理商户审核数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSSDaycheckMerNumberSub(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY直属商户审核数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSYDaycheckMerNumberDir(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY下级代理商户审核数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getHSYDaycheckMerNumberSub(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 二维码总数
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getQrCodeNumberfirst(@Param("productType") String productType,@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);
//    /**
//     * HSY二维码总数
//     * @param dealerid
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    Integer getHSYQrCodeNumberfirst(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 二维码总数-二级
     * @param dealerid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getQrCodeNumbersecond(@Param("productType") String productType,@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);
//    /**
//     * HSY二维码总数-二级
//     * @param dealerid
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    Integer getHSYQrCodeNumbersecond(@Param("dealerid") long dealerid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 代理商数据日报表
     * @param stDealerRecord
     */
    void insertstdealerrecord(STDealerRecord stDealerRecord);

    STDealerRecord getstdealerrecord(@Param("dealerid") long dealerid,@Param("recordDay") String recordDay,@Param("sysType") String sysType);

    /**
     * HSS历史直属商户注册数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSSHisTJregMerNumberDir(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS历史下级代理商户注册数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSSHisTJregMerNumberSub(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS历史直属商户审核数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSSHisTJcheckMerNumberDir(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSS历史下级代理商户审核数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSSHisTJcheckMerNumberSub(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     * HSY历史直属商户注册数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSYHisTJregMerNumberDir(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY历史下级代理商户注册数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSYHisTJregMerNumberSub(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY历史直属商户审核数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSYHisTJcheckMerNumberDir(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * HSY历史下级代理商户审核数
     * @param startTime
     * @param endTime
     * @return
     */
    List<DealerRegCheck> getHSYHisTJcheckMerNumberSub(@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<DealerRegCheck> getHisTJQrCodeNumberfirst(@Param("productType") String productType,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<DealerRegCheck> getHisTJQrCodeNumbersecond(@Param("productType") String productType,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<STDealerHistory> getdealerhistory(@Param("dealerId") Long dealerId);

    void insertdealerhistory(STDealerHistory stDealerHistory);

    void updatedealerhistory(STDealerHistory stDealerHistory);


}
