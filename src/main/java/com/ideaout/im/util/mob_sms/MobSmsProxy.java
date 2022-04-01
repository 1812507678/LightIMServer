package com.ideaout.im.util.mob_sms;

import com.ideaout.im.util.GsonUtil;
import com.ideaout.im.util.HttpRequestProxy;

/*
* 发送短信验证码
* */
public class MobSmsProxy {
    private static final String appkey = "347a18b33cb5a";

    private static MobSmsProxy smsProxy;
    public static MobSmsProxy getInstance(){
        if (smsProxy==null){
            smsProxy = new MobSmsProxy();
        }
        return smsProxy;
    }

    public SmsResult sendMsg(String phone) {
        String url = "https://webapi.sms.mob.com/sms/sendmsg?appkey="+appkey+"&zone=86&phone="+phone;
        String resultStr = HttpRequestProxy.sendGet(url);
        return GsonUtil.fromJson(resultStr, SmsResult.class);
    }

    public SmsResult verify(String phone,String code) {
        String url = "https://webapi.sms.mob.com/sms/verify?appkey="+appkey+"&phone="+phone+"&zone=86&code="+code;
        String resultStr = HttpRequestProxy.sendGet(url);
        return GsonUtil.fromJson(resultStr, SmsResult.class);
    }

    public static class SmsResult{

        /**
         * status : 457
         * error : Phone format error.
         */

        public int status;  //状态码，200为成功
        public String error;

        public static int STATUS_SUCCESS = 200;

    }



}
