package com.jkm.base.common.util;

/**
 * Created by hutao on 15/11/12.
 * 下午12:03
 */
public final class StringFormatter {
    private StringFormatter() {
    }

    /**
     * 格式化字符串，替换字符串中的％s置位符
     *
     * @param template
     * @param args
     * @return
     */
    public static String format(final String template, final Object... args) {
        final String tmpTemplate = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(tmpTemplate.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = tmpTemplate.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(tmpTemplate.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(tmpTemplate.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}
