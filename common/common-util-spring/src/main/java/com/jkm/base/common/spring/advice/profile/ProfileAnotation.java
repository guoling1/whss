package com.jkm.base.common.spring.advice.profile;

import java.lang.annotation.*;

/**
 * Created by hutao on 15/7/8.
 * 下午3:04
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProfileAnotation {
}