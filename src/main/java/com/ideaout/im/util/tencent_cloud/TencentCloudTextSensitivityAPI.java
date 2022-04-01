package com.ideaout.im.util.tencent_cloud;

import com.ideaout.im.util.GsonUtil;
import com.ideaout.im.util.HttpRequestProxy;
import com.ideaout.im.util.LogUtil;

import java.util.TreeMap;

/*腾讯云文本敏感信息监测api
 * 官方地址（旧版）：https://cloud.tencent.com/document/api/271/2615
 * 官方地址（新版）：https://cloud.tencent.com/document/api/271/35501
 * 控制台：https://console.cloud.tencent.com/nlp2/senior
 * */

public class TencentCloudTextSensitivityAPI {
    private static final String apiDemoName = "nlp.tencentcloudapi.com";  //接口域名
    private static final String apiAction = "SensitiveWordsRecognition";  //action
    private static final String apiVersion = "2019-04-08"; //api版本

    private final static String CHARSET = "UTF-8";

    private static TencentCloudTextSensitivityAPI tencentCloudTextSensitivityAPI;

    public static TencentCloudTextSensitivityAPI getInstance() {
        if (tencentCloudTextSensitivityAPI == null) {
            tencentCloudTextSensitivityAPI = new TencentCloudTextSensitivityAPI();
        }
        return tencentCloudTextSensitivityAPI;
    }

    private String generateParam(String content) {
        APICommonUtil apiCommonUtil = APICommonUtil.getInstance(apiDemoName);
        String result = "";
        TreeMap<String, Object> params = apiCommonUtil.getCommonParam(apiAction, apiVersion);
        params.put("Text", content);
        try {
            params.put("Signature", apiCommonUtil.sign(params));
            result =  apiCommonUtil.getUrl(params);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public double getContentSensitiveValue(String content) {
        String url = generateParam(content);
        //LogUtil.i("发送敏感请求：" + url);
        String response = HttpRequestProxy.sendGet(url);
        LogUtil.i("腾讯云敏感文本校验结果：" + response);

        /*{
              "Response": {
                "RequestId": "8dd99adb-5144-43ca-8213-f6a929ce5075",
                "SensitiveWords": [
                  "敏感词A",
                  "敏感词B"
                ]
              }
            }*/

        APINewResult result = GsonUtil.fromJson(response, APINewResult.class);
        double sensitiveWordsSize = 0;
        if (result != null && result.Response!=null) {
            if (result.Response.SensitiveWords!=null && result.Response.SensitiveWords.length>0){
                sensitiveWordsSize = result.Response.SensitiveWords.length;
            }

        }
        return sensitiveWordsSize;
    }

    class APIResult {
        public int code;
        public String message;
        public String codeDesc;
        public double sensitive;
        public double nonsensitive;
    }


    class APINewResult {
        public Response Response;

        class Response{
            String RequestId;
            String[] SensitiveWords;
        }
    }
}