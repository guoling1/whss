package com.jkm.base.common.spring.advice.profile;

import com.jkm.base.common.spring.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hutao on 15/9/9.
 * 下午2:38
 */
public class ProfileInterceptorTest extends TestBase {
    @Autowired
    private ProfileTestClass1 profileTestClass1;

    @Test
    public void testName() throws Exception {
        profileTestClass1.func1();
    }
}