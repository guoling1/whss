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
        bizMapper.put("HSS001003", new String[]{"appVersionService","getAppVersion"});
    }
}
