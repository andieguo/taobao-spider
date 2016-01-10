package com.andieguo.taobao.bean;

public class ThreeTupleUtil {

    public static <A,B,C> ThreeTuple<A,B,C> tuple(A a, B b,C c) {
        return new ThreeTuple<A,B,C>(a, b,c);
    }
}
