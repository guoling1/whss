package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.BankCardBin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangbin on 2016/11/29.
 */
@Repository
public interface BankCardBinDao {
    BankCardBin loadByBinNo(@Param("bankNo")String bankNo);

    /**
     * 查询信用卡
     * @param bankNo
     * @return
     */
    BankCardBin loadByBinNoAndType(@Param("bankNo")String bankNo);

}
