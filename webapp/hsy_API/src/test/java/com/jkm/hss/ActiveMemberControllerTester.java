package com.jkm.hss;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.util.AppDateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Allen on 2017/5/12.
 */
public class ActiveMemberControllerTester {
    @Test
    public void testInsertMemshipCard()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001047");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.6");
        String param="{\"membershipName\": \"会员卡名称\",\"membershipShopName\":\"卡上显示的店铺姓名\",\"uid\":71,\"sids\":\"62,63\",\"discount\":7.5,\"isDeposited\":1,\"depositAmount\":100,\"isPresentedViaActivate\":1,\"presentAmount\":13,\"isPresentedViaRecharge\":1,\"rechargeLimitAmount\":50,\"rechargePresentAmount\":12}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }
}
