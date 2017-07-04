package com.jkm.base.common.spring;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by hutao on 15/8/20.
 * 下午4:20
 */
@ContextConfiguration(locations = {"classpath:spring/base-common-spring-test.xml"})
public abstract class TestBase extends AbstractJUnit4SpringContextTests {
}
