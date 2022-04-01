package com.ideaout.im.util.tencent_cloud;

/*
    腾讯云注册保护
    https://cloud.tencent.com/document/product/295/6592
    调试未通过
 */

import com.ideaout.im.util.HttpRequestProxy;

import java.util.TreeMap;

public class RegisterProtectionUtil {
    private static final String apiDemoName = "csec.api.qcloud.com/v2/index.php";  //接口域名
    private static final String apiAction = "RegisterProtection";  //action
    private static final String apiVersion = "2018-03-01"; //api版本


    public static void verify(String idCard, String name) {
        String url = generateParam(idCard,name);
        String response = HttpRequestProxy.sendGet(url);
        System.out.println(response);
    }

    private static String generateParam(String idCard,String name) {
        APICommonUtil apiCommonUtil = APICommonUtil.getInstance(apiDemoName);
        String result = "";
        TreeMap<String, Object> params = apiCommonUtil.getCommonParam(apiAction, apiVersion);
        params.put("accountType", "1");
        params.put("uid", "D692D87319F2098C3877C3904B304706");
        params.put("registerIp", "127.0.0.1");
        params.put("registerTime", "1553484280");
        params.put("associateAccount", "SpFsjpyvaJ27329");
        try {
            params.put("Signature", apiCommonUtil.sign(params));
            result =  apiCommonUtil.getUrl(params);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
