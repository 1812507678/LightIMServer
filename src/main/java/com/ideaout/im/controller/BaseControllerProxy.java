package com.ideaout.im.controller;

import com.google.gson.reflect.TypeToken;
import com.ideaout.im.domain.User;
import com.ideaout.im.http.RequestParamBase;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.http.RequestCode;
import com.ideaout.im.http.param.*;
import com.ideaout.im.service.*;
import com.ideaout.im.util.FileUploadUtil;
import com.ideaout.im.util.HttpUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

class BaseControllerProxy {
    private UserService userService;
    private MsgService msgService;
    private AdminService adminService;


    BaseControllerProxy(UserService userService,MsgService msgService,AdminService adminService) {
        this.userService = userService;
        this.msgService = msgService;
        this.adminService = adminService;
    }

    void post(String requestData, ResponseDataBase responseDataBase, MultipartFile[] file) {
        switch (responseDataBase.requestNo) {
            case RequestCode.CODE0001:
                handle_CODE0001(requestData, responseDataBase,file);
                break;
            case RequestCode.CODE0002:
                handle_CODE0002(requestData, responseDataBase,file);
                break;
            case RequestCode.CODE0003:
                handle_CODE0003(requestData, responseDataBase);
                break;
            case RequestCode.CODE0004:
                handle_CODE0004(requestData, responseDataBase);
                break;
            case RequestCode.CODE0005:
                handle_CODE0005(requestData, responseDataBase);
                break;
            case RequestCode.CODE0006:
                handle_CODE0006(requestData, responseDataBase);
                break;
            case RequestCode.CODE0007:
                handle_CODE0007(requestData, responseDataBase);
                break;
            case RequestCode.CODE0008:
                handle_CODE0008(requestData, responseDataBase);
                break;
            case RequestCode.CODE0009:
                handle_CODE0009(requestData, responseDataBase);
                break;
            case RequestCode.CODE0010:
                handle_CODE0010(requestData, responseDataBase);
                break;
            case RequestCode.CODE0011:
                handle_CODE0011(requestData, responseDataBase);
                break;
            default:
                responseDataBase.code = HttpUtil.ErrorDec.RequestCodeNoFound.value;
                responseDataBase.errorDec = HttpUtil.ErrorDec.RequestCodeNoFound.desc;
                break;
        }
    }

    //登录
    private void handle_CODE0001(String requestData, ResponseDataBase responseDataBase, MultipartFile[] file) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase>() {
        }.getType());
        if (requestParmBase != null) {
            /*LoginParam param = (LoginParam) requestParmBase.requestData;

            handleLoginResult(responseDataBase, user, "登录");*/

        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0002(String requestData, ResponseDataBase responseDataBase, MultipartFile[] file) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase>() {
        }.getType());
        if (requestParmBase != null) {
            if (file!=null){
                Map<String, List<String>> stringListMap = FileUploadUtil.uploadFile(file, responseDataBase, false);
                List<String> imgList = stringListMap.get("imgList");
                if (FileUploadUtil.isHasUploadFileFailure(file, imgList.size())) { // 文件上传失败
                    responseDataBase.errorDec = "文件上传失败";
                    responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
                } else {
                    responseDataBase.data = stringListMap;
                    responseDataBase.errorDec = "上传成功";
                }
            }
            else {
                responseDataBase.errorDec = "图片为空";
                responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            }
        } else {
            setRequestParmError(responseDataBase);
        }
    }


    private void handle_CODE0003(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<SendMsgParam>>() {
        }.getType());
        if (requestParmBase != null) {
            SendMsgParam param = (SendMsgParam) requestParmBase.requestData;
            msgService.sendMsg(param,responseDataBase);
        } else {
            setRequestParmError(responseDataBase);
        }
    }


    private void handle_CODE0004(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<IdParam>>() {
        }.getType());
        if (requestParmBase != null) {
            IdParam param = (IdParam) requestParmBase.requestData;
            int i = msgService.deleteMsg(param);
            HttpUtil.setResponseByCRUDReturn(responseDataBase, i);
        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0005(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<QueryMsgListParam>>() {
        }.getType());
        if (requestParmBase != null) {
            QueryMsgListParam param = (QueryMsgListParam) requestParmBase.requestData;
            param.userId = requestParmBase.tokenAttr.userId;
            responseDataBase.data = msgService.queryMsgList(param);
        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0006(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<ListParam>>() {
        }.getType());
        if (requestParmBase != null) {
            ListParam param = (ListParam) requestParmBase.requestData;
            param.userId = requestParmBase.tokenAttr.userId;
            responseDataBase.data = msgService.queryConversationList(param);
        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0007(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<IdParam>>() {
        }.getType());
        if (requestParmBase != null) {
            IdParam param = (IdParam) requestParmBase.requestData;
            int i = msgService.deleteConversation(param);
            HttpUtil.setResponseByCRUDReturn(responseDataBase, i);
        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0008(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParmBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<AdminLoginParam>>() {
        }.getType());
        if (requestParmBase != null) {
            AdminLoginParam param = (AdminLoginParam) requestParmBase.requestData;
            adminService.login(param,responseDataBase);
        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0009(String requestData,ResponseDataBase responseDataBase) {
        RequestParamBase requestParamBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase>() {
        }.getType());
        if (requestParamBase != null) {
            responseDataBase.data = adminService.queryAppList(requestParamBase.tokenAttr.userId);
        } else {
            setRequestParmError(responseDataBase);
        }
    }


    private void handle_CODE0010(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParamBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<CreateAppParam>>() {
        }.getType());
        if (requestParamBase != null) {
            CreateAppParam param = (CreateAppParam) requestParamBase.requestData;
            param.userId = requestParamBase.tokenAttr.userId;
            int i = adminService.createApp(param,responseDataBase);
            HttpUtil.setResponseByCRUDReturn(responseDataBase, i);
        } else {
            setRequestParmError(responseDataBase);
        }
    }

    private void handle_CODE0011(String requestData, ResponseDataBase responseDataBase) {
        RequestParamBase requestParamBase = HttpUtil.getRequestParm(requestData, new TypeToken<RequestParamBase<InitSdkParam>>() {
        }.getType());
        if (requestParamBase != null) {
            InitSdkParam param = (InitSdkParam) requestParamBase.requestData;
            userService.initSdk(param,responseDataBase);
        } else {
            setRequestParmError(responseDataBase);
        }
    }


    //请求参数不合法
    private void setRequestParmError(ResponseDataBase responseDataBase) {
        responseDataBase.code = HttpUtil.ErrorDec.RequestParmError.value;
        responseDataBase.errorDec = HttpUtil.ErrorDec.RequestParmError.desc;
    }

    //登录注册结果统一处理
    private void handleLoginResult(ResponseDataBase responseDataBase, Object user, String requeName) {
        if (user != null) {
            if (user instanceof User ) {
                responseDataBase.code = HttpUtil.ErrorDec.RequestSuccess.value;
                responseDataBase.data = user;
                responseDataBase.errorDec = requeName + "成功";
            } else {
                responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
                responseDataBase.errorDec = (String) user;
            }
        } else {
            responseDataBase.code = HttpUtil.ErrorDec.RequestParmError.value;
            responseDataBase.errorDec = requeName + "失败";
        }
    }



}
