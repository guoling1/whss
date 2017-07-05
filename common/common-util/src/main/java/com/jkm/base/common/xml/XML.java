package com.jkm.base.common.xml;

import com.google.common.base.Throwables;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by huangwei on 1/30/15.
 */
public final class XML {

    private XML() {
    }

    /**
     * 获取 XStream 实例
     *
     * @param autodetectAnnotations
     * @return
     */
    public static XStream getXStreamInstance(final boolean autodetectAnnotations) {
        final XStream xStream = new XStream(new XppDriver() {
            /**
             * {@inheritDoc}
             *
             * @param out
             * @return
             */
            @Override
            public HierarchicalStreamWriter createWriter(final Writer out) {
                return new PrettyPrintWriter(out, getNameCoder()) {
                    protected String PREFIX_CDATA = "<![CDATA[";
                    protected String SUFFIX_CDATA = "]]>";

                    /**
                     * {@inheritDoc}
                     * @param writer
                     * @param text
                     */
                    @Override
                    protected void writeText(final QuickWriter writer, final String text) {
                        if (text.startsWith(this.PREFIX_CDATA) && text.endsWith(this.SUFFIX_CDATA)) {
                            writer.write(text);
                        } else {
                            super.writeText(writer, text);
                        }
                    }
                };
            }
        });
        xStream.ignoreUnknownElements();
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.addPermission(NullPermission.NULL);
        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xStream.autodetectAnnotations(autodetectAnnotations);
        return xStream;
    }

    /**
     * xml转对象
     *
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> T fromXML(final String xml, final Class<T>... clazz) {
        XStream xStream = null;
        if (clazz.length == 0) {
            xStream = getXStreamInstance(true);
        } else {
            xStream = getXStreamInstance(false);
            xStream.processAnnotations(clazz);
        }
        return (T) xStream.fromXML(xml);
    }

    /**
     * 对象转xml
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toXML(final T t) {
        final XStream xStream = getXStreamInstance(false);
        xStream.processAnnotations(t.getClass());
        return xStream.toXML(t);
    }

    /**
     * 对象转xml
     * 没有换行和空格
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toCompressXML(final T t) {
        final XStream xStream = getXStreamInstance(false);
        xStream.processAnnotations(t.getClass());
        try (final Writer writer = new StringWriter()) {
            xStream.marshal(t, new CompactWriter(writer));
            return writer.toString();
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
