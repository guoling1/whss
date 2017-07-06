package com.jkm.base.common.xml;

import com.thoughtworks.xstream.converters.basic.StringConverter;

/**
 * CDataConverter
 * Created by huangwei on 1/30/15.
 */
public class XStreamCDataConverter extends StringConverter {

    /**
     * {@inheritDoc}
     *
     * @param obj
     * @return
     */
    @Override
    public String toString(final Object obj) {
        return "<![CDATA[" + super.toString(obj) + "]]>";
    }

}
