//package com.gzu.common;
//
//import lombok.Data;
//
//@Data
//public class R<T> {
//    private Integer code;
//    private String msg;
//    private Object data;
//
//    public static R<T> ok() {
//        R<T> r = new R<T>();
//        r.setCode(200);
//        return r;
//    }
//    public static R<T> error() {
//        R<T> r = new R<T>();
//        r.setCode(500);
//        return r;
//    }
//    public static R<T> ok(Integer code, String msg) {
//        R<T> r = new R<T>();
//        r.setCode(code);
//        r.setMsg(msg);
//        return r;
//    }
//    public static R<T> error(Integer code, String msg) {
//        R<T> r = new R<T>();
//        r.setCode(code);
//        r.setMsg(msg);
//        return r;
//    }
//
//}
