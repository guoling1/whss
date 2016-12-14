package com.jkm.base.common.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by huangwei on 6/2/15.
 * Velocity 作为String模板
 */
public final class VelocityStringTemplate {

    private VelocityStringTemplate() {
    }

    /**
     * String模板转换
     *
     * @param template
     * @param params
     * @return
     */
    public static String process(final String template, final Map<String, ?> params) {
        final Context context = new VelocityContext(params);
        final StringWriter sw = new StringWriter();
        Velocity.evaluate(context, sw, "velocity", template);
        return sw.toString();
    }

}
