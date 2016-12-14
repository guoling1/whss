package com.jkm.base.common.spring.aliyun;

import com.jkm.base.common.spring.TestBase;
import com.jkm.base.common.spring.aliyun.service.VerifyID4ElementService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
public class VerifyTest extends TestBase {

    @Autowired
    private VerifyID4ElementService verifyID4ElementService;

    @Test
    public void  testVerify() {
        final String bankcard = "6214830107011438";
        final String idCard = "412724199010251531";
        final String mobile = "18640426296";
        final String realName = "张玉龙";
        final Pair<Integer, String> verify = this.verifyID4ElementService.verify(bankcard, idCard, mobile, realName);
        System.out.print(verify.getLeft());
        System.out.print(verify.getRight());
    }
}
