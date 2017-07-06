package com.jkm.base.common.spring.advice;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by hutao on 15/8/20.
 * 下午4:20
 */
@ContextConfiguration(locations = {"classpath:spring/base-common-spring-advice-test.xml"})
public abstract class AdviceTestBase extends AbstractJUnit4SpringContextTests {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
}
