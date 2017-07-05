package com.jkm.base.common.util;

import org.junit.Test;

/**
 * Created by hutao on 15/8/12.
 * 上午11:46
 */
public class SnGeneratorTest {
    @Test
    public void should_success_generate_new_sn() throws Exception {
        System.out.println(SnGenerator.generate());
        System.out.println(SnGenerator.generate(30));
        System.out.println(SnGenerator.generate("a", 32));
    }
}