package com.jkm.base.common.spring.advice.exception.interceptor;

import com.jkm.base.common.spring.advice.AdviceTestBase;
import com.jkm.base.common.spring.advice.exception.B;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/12/4.
 * 下午3:38
 */
public class ExceptionInterceptorAdviceTest extends AdviceTestBase {
    @Autowired
    private B b;

    @Test
    public void should_handle_state_exception() throws Exception {
        assertThat(b.func1(), is(11));
    }

    @Test
    public void should_not_handle_no_state_exception() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("argument exception");
        b.func2();
    }

    @Test
    public void should_not_handle_when_no_exception_throw() throws Exception {
        assertThat(b.func3(), is(10));
    }
}