package com.jkm.hss.helper.response;

import com.jkm.base.common.entity.PageModel;
import lombok.Data;
import net.sf.json.JSONObject;

/**
 * Created by yuxiang on 2017-01-11.
 */
@Data
public class PartnerShallResponse {

    private String totalShall;

    private PageModel<JSONObject> pageModel;

    /**
     * 分润类型
     * 1合伙人分润 2超级合伙人分润 3其它分润
     */
    private int type;
}
