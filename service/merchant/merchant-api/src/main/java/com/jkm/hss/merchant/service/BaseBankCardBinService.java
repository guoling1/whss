package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.BankCardBin;

/**
 * Created by zhangbin on 2016/11/29.
 */
public interface BaseBankCardBinService {

    Optional<BankCardBin> loadByBinNo(final String bankNo);

}
