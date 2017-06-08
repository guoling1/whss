package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.hss.dealer.entity.DealerRatePolicy;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2016/12/9.
 */
@Data
public class DealerRatePolicySaveOrUpdateRequest {
    /**
     * 代理商编码
     */
    private long dealerId;
    /**
     * 代理商政策
     */
    private List<DealerRatePolicy> dealerRatePolicies;
}
