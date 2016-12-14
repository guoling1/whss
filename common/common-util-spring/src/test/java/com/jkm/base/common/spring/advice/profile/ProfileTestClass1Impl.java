package com.jkm.base.common.spring.advice.profile;

import com.jkm.base.common.util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/9/9.
 * 下午2:39
 */
@Component
public class ProfileTestClass1Impl implements ProfileTestClass1 {
    @Autowired
    private ProfileTestClass2 profileTestClass2;

    @ProfileAnotation
    @Override
    public void func1() {
        ThreadUtil.sleep(10);
        profileTestClass2.func2();
        ThreadUtil.sleep(10);
    }
}
