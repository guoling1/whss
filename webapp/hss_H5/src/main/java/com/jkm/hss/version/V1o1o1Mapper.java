package com.jkm.hss.version;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingliujie on 2017/7/24.
 */
public class V1o1o1Mapper {
    public static Map<String,String[]> bizMapper=new HashMap<String, String[]>();
    static{
        bizMapper.put("HSS001001", new String[]{"appAuTokenService","insertTokenDeviceClientInfoAndReturnKey"});
        bizMapper.put("HSS001002", new String[]{"appAuTokenService","updateClientID"});
        bizMapper.put("HSS001003", new String[]{"appVersionService","findVersionDetail"});
        bizMapper.put("HSS001004", new String[]{"appVersionService","getAppVersion"});
        bizMapper.put("HSS001005", new String[]{"appMerchantInfoService","getCode","发送验证码成功"});
        bizMapper.put("HSS001006", new String[]{"appMerchantInfoService","register","注册成功"});
        bizMapper.put("HSS001007", new String[]{"appMerchantInfoService","login","登录成功"});
        bizMapper.put("HSS001008", new String[]{"appMerchantInfoService","logout","退出成功"});
        bizMapper.put("HSS001009", new String[]{"appAuTokenService","getInitOemInfo"});
        bizMapper.put("HSS001010", new String[]{"appMerchantInfoService","getShareInfo"});
        bizMapper.put("HSS001011", new String[]{"appCenterLettersService","plusDownLoad","更新成功"});
        bizMapper.put("HSS001012", new String[]{"appCenterLettersService","getCenterLettersList"});
        bizMapper.put("HSS001013", new String[]{"appMessageService","getMessageList"});
        bizMapper.put("HSS001014", new String[]{"appMessageService","updateReadStatus"});
        bizMapper.put("HSS001015", new String[]{"appMerchantInfoService","noticeList"});
        bizMapper.put("HSS001016", new String[]{"appMerchantInfoService","getFlag"});
    }
}
