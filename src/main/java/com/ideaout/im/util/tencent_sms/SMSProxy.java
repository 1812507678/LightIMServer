package com.ideaout.im.util.tencent_sms;

import com.ideaout.im.util.GsonUtil;
import com.ideaout.im.util.HttpRequestProxy;

import java.util.HashMap;
import java.util.Map;

/*
* 短信通知
* */
public class SMSProxy {
    public static final String appkey = "";
    public static final String tpl_id_order_buyer = "";  //下单通知买家:模板参数为商品名称。
    public static final String tpl_id_confirm_order = "";  //确认收货通知卖家: 模板参数为商品名称。
    public static final String tpl_id_confirm_order_seller = "";  //下单通知卖家: 模板参数为商品名称。
    public static final String sdkappid = "";

    private static SMSProxy smsProxy;
    public static SMSProxy getInstance(){
        if (smsProxy==null){
            smsProxy = new SMSProxy();
        }
        return smsProxy;
    }

    public void sendSMSNotify(String mobile,String[] params,String tpl_id) {
        //String[] params = {"下单通知","商品名称"};

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("params",params);
        paramMap.put("sign","三天情侣");
        paramMap.put("tpl_id",tpl_id);
        String time = System.currentTimeMillis()/1000+"";
        paramMap.put("time",time);
        String random = (int)(Math.random()*1000)+"";
        paramMap.put("sig",getSig(mobile,random,time));

        Map<String,String> mobileParamMap = new HashMap<>();
        mobileParamMap.put("mobile",mobile);
        mobileParamMap.put("nationcode","86");
        paramMap.put("tel",mobileParamMap);

        String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid="+sdkappid+"&random="+random;

        String[] p = {GsonUtil.toJson(paramMap)};
        System.out.println("=====发送通知短信=====：" + p);
        String resultString = HttpRequestProxy.post(url,p );
        /*
        * {
            "result": 0,
            "errmsg": "OK",
            "sid": "2019:-3662193674736389554",
            "fee": 1
        }*/
        System.out.println("=====通知短信结果=====：" + resultString);
        Map<String, Object> stringObjectMap = GsonUtil.toMap(resultString);
        if (stringObjectMap!=null && (Double) stringObjectMap.get("result")==0){
            System.out.println("=====通知短信成功=====");
        }
        else {
            System.out.println("=====通知短信失败=====");
        }

    }

    private String getSig(String mobile,String random,String time)  {
        return Sha256.getSHA256("appkey="+appkey+"&random="+random+"&time="+time+
                "&mobile="+mobile);
    }
}
