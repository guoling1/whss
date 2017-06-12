package com.jkm.hsy.user.help.requestparam;

import com.jkm.hsy.user.entity.UserTradeRate;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Data
public class UserTradeRateListRequest {
    private List<UserTradeRate> rateList;

}
