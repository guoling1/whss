package com.jkm.hss.dealer.dao;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import com.jkm.hss.account.entity.Account;

/**
 * Created by wayne on 17/4/27.
 */
@Repository
public interface ReportDao {

    /**
     * 查询分润总额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getDayProfit(Date startTime, Date endTime);

    /**
     * 直属商户交易额
     * @param dealerId
     * @param {@link com.jkm.hss.product.enums.EnumProductType}
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getDayMertradeAmountDir(String dealerId,String sysType,Date startTime, Date endTime);

    /**
     * 下级代理商户交易额
     * @param dealerId
     * @param sysType
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getDayMertradeAmountSub(String dealerId,String sysType,Date startTime, Date endTime);

    /**
     * 直属商户注册数
     * @param dealerId
     * @param sysType
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getDayregMerNumberDir(String dealerId,String sysType,Date startTime, Date endTime);

    /**
     * 下级代理商户注册数
     * @param dealerId
     * @param sysType
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getDayregMerNumberSub(String dealerId,String sysType,Date startTime, Date endTime);

    /**
     * 直属商户审核数
     * @param dealerId
     * @param sysType
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getDaycheckMerNumberDir(String dealerId,String sysType,Date startTime, Date endTime);

    /**
     * 下级代理商户审核数
     * @param dealerId
     * @param sysType
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getDaycheckMerNumberSub(String dealerId,String sysType,Date startTime, Date endTime);

    /**
     * 二维码总数
     * @param dealerId
     * @param sysType
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getQrCodeNumber(String dealerId,String sysType,Date startTime, Date endTime);
}
