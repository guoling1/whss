package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.BaseBankCardBinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/25.
 */
@Slf4j
@Service
public class BankCardBinServiceImpl implements BankCardBinService {

    @Autowired
    private BaseBankCardBinService baseBankCardBinService;

    private final static List<Integer> BIN_LENGTH_LIST = Lists.newArrayList(
            9, 10, 8, 7, 6, 5, 4, 3
    );

    @Override
    public Optional<BankCardBin> analyseCardNo(String bankNo) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(bankNo) && bankNo.length() >= 10, "卡号不正确");
        for (final int binLength : BIN_LENGTH_LIST) {
            final Optional<BankCardBin> cardBinOptional = baseBankCardBinService.loadByBinNo(bankNo.substring(0, binLength));
            if (cardBinOptional.isPresent()) {
                return cardBinOptional;
            }
        }

        return Optional.absent();
    }
}
