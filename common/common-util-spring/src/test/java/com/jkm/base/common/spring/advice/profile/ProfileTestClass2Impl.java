package com.jkm.base.common.spring.advice.profile;

import com.jkm.base.common.util.ThreadUtil;
import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/9/9.
 * 下午2:39
 */
@Component
public class ProfileTestClass2Impl implements ProfileTestClass2 {
    @Override
    @ProfileAnotation
    public void func2() {
        ThreadUtil.sleep(20);
    }
}
