package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.BankCardBin;

/**
 * Created by zhangbin on 2016/11/25.
 */
public interface BankCardBinService {

    Optional<BankCardBin> analyseCardNo(String bankNo);

    /**
     * 查询信用卡
     * @param bankNo
     * @return
     */
    Optional<BankCardBin> analyseCardNoByType(String bankNo);
}
