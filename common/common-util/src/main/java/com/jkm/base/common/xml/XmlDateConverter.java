package com.jkm.base.common.xml;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;


/**
 * Created by hutao on 11/30/15.
 */
public class XmlDateConverter extends DateConverter {
    private final DateTimeFormatter formatter;

    /**
     * 构造
     *
     * @param dateFormat
     */
    public XmlDateConverter(final String dateFormat) {
        super(dateFormat, new String[]{dateFormat}, TimeZone.getTimeZone("GMT+8"));
        formatter = DateTimeFormat.forPattern(dateFormat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object fromString(final String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return formatter.parseDateTime(str).toDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(final Object obj) {
        return new DateTime(obj).toString(formatter);
    }
}