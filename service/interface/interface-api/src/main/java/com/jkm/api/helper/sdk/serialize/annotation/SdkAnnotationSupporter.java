package com.jkm.api.helper.sdk.serialize.annotation;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by yulong.zhang on 2017/2/10.
 */
@UtilityClass
public class SdkAnnotationSupporter {
    public static String getFieldAlias(final Field field) {
        final SdkSerializeAlias aliasAnnotation = field.getAnnotation(SdkSerializeAlias.class);
        if (aliasAnnotation == null) {
            return field.getName();
        }
        return StringUtils.isEmpty(aliasAnnotation.name()) ? field.getName() : aliasAnnotation.name();
    }

    public static int getFieldSignSort(final Field field) {
        final SdkSerializeAlias aliasAnnotation = field.getAnnotation(SdkSerializeAlias.class);
        if (aliasAnnotation == null) {
            return 0;
        }
        return aliasAnnotation.signSort();
    }

    public static boolean getFieldNeedSign(final Field field) {
        final SdkSerializeAlias aliasAnnotation = field.getAnnotation(SdkSerializeAlias.class);
        if (aliasAnnotation == null) {
            return false;
        }
        return aliasAnnotation.needSign();
    }
}
