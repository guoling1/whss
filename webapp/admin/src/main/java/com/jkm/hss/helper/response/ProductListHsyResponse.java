package com.jkm.hss.helper.response;

import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.entity.ProductRatePolicy;
import com.jkm.hss.product.enums.EnumProductType;
import lombok.Data;

import java.util.List;

/**
 * Created by yuxiang on 2017-06-09.
 */
@Data
public class ProductListHsyResponse {

    /**
     * 产品id
     */
    private long productId;

    /**
     * 产品名称 快收银
     */
    private String productName;

    /**
     * 产品通道列表
     */
    private List<ProductRatePolicy> list;

    /**
     * 类型 hss,hsy
     * {@link EnumProductType}
     */
    private String type;
}
