package com.jkm.hss.merchant.service;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.merchant.helper.request.ApiMerchantRequest;
import net.sf.json.JSONObject;

/**
 * Created by xingliujie on 2017/8/15.
 */
public interface ApiMerchantService {
    /**
     * 商户入网
     * @return
     */
    CommonResponse merchantIn(ApiMerchantRequest apiMerchantRequest);

}
