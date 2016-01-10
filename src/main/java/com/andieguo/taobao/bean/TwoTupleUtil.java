package com.andieguo.taobao.bean;

public class TwoTupleUtil {

    public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
        return new TwoTuple<A, B>(a, b);
    }
}
