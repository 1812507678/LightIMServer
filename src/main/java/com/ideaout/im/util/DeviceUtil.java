package com.ideaout.im.util;

import com.google.gson.reflect.TypeToken;
import com.ideaout.im.http.RequestParamBase;

import java.util.Map;

public class DeviceUtil {

    //从请求参数中获取设备唯一标识
    public static String getDeviceUniqueIdFromRequest(RequestParamBase requestParmBase) {
        String deviceUniqueId = "";
        Map<String, String> map = GsonUtil.fromJson(requestParmBase.deviceInfo, new TypeToken<Map<String, String>>() {
        }.getType());
        if (map != null) {
            deviceUniqueId = map.get("deviceUniqueId");
        }
        return deviceUniqueId;
    }

    //从请求参数中获取app版本号
    public static int getApkVersionCodeFromRequest(RequestParamBase requestParmBase) {
        int versionCode = -1;
        Map<String, String> map = GsonUtil.fromJson(requestParmBase.deviceInfo, new TypeToken<Map<String, String>>() {
        }.getType());
        if (map != null) {
            String versionCodeString = map.get("versionCode");
            if (!TextUtil.isEmpty(versionCodeString)) {
                versionCode = Integer.parseInt(versionCodeString);
            }
        }
        return versionCode;
    }

    //从请求参数中获取app版本名称
    public static String getApkVersionNameFromRequest(RequestParamBase requestParmBase) {
        String versionName = "";
        Map<String, String> map = GsonUtil.fromJson(requestParmBase.deviceInfo, new TypeToken<Map<String, String>>() {
        }.getType());
        if (map != null) {
            return map.get("versionName");
        }
        return versionName;
    }

    //从请求参数中获取app版本号
    public static boolean isIosSystemFromRequest(RequestParamBase requestParmBase) {
        Map<String, String> map = GsonUtil.fromJson(requestParmBase.deviceInfo, new TypeToken<Map<String, String>>() {
        }.getType());
        if (map != null) {
            String deviceBrand = map.get("deviceBrand");
            return "iOS".equals(deviceBrand);
        }
        return false;
    }

    //从请求参数中获取手机型号
    public static String getDeviceBrandFromRequest(RequestParamBase requestParmBase) {
        String versionName = "";
        Map<String, String> map = GsonUtil.fromJson(requestParmBase.deviceInfo, new TypeToken<Map<String, String>>() {
        }.getType());
        if (map != null) {
            return map.get("deviceBrand");
        }
        return versionName;
    }

}
