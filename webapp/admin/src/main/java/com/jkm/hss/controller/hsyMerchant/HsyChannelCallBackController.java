package com.jkm.hss.controller.hsyMerchant;

import com.alibaba.fastjson.JSONObject;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.XmmsCallBackRequest;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.Enum.EnumNetStatus;
import com.jkm.hsy.user.Enum.EnumOpenProductStatus;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.UserChannelPolicy;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xingliujie on 2017/6/10.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/hsy/callBack")
public class HsyChannelCallBackController extends BaseController {
    @Autowired
    private UserChannelPolicyService userChannelPolicyService;
    @Autowired
    private HsyCmbcDao hsyCmbcDao;
    @ResponseBody
    @RequestMapping(value = "/xmms",method = RequestMethod.POST)
    public void xmms(@RequestBody XmmsCallBackRequest xmmsCallBackRequest){
        log.info("请求参数是{}", JSONObject.toJSON(xmmsCallBackRequest).toString());
        int channelTypeSing = 0;
        if("WXZF".equals(xmmsCallBackRequest.getPayWay())&&"T1".equals(xmmsCallBackRequest.getSettleType())){
            channelTypeSing = EnumPayChannelSign.XMMS_WECHAT_T1.getId();
        }
        if("ZFBZF".equals(xmmsCallBackRequest.getPayWay())&&"T1".equals(xmmsCallBackRequest.getSettleType())){
            channelTypeSing = EnumPayChannelSign.XMMS_ALIPAY_T1.getId();
        }
        if("WXZF".equals(xmmsCallBackRequest.getPayWay())&&"D0".equals(xmmsCallBackRequest.getSettleType())){
            channelTypeSing = EnumPayChannelSign.XMMS_WECHAT_D0.getId();
        }
        if("ZFBZF".equals(xmmsCallBackRequest.getPayWay())&&"D0".equals(xmmsCallBackRequest.getSettleType())){
            channelTypeSing = EnumPayChannelSign.XMMS_ALIPAY_D0.getId();
        }
        if("SUCCESS".equals(xmmsCallBackRequest.getStatus())){
            AppAuUser appAuUser = hsyCmbcDao.selectByGlobalId(xmmsCallBackRequest.getMerchantNo());
            UserChannelPolicy userChannelPolicy = new UserChannelPolicy();
            userChannelPolicy.setUserId(appAuUser.getId());
            userChannelPolicy.setChannelTypeSign(channelTypeSing);
            userChannelPolicy.setOpenProductStatus(EnumOpenProductStatus.PASS.getId());
            userChannelPolicy.setNetStatus(EnumNetStatus.SUCCESS.getId());
            userChannelPolicy.setExchannelCode(xmmsCallBackRequest.getMerchantCode());
            userChannelPolicy.setExchannelEventCode(xmmsCallBackRequest.getChannelMerchantCode());
            userChannelPolicy.setNetMarks(xmmsCallBackRequest.getMsg());
            userChannelPolicy.setOpenProductMarks(xmmsCallBackRequest.getMsg());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(userChannelPolicy);
        }
        if("FAIL".equals(xmmsCallBackRequest.getStatus())){
            AppAuUser appAuUser = hsyCmbcDao.selectByGlobalId(xmmsCallBackRequest.getMerchantNo());
            UserChannelPolicy userChannelPolicy = new UserChannelPolicy();
            userChannelPolicy.setUserId(appAuUser.getId());
            userChannelPolicy.setChannelTypeSign(channelTypeSing);
            userChannelPolicy.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
            userChannelPolicy.setNetStatus(EnumNetStatus.FAIL.getId());
            userChannelPolicy.setExchannelCode(xmmsCallBackRequest.getMerchantCode());
            userChannelPolicy.setExchannelEventCode(xmmsCallBackRequest.getChannelMerchantCode());
            userChannelPolicy.setNetMarks(xmmsCallBackRequest.getMsg());
            userChannelPolicy.setOpenProductMarks(xmmsCallBackRequest.getMsg());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(userChannelPolicy);
        }

    }
}
