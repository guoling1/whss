package com.jkm.hss;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.util.AppAesUtil;
import com.jkm.hsy.user.util.AppDateUtil;
import org.junit.Test;

import java.net.URLEncoder;
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
        String param="{\"membershipName\": \"会员卡名称\",\"membershipShopName\":\"卡上显示的店铺姓名\",\"uid\":215,\"sids\":\"62,63\",\"discount\":7.5,\"isDeposited\":1,\"depositAmount\":100,\"isPresentedViaActivate\":1,\"presentAmount\":13,\"isPresentedViaRecharge\":1,\"rechargeLimitAmount\":50,\"rechargePresentAmount\":12}";
//        String param="{\"membershipName\": \"会员卡名称不充值\",\"membershipShopName\":\"卡上显示的店铺姓名\",\"uid\":71,\"sids\":\"62,63\",\"discount\":7.5,\"isDeposited\":0,\"isPresentedViaActivate\":0,\"isPresentedViaRecharge\":0}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void findMemshipCards()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001051");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.6");
        String param="{\"uid\":71}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testEnAndDe()throws Exception{
        String data="215";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(data, "utf-8", "61243d4fa76d5a64", "1234567812345678");
        String base65D= AppAesUtil.decryptCBC_NoPaddingFromBase64String(base64E, "utf-8", "61243d4fa76d5a64", "1234567812345678");
        String httpEncode= URLEncoder.encode(base64E,"utf-8");
        System.out.println(base64E);
        System.out.println(base65D);
        System.out.println(httpEncode);
    }

}
