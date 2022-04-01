package com.ideaout.im.util;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IDEA
 *
 * @ Author：wangm
 * @ Date：Created in  2018/12/4 10:48
 * @ Description：请求内容处理工具类
 */
public class RequestHandleUtil {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * 获取请求参数
     * @param req
     * @return 请求参数格式key-value
     */
    public static Map<String, String> getParamFromHttpServletRequest(HttpServletRequest req){
        String method = req.getMethod();
        Map<String, String> reqMap = new HashMap<String, String>();
        if(METHOD_GET.equals(method)){
            reqMap = doGet(req);
        }else if(METHOD_POST.equals(method)){
            reqMap = doPost(req);
        }else{
            return null;//其他请求方式暂不处理
        }
        return reqMap;
    }

    private static Map<String, String> doGet(HttpServletRequest req) {
        String param = req.getQueryString();
        return paramsToMap(param);
    }

    private static Map<String, String> doPost(HttpServletRequest req){
        String contentType = req.getContentType();
        try {
            if (CONTENT_TYPE_JSON.equals(contentType)) {
                StringBuffer sb = new StringBuffer();
                InputStream is = req.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String s = "";
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                String str = sb.toString();
                return paramsToMap(str);
            } else {
                //其他内容格式的请求暂不处理
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> paramsToMap(String params) {
        Map<String, String> map = new LinkedHashMap<>();
        if (!TextUtil.isEmpty(params)) {
            String[] array = params.split("&");
            for (String pair : array) {
                if ("=".equals(pair.trim())) {
                    continue;
                }
                String[] entity = pair.split("=");
                if (entity.length == 1) {
                    map.put(decode(entity[0]), null);
                } else {
                    map.put(decode(entity[0]), decode(entity[1]));
                }
            }
        }
        return map;
    }

    /**
     * 编码格式转换
     * @param content
     * @return
     */
    private static String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Map<String, String> getParameterFormUrl(String params) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            final String charset = "utf-8";
            String[] keyValues = params.split("&");
            for (int i = 0; i < keyValues.length; i++) {
                String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
