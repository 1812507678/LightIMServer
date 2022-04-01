package com.ideaout.im.http;

public class ResponseDataBase<T> {
    public int code;
    public T data;
    public String errorDec;
    public String requestNo;
    public String clientIp;


    @Override
    public String toString() {
        return "ResponseDataBase{" +
                "code=" + code +
                ", data=" + data +
                ", errorDec='" + errorDec + '\'' +
                '}';
    }
}
