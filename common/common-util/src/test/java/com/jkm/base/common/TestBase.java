package com.jkm.base.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by yulong.zhang on 2016/11/15.
 */
@Slf4j
@ContextConfiguration(locations = {"classpath:spring/sqb-common-util-test.xml"})
public class TestBase extends AbstractJUnit4SpringContextTests {
}
