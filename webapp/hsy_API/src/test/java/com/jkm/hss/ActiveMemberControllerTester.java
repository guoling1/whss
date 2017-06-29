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
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
//        String param="{\"membershipName\": \"会员卡名称\",\"membershipShopName\":\"卡上显示的店铺姓名\",\"uid\":215,\"sids\":\"211,234\",\"discount\":7.5,\"isDeposited\":1,\"depositAmount\":100,\"isPresentedViaActivate\":1,\"presentAmount\":13,\"isPresentedViaRecharge\":1,\"rechargeLimitAmount\":50,\"rechargePresentAmount\":12,\"canRecharge\":1}";
//        String param="{\"membershipName\": \"会员卡名称不充值\",\"membershipShopName\":\"卡上显示的店铺姓名\",\"uid\":215,\"sids\":\"62,63\",\"discount\":7.5,\"isDeposited\":0,\"isPresentedViaActivate\":0,\"isPresentedViaRecharge\":0,\"canRecharge\":1}";
        String param="{\"membershipName\": \"会员卡需要修改的记录\",\"membershipShopName\":\"卡上显示的店铺姓名\",\"uid\":215,\"sids\":\"211,234\",\"discount\":7.5,\"isDeposited\":1,\"depositAmount\":100,\"isPresentedViaActivate\":1,\"presentAmount\":13,\"isPresentedViaRecharge\":1,\"rechargeLimitAmount\":50,\"rechargePresentAmount\":12,\"canRecharge\":1}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void findMemshipCards()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001051");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"uid\":215}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void findMemberQr()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001052");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\":215}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void findMemshipCardsInfo()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001053");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\":18}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void updateMemshipCardsStatus()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001054");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\":18,\"status\":1}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void updateMemshipCard()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001055");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\":20,\"membershipName\": \"会员卡修改后的记录\",\"membershipShopName\":\"修改后卡上显示的店铺姓名\",\"sids\":\"211,234\",\"depositAmount\":0.01,\"isPresentedViaActivate\":1,\"presentAmount\":10,\"isPresentedViaRecharge\":1,\"rechargeLimitAmount\":0.01,\"rechargePresentAmount\":20}";
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
