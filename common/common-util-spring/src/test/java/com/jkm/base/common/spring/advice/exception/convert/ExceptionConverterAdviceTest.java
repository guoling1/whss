package com.jkm.base.common.spring.advice.exception.convert;

import com.jkm.base.common.spring.advice.AdviceTestBase;
import com.jkm.base.common.spring.advice.exception.A;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/12/4.
 * 下午3:38
 */
public class ExceptionConverterAdviceTest extends AdviceTestBase {
    @Autowired
    private A a;

    @Test
    public void should_convert_state_exception() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("my error message");
        a.func1();
    }

    @Test
    public void should_not_convert_no_state_exception() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("argument exception");
        a.func2();
    }

    @Test
    public void should_not_convert_when_no_exception_throw() throws Exception {
        assertThat(a.func3(), is(10));
    }
}