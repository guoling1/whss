package com.jkm.base.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/11/9.
 * 下午9:37
 */
public class JsonUtilTest {
    @Test
    public void testConvertToList() throws Exception {
        final List<String> strList = Lists.newArrayList("a", "b");
        assertThat(strList, is(JsonUtil.convertToList(JsonUtil.toJsonString(strList), String.class)));
        assertThat(JsonUtil.convertToList("", String.class).isEmpty(), is(true));
    }

    @Test
    public void testConvertToListByTypeReference() throws Exception {
        final List<A> aList = Lists.newArrayList(new A(1, 1), new A(1, 2));
        final List<A> aList1 = JsonUtil.convertToListByTypeReference(JsonUtil.toJsonString(aList), new TypeReference<List<A>>() {
        });
        assertThat(aList1.get(0).a, is(1));
        assertThat(aList1.get(0).b, is(1));
        assertThat(aList1.get(1).a, is(1));
        assertThat(aList1.get(1).b, is(2));
    }

    @Test
    public void testParseObject() throws Exception {
        final B<A> b = new B<>();
        b.setT(new A(1, 2));
        b.setA(3);
        final B<A> b1 = JsonUtil.parseObject(JsonUtil.toJsonString(b), new TypeReference<B<A>>() {
        });
        assertThat(b, is(b1));
        final String str = "{\"a\":3}";
        final B<A> b2 = JsonUtil.parseObject(str, new TypeReference<B<A>>() {
        });
        assertThat(b2.getT() == null, is(true));
        assertThat(b2.getA(), is(3));
    }

    private static class A {
        private int a;
        private int b;

        public A() {
        }

        public A(int a, int b) {

            this.a = a;
            this.b = b;
        }

        public int getA() {

            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof A && a == ((A) obj).a && b == ((A) obj).b;
        }
    }

    private static class B<T> {
        private int a;
        private T t;

        public B() {
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            B<?> b = (B<?>) o;

            return t != null ? (a == b.a && t.equals(b.t)) : b.t == null;

        }

        @Override
        public int hashCode() {
            return t != null ? t.hashCode() : 0;
        }
    }
}