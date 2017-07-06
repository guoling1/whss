package com.jkm.hss.dealer.helper.response;

import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/5/8.
 */
@Data
public class DealerProfitSettingResponse {
    /**
     * 通道唯一标识...
     * {@link EnumPayChannelSign}
     */
    private Integer channelTypeSign;
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 分润空间
     */
    private BigDecimal profitSpace;
}
