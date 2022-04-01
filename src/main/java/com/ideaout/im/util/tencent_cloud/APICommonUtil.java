package com.ideaout.im.util.tencent_cloud;

import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.TreeMap;

public class APICommonUtil {
    private final static String CHARSET = "UTF-8";
    private String apiDemo = "";

    public static APICommonUtil getInstance(String apiDemo){
        return new APICommonUtil(apiDemo);
    }

    private APICommonUtil(String apiDemo) {
        this.apiDemo = apiDemo;
    }

    //生成公共参数
    TreeMap<String, Object> getCommonParam(String action, String version){
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        //新版使用参数
        params.put("Nonce", System.currentTimeMillis()); // 公共参数
        // 实际调用时应当使用系统当前时间，例如：
        params.put("Timestamp", System.currentTimeMillis() / 1000);
        params.put("SecretId", "AKIDSnCSdgdJskREsko7RbggrwzrjcGzHoAe"); // 公共参数
        params.put("Action", action); // 公共参数
        params.put("Region", "ap-guangzhou"); // 公共参数
        params.put("Version", version);
        params.put("SignatureMethod", "HmacSHA1");
        return params;
    }

    String getStringToSign(TreeMap<String, Object> params) {
        StringBuilder s2s = new StringBuilder("GET"+apiDemo+"/?");
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k).toString()).append("&");
        }
        return s2s.toString().substring(0, s2s.length() - 1);
    }

    //签名
    String sign(TreeMap<String, Object> params) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Config.TENCENT_IMAGECHECKSDK_SECRETKEY.getBytes(CHARSET), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(getStringToSign(params).getBytes(CHARSET));
        return DatatypeConverter.printBase64Binary(hash);
    }


    String getUrl(TreeMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder("https://"+apiDemo+"/?");
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }

}
