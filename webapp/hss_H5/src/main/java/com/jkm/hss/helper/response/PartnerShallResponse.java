package com.jkm.hss.helper.response;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import lombok.Data;

/**
 * Created by yuxiang on 2017-01-11.
 */
@Data
public class PartnerShallResponse {

    private String totalShall;

    private PageModel<JSONObject> pageModel;
}
