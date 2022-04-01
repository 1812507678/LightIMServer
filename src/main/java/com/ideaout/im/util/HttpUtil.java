package com.ideaout.im.util;


import com.auth0.jwt.interfaces.Claim;
import com.ideaout.im.bean.TokenAttr;
import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;
import com.ideaout.im.enumtype.UserRoleType;
import com.ideaout.im.http.RequestParamBase;
import com.ideaout.im.http.ResponseDataBase;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private static Logger logger = Logger.getLogger(FileUploadUtil.class);

    private static RedisUtils redisUtils;

    private static HttpUtil httpUtil;

    public static  HttpUtil getInstance(RedisUtils redisUtils){
        if (httpUtil==null){
            httpUtil = new HttpUtil();
            HttpUtil.redisUtils = redisUtils;
        }
        return httpUtil;
    }


    public enum ErrorDec {
        RequestSuccess(200, "请求成功"),
        RequestWayNotSupport(-1, "请求方式或参数格式不对"),
        RequestCodeNoFound(-2, "请求码不正确"),
        RequestParmError(-3, "请求参数不正确"),
        RequestDataBaseError(-4, "数据库操作异常"),  //数据库操作异常
        RequestError(-5, "请求失败"),
        ContentContainSensitiveMsg(-6, "内容包含敏感信息，多次输入可能会封号处理"),
        ContentContainPrivateMsg(-7, "内容包含敏感信息，避免输入违规词汇"),
        RequestContentTooLongError(-8, "输入文本内容过长"),
        AccountFrozenError(-9, "账号已被封，如有疑问请联系管理员"),
        FunctionNotOpenError(-10, "该功能未开放，还不能发布动态"),
        ImageContainSensitiveMsg(-11, "图片包含敏感信息"),
        TokenExpiredError(-12, "用户信息已过期，需重新登录"),
        SignError(-13, "sign签名错误或为空"),
        TimestampExpiredError(-14, "请求时间戳过期"),
        DeviceFrozenError(-15, "注册失败，因之前账号违规操作，已经被平台拉黑，如有疑问请联系管理员"),
        RepeatRequest(-16, "重复提交");

        public int value;
        public String desc;

        ErrorDec(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    public static RequestParamBase getRequestParm(String request, Type typeOfT) {
        if (!TextUtil.isEmpty(request)) {
            return GsonUtil.fromJson(request, typeOfT);
        }
        return null;
    }

    //解析请求数据，根据是否是json格式，做不同处理
    public static String resolveRequestData(HttpServletRequest request) {
        String requestData = request.getParameter("requestData");
        String encryptWay = request.getParameter("encryptWay"); //加密方式
        String sign = request.getParameter("sign"); //签名

        if (TextUtil.isEmpty(requestData)){
            requestData = HttpUtil.getRequestGsonData(request);
            /*Map<String, Object> stringObjectMap = GsonUtil.toMap(requestData);
            if (!CollectionUtils.isEmpty(stringObjectMap)){
                requestData = GsonUtil.toJson(stringObjectMap.get("requestData"));
                encryptWay = (String) stringObjectMap.get("encryptWay");
                sign = (String) stringObjectMap.get("sign");
            }*/
        }

        //过滤掉字符串开头和结尾多余的引号，在有的请求中会有多余的出现
        if (requestData.startsWith("\"")) {
            requestData = requestData.substring(1, requestData.length() - 1);
        }
        if (!TextUtil.isEmpty(encryptWay) && encryptWay.startsWith("\"")) {
            encryptWay = encryptWay.substring(1, encryptWay.length() - 1);
        }
        if (!TextUtil.isEmpty(sign) && sign.startsWith("\"")) {
            sign = sign.substring(1, sign.length() - 1);
        }

        String decryptData = "";
        if ("gen_01".equals(encryptWay)){
            //返回解密数据
            String randomKey =sign.substring(0, 8);
            decryptData =  EncryptUtil.decryptData(requestData,randomKey);
        }
        else if ("gen_H5_01".equals(encryptWay)){
            decryptData = AesEncodeUtil.decrypt(requestData);  //用AES解密
        }
        else if ("gen_H5_02".equals(encryptWay)){
            decryptData = requestData;
        }
        else {
            decryptData = requestData;
        }


        return decryptData;
    }

    /*public static RequestParamBase getRequestParm(String requestData,HttpServletRequest request, Type typeOfT) {
        String sign = request.getParameter("sign"); //签名
        if (!TextUtil.isEmpty(requestData)) {
            RequestParamBase requestParmBase = GsonUtil.fromJson(requestData, typeOfT);
            requestParmBase.sign = sign;
            return requestParmBase;
        }
        return null;
    }*/

    public ResponseDataBase getResponseDataBase(HttpServletRequest request, String requestData,RequestParamBase requestParmBase, String sign) {
        ResponseDataBase responseDataBase = new ResponseDataBase();
        if (requestParmBase == null) {
            responseDataBase.code = ErrorDec.RequestWayNotSupport.value;
            responseDataBase.errorDec = ErrorDec.RequestWayNotSupport.desc;
        } else {
            responseDataBase.requestNo = requestParmBase.requestNo;
            //responseDataBase.clientIp = HttpUtil.getRemoteIP(request); //客户端请求IP地址
            responseDataBase.clientIp = "127.0.01"; //客户端请求IP地址

            //做签名sign校验
            if (!isSignValid(sign,requestData)){
                responseDataBase.code = ErrorDec.SignError.value;
                responseDataBase.errorDec = ErrorDec.SignError.desc;
            }
            //做时间戳是否过期校验
            else if (isRequestTimestampExpired(requestParmBase)){
                responseDataBase.code = ErrorDec.TimestampExpiredError.value;
                responseDataBase.errorDec = ErrorDec.TimestampExpiredError.desc;
            }
            //token校验
            else if (!isTokenValid(requestParmBase)){
                responseDataBase.code = ErrorDec.TokenExpiredError.value;
                responseDataBase.errorDec = ErrorDec.TokenExpiredError.desc;
            }
            else if (!requestParmBase.isNotRepeatCheck && checkIsRepeat(requestParmBase)){
                LogUtil.i("重复请求");
                responseDataBase.code = ErrorDec.RepeatRequest.value;
                responseDataBase.errorDec = ErrorDec.RepeatRequest.desc;
            }
            else {
                responseDataBase.code = ErrorDec.RequestSuccess.value;
                responseDataBase.errorDec = ErrorDec.RequestSuccess.desc;
            }
        }
        return responseDataBase;
    }


    //感觉请求数据判断请求是否合法
 /*   public static ResponseDataBase getResponseDataBase(RequestParamBase requestParmBase) {
        ResponseDataBase responseDataBase = new ResponseDataBase();
        if (requestParmBase == null) {
            responseDataBase.code = ErrorDec.RequestWayNotSupport.value;
            responseDataBase.errorDec = ErrorDec.RequestWayNotSupport.desc;
        } else {
            //做签名sign校验
            if (!isSignValid(requestParmBase)){
                responseDataBase.code = ErrorDec.SignError.value;
                responseDataBase.errorDec = ErrorDec.SignError.desc;
            }
            //做时间戳是否过期校验
            else if (isRequestTimestampExpired(requestParmBase)){
                responseDataBase.code = ErrorDec.TimestampExpiredError.value;
                responseDataBase.errorDec = ErrorDec.TimestampExpiredError.desc;
            }
            //token校验
            else if (!isTokenValid(requestParmBase)){
                responseDataBase.code = ErrorDec.TokenExpiredError.value;
                responseDataBase.errorDec = ErrorDec.TokenExpiredError.desc;
            }
            else {
                responseDataBase.code = ErrorDec.RequestSuccess.value;
                responseDataBase.errorDec = ErrorDec.RequestSuccess.desc;
            }
        }
        return responseDataBase;
    }*/

   /* private static String getHttpDataSign(RequestParamBase request){
        String data = request.requestNo+ request.requestTimestamp+request.token+request.deviceInfo;
        if (request.userId >0){
            data+=request.userId;
        }
        return MD5Util.md5(data);
    }*/

    //获取HTTP请求的json数据
    private static String getRequestGsonData(HttpServletRequest request) {
        BufferedReader br;
        StringBuilder sb = null;
        String reqBody = "";
        try {
            br = new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            String line = null;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            reqBody = URLDecoder.decode(sb.toString(), "UTF-8");
            reqBody = sb.toString();
            if (reqBody.length() > 0 && reqBody.startsWith("{")) {
                reqBody = reqBody.substring(reqBody.indexOf("{"));
                request.setAttribute("inputParam", reqBody);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reqBody;
    }





    //根据数据库操作的返回结果设置返回结果：insert>0表示成功
    public static void setResponseByCRUDReturn(ResponseDataBase responseDataBase, int insert) {
        if (!TextUtil.isEmpty(responseDataBase.errorDec)){
            if (insert > 0) {
                responseDataBase.code = HttpUtil.ErrorDec.RequestSuccess.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.RequestSuccess.desc;
            } else if (insert == ErrorDec.ContentContainSensitiveMsg.value) {
                responseDataBase.code = HttpUtil.ErrorDec.ContentContainSensitiveMsg.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.ContentContainSensitiveMsg.desc;
            } else if (insert == ErrorDec.ContentContainPrivateMsg.value) {
                responseDataBase.code = HttpUtil.ErrorDec.ContentContainPrivateMsg.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.ContentContainPrivateMsg.desc;
            }  else if (insert == ErrorDec.RequestContentTooLongError.value) {
                responseDataBase.code = HttpUtil.ErrorDec.RequestContentTooLongError.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.RequestContentTooLongError.desc;
            } else if (insert == ErrorDec.AccountFrozenError.value) {
                responseDataBase.code = HttpUtil.ErrorDec.AccountFrozenError.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.AccountFrozenError.desc;
            } else if (insert == ErrorDec.FunctionNotOpenError.value) {
                responseDataBase.code = HttpUtil.ErrorDec.FunctionNotOpenError.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.FunctionNotOpenError.desc;
            } else {
                responseDataBase.code = HttpUtil.ErrorDec.RequestDataBaseError.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.RequestDataBaseError.desc;
            }
        }
    }

    //做签名sign校验
    private static boolean isSignValid(String sign,String requestData){
        if(!TextUtil.isEmpty(sign)){
            sign = sign.replaceAll("\"",""); //签名
            String requestSign = MD5Util.md5(requestData);
            return sign.equals(requestSign);
        }
        return true;
    }


    //验证请求的时间戳是否过期：和服务器时间相差3分钟秒则认为过期
    private static boolean isRequestTimestampExpired(RequestParamBase requestParmBase){
        //logger.debug("time :"+(System.currentTimeMillis() - requestParmBase.requestTimestamp));
        return System.currentTimeMillis() - requestParmBase.requestTimestamp > 1000 * 60*3;
    }

    //验证token是否有效
    private static boolean isTokenValid(RequestParamBase requestParamBase){
        //不需要登录的接口，直接返回true
        if (isNotNeedLoginRequest(requestParamBase.requestNo)){
            return true;
        }

        //1.先通过JWT校验token
        Map<String, Claim> stringClaimMap = TokenUtils.verifyWithReturn(requestParamBase.token);
        if (!CollectionUtils.isEmpty(stringClaimMap)) {
            Integer userRole = stringClaimMap.get("userRole").asInt();
            Integer userId = stringClaimMap.get("userId").asInt();
            Integer appType = stringClaimMap.get("clientType").asInt();
            Integer userType = null;
            if (stringClaimMap.get("userType")!=null){
                userType = stringClaimMap.get("userType").asInt();
            }
            //String deviceUniqueId = stringClaimMap.get("deviceUniqueId").asString();

            requestParamBase.tokenAttr = new TokenAttr(userId, appType, userType);

            if (Config.isSingleLogin) {
                //2.校验Redis当前用户的是否过期（单点登录时需要）
                String redisTokenKey = null;
                UserRoleType userRoleType = UserRoleType.getUserRoleType(userRole);
                switch (userRoleType) {
                    case Visitor:
                        //游客模式，暂不支持token
                        break;
                    case Admin:
                        redisTokenKey = CacheUtil.getAdminTokenRedisKey(userId, appType);
                        break;
                    case IMUser:
                        redisTokenKey = CacheUtil.getImUserTokenRedisKey(userId, appType);
                        break;
                }
                String redisToken = redisUtils.get(redisTokenKey);
                return !TextUtil.isEmpty(redisToken) && redisToken.equals(requestParamBase.token);
            }
            else {
                return true;
            }
        }

        return false;
    }

    //是否重复请求
    private static boolean checkIsRepeat(RequestParamBase param){
        String requestNo = param.requestNo;
        int userId = 0;
        if (param.tokenAttr!=null){
            userId = param.tokenAttr.userId;
        }
        long requestTimestamp = param.requestTimestamp;
        return isRepeatHttpPost(requestNo,userId,requestTimestamp) || isRepeatRequest(requestNo,userId,requestTimestamp);
    }


    //是否http重复请求
    private static boolean isRepeatHttpPost(String requestNo, int userId, long requestTimestamp){
        if (userId>0){
            List<String> notPreventRepeatRequestNoList = Arrays.asList(notPreventRepeatRequestNo);
            if (!notPreventRepeatRequestNoList.contains(requestNo)){
                String key = requestNo + userId;   //key
                String value = redisUtils.get(key);  //value
                redisUtils.set(key,requestTimestamp+"",1);  //2秒
                return !TextUtil.isEmpty(value);
            }
        }
        return false;
    }

    //
    private static boolean isRepeatRequest(String requestNo, int userId, long requestTimestamp){
        if (userId>0){
            List<String> notPreventRepeatRequestNoList = Arrays.asList(notPreventRepeatRequestNo);
            if (!notPreventRepeatRequestNoList.contains(requestNo)){
                String key = requestNo + userId + requestTimestamp;   //key
                String value = redisUtils.get(key);  //value
                redisUtils.set(key,requestTimestamp+"",1, TimeUnit.DAYS);  //1天
                return !TextUtil.isEmpty(value);
            }
        }
        return false;
    }

    private static boolean isNotNeedLoginRequest(String requestNo){
        return Arrays.asList(notNeedLoginRequestNo).contains(requestNo);
    }

    //不用放重复的接口
    private static String[] notPreventRepeatRequestNo = {
        "CODE0022",
        "CODE0015",
    };

    //不需要登录的接口
    private static String[] notNeedLoginRequestNo = {
        "CODE0008",
        "CODE0011",
    };


    public static String getRemoteIP(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


}
