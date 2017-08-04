package com.jkm.hss.helper.response;

import com.jkm.hss.product.helper.response.PartnerRuleSettingResponse;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/4.
 */
@Data
public class UpgradeMaxResponse {
    private String headimgUrl;
    private String mobile;
    private String name;
    private Integer level;
    List<PartnerRuleSettingResponse> partnerRuleSettingResponses;
}
