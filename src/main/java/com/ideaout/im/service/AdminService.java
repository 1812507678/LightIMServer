package com.ideaout.im.service;

import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.http.param.AdminLoginParam;
import com.ideaout.im.http.param.CreateAppParam;

public interface AdminService {
    void login(AdminLoginParam param, ResponseDataBase responseDataBase);

    Object queryAppList(int userId);

    int createApp(CreateAppParam param, ResponseDataBase responseDataBase);




}
