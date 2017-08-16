package com.jkm.api.service;

import com.jkm.api.helper.requestparam.MerchantRequest;
import com.jkm.base.common.entity.CommonResponse;

import java.util.Map;

/**
 * Created by xingliujie on 2017/8/16.
 */
public interface MerchantService {
    public Map<String,String> merchantIn(MerchantRequest apiMerchantRequest);
}
