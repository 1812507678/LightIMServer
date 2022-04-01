package com.ideaout.im.util.tencent_cloud;

import com.ideaout.im.util.GsonUtil;
import com.ideaout.im.util.HttpRequestProxy;
import com.ideaout.im.util.TextUtil;

import java.util.TreeMap;

/*
    腾讯云身份证识别及信息核验
    https://cloud.tencent.com/document/product/1007/37980
 */

public class IdCardOCRVerifyUtil {
    private static final String apiDemoName = "faceid.tencentcloudapi.com";  //接口域名
    private static final String apiAction = "IdCardOCRVerification";  //action
    private static final String apiVersion = "2018-03-01"; //api版本


    public static APIResult verify(String idCard,String name) {
        if (preVerifyInput(idCard,name)){
            String url = generateParam(idCard,name);
            String response = HttpRequestProxy.sendGet(url);
            return GsonUtil.fromJson(response, APIResult.class);
        }
        else {
            APIResult apiResult = new APIResult();
            APIResult.Response response = new APIResult.Response();
            response.Description = "姓名或身份证号长度不对，请检查后重新提交";
            apiResult.Response = response;
            return apiResult;
        }
    }

    public static APIResult verifyByLocal(String idCard,String name) {
        APIResult apiResult = new APIResult();
        APIResult.Response response = new APIResult.Response();
        if (preVerifyInput(idCard,name)){
            response.Result = "0";
        }
        else {
            response.Description = "姓名或身份证号长度不对，请检查后重新提交";
        }
        apiResult.Response = response;
        return apiResult;
    }

    public static boolean preVerifyInput(String idCard,String name) {
        return !TextUtil.isEmpty(name) && name.length() >= 2 && !TextUtil.isEmpty(idCard) && idCard.length() == 18;
    }

    private static String generateParam(String idCard,String name) {
        APICommonUtil apiCommonUtil = APICommonUtil.getInstance(apiDemoName);
        String result = "";
        TreeMap<String, Object> params = apiCommonUtil.getCommonParam(apiAction, apiVersion);
        params.put("IdCard", idCard);
        params.put("Name", name);
        try {
            params.put("Signature", apiCommonUtil.sign(params));
            result =  apiCommonUtil.getUrl(params);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    /*{
      "Response": {
        "Result": "-1",
        "Description": "姓名和身份证号不一致",
        "Name": "马海",
        "IdCard": "622428199301093412",
        "Sex": "",
        "Nation": "",
        "Birth": "",
        "Address": "",
        "RequestId": "0c4e19d0-cb36-45aa-a03b-f625a803e881"
      }
    }*/
    public static class APIResult {
        public Response Response;
        public static class Response{
            public String Result;  //-1:姓名和身份证号不一致,0:姓名和身份证号一致,-2:非法身份证号（长度、校验位等不正确）
            public String Description;
            public String IdCard;
            public String Sex;
            public String Nation;
            public String Birth;
            public String Address;
        }
    }


}
