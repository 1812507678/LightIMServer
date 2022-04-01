package com.ideaout.im.controller;

import com.google.gson.reflect.TypeToken;
import com.ideaout.im.http.RequestParamBase;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.service.*;
import com.ideaout.im.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class BaseController {
    @Autowired
    UserService userService;
    @Autowired
    MsgService msgService;
    @Autowired
    AdminService adminService;
    @Resource
    RedisUtils redisUtils;

    //@Resource
    //private RedisUtils redisUtils;

    private static Logger logger = Logger.getLogger(BaseController.class);
    private BaseControllerProxy baseController;

    @RequestMapping("/req")
    @CrossOrigin //使用注解方式添加跨域访问消息头
    private String post(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile[] fileList) {
        long startTime = System.currentTimeMillis();
        String requestData = HttpUtil.resolveRequestData(request);
        LogUtil.i("requestData: " + requestData);
        String sign = request.getParameter("sign"); //签名

        RequestParamBase requestParamBase = GsonUtil.fromJson(requestData, new TypeToken<RequestParamBase>() {
        }.getType());

        ResponseDataBase responseDataBase = HttpUtil.getInstance(redisUtils).getResponseDataBase(request,requestData,requestParamBase,sign);

        if (responseDataBase.code == HttpUtil.ErrorDec.RequestSuccess.value) {
            if (baseController == null) {
                baseController = new BaseControllerProxy(userService,msgService,adminService);
            }
            baseController.post(GsonUtil.toJson(requestParamBase), responseDataBase, fileList);
        }
        String result = GsonUtil.toJson(responseDataBase);
        LogUtil.i("response: " + result);
        if (responseDataBase.code==HttpUtil.ErrorDec.RepeatRequest.value){
            result = null;
        }
        long endTime = System.currentTimeMillis();
        LogUtil.i("耗时==========: " + (endTime-startTime));
        return result;
    }


    /*@RequestMapping("/weixinPayNotify")
    @CrossOrigin //使用注解方式添加跨域访问消息头
    private String post(HttpServletRequest request,HttpServletResponse response) throws Exception {
        PayCallbackProxy payCallbackProxy = new PayCallbackProxy(orderService, OrderEnum.PayWayType.WeiXin);
        return payCallbackProxy.onPayCallback(request,response);
    }

    @RequestMapping("/aLiPayNotify")
    @CrossOrigin //使用注解方式添加跨域访问消息头
    private String aLiPayNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
        PayCallbackProxy payCallbackProxy = new PayCallbackProxy(orderService,OrderEnum.PayWayType.AliPay);
        return payCallbackProxy.onPayCallback(request,response);
    }*/

    @RequestMapping("/weixinGZHNotify")
    @CrossOrigin //使用注解方式添加跨域访问消息头
    private String weixinGZHNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LogUtil.i("weixinGZHNotify============================");

        Map<String, String> param = getAllRequestParam(request);
        String queryString = request.getQueryString();
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        System.out.println("param:" + param);
        System.out.println("queryString:" + queryString);

        String result;
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (!TextUtil.isEmpty(signature) && WeixinGZHSignUtil.checkSignature(signature, timestamp, nonce)) {
            result = echostr;
            response.sendRedirect("https://www.baidu.com");
        } else {
            result = "公众号请求数据签名校验失败";
        }
        return result;
    }


    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }

}
