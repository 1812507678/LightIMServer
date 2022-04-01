package com.ideaout.im.http;

public class RequestParse<T> {
    public String requestNo;
    public long requestTimestamp;
    public long userID;
    public String token;
    public T requestData;
    public int channel; //app渠道
    public String sign; //数据签名
    public String deviceInfo; //设备信息


    @Override
    public String toString() {
        return "RequestParamBase{" +
                "requestNo='" + requestNo + '\'' +
                ", requestTimestamp='" + requestTimestamp + '\'' +
                ", userId='" + userID + '\'' +
                ", token='" + token + '\'' +
                ", requestData=" + requestData +
                '}';
    }
}
