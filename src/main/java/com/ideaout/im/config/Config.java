package com.ideaout.im.config;

public class Config {
    public static final String SERVER_UAT_URL = "http://94.191.22.221:8080/";   //UAT服务器地址
    public static final String SERVER_PROURL = "";   //生产服务器地址

    public static final int ENVIRONMENT_LOCAL = 1;
    public static final int ENVIRONMENT_UAT = 2;
    public static final int ENVIRONMENT_PRO = 3;
    public static final int ENVIRONMENT_INDEX = ENVIRONMENT_UAT;


    public static final int compress_file_width = 512;


    //腾讯云api相关key
    public static final long TENCENT_IMAGECHECKSDK_APPID = 123;  //appid
    public static final String TENCENT_IMAGECHECKSDK_SECRETID = "";//secretId
    public static final String TENCENT_IMAGECHECKSDK_SECRETKEY = "";//secretKey

    public static final int adminTokenExpireDay = 7;  //管理员token过期时间
    public static final int imUserTokenExpireDay = 7;  //im用户token过期时间

    public static final boolean isSingleLogin = false;  //是否单点登录


    public static String getALiPayCallbackUrl(){
        String url = "";
        switch (ENVIRONMENT_INDEX){
            case ENVIRONMENT_LOCAL:
                url =  SERVER_PROURL+"WorksShopServer/aLiPayNotify";;
                break;
            case ENVIRONMENT_UAT:
                url =  SERVER_UAT_URL+"WorksShopServer/aLiPayNotify";;
                break;
            case ENVIRONMENT_PRO:
                url =  SERVER_PROURL+"WorksShopServer/aLiPayNotify";
                break;
        }
        return url;
    }

    public static String getWXPayCallbackUrl(){
        String url = "";
        switch (ENVIRONMENT_INDEX){
            case ENVIRONMENT_LOCAL:
                url =  SERVER_PROURL+"WorksShopServer/weixinPayNotify";;
                break;
            case ENVIRONMENT_UAT:
                url =  SERVER_UAT_URL+"WorksShopServer/weixinPayNotify";;
                break;
            case ENVIRONMENT_PRO:
                url =  SERVER_PROURL+"WorksShopServer/weixinPayNotify";
                break;
        }
        return url;
    }


    public static String getNginxFileDirectory(){
        String url = "";
        switch (ENVIRONMENT_INDEX){
            case ENVIRONMENT_LOCAL:
                url =  "/Users/haijun/exdata/app/im/upload/imgs/";
                break;
            case ENVIRONMENT_UAT:
                url =  "/home/exdata/app/im/upload/imgs/";
                break;
            case ENVIRONMENT_PRO:
                url =  "/home/exdata/app/im/upload/imgs/";
                break;
        }
        return url;
    }

    //支付宝提现相关证书文件目录
    public static String getALiPayWithdrawCertPath(){
        String url = "";
        switch (ENVIRONMENT_INDEX){
            case ENVIRONMENT_LOCAL:
                url =  "/Users/haijun/pay/";
                break;
            case ENVIRONMENT_UAT:
                url =  "/home/exdata/app/im/upload/pay/";
                break;
            case ENVIRONMENT_PRO:
                url =  "/home/exdata/app/im/upload/pay/";
                break;
        }
        return url;
    }




}
