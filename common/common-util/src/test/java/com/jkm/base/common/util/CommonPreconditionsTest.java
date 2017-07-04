package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 16/3/3.
 * 下午3:57
 */
public class CommonPreconditionsTest {
    @Test(expected = TestException.class)
    public void should_success_use_common_preconditions() throws Exception {
        final CommonPreconditions preconditions = CommonPreconditions.create(new CommonPreconditions.ExceptionFactory<TestException>() {
            /**
             * {@inheritDoc}
             */
            @Override
            public TestException create(final String msg) {
                return new TestException(msg);
            }
        });
        preconditions.assertThat(false, "aaa");
        assertThat("should not be there", false, is(true));

    }

    private static class TestException extends RuntimeException {
        public TestException(final String message) {
            super(message);
        }
    }
}