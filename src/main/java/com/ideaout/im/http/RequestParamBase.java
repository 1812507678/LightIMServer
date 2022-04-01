package com.ideaout.im.http;

import com.ideaout.im.bean.TokenAttr;

public class RequestParamBase<T> {
    public String requestNo;
    public long requestTimestamp;
    //public int userId;
    public String token;
    public T requestData;
    public int channel; //app渠道
    public String sign; //数据签名
    public String deviceInfo; //设备信息
    public String appStore; //多渠道
    public boolean isNotRepeatCheck;  //不需要放重复校验

    public TokenAttr tokenAttr;


    @Override
    public String toString() {
        return "RequestParamBase{" +
                "requestNo='" + requestNo + '\'' +
                ", requestTimestamp='" + requestTimestamp + '\'' +
                ", token='" + token + '\'' +
                ", requestData=" + requestData +
                '}';
    }
}
