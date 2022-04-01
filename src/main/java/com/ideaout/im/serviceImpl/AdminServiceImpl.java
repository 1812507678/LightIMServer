package com.ideaout.im.serviceImpl;

import com.ideaout.im.bean.TokenAttr;
import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;
import com.ideaout.im.dao.AccountMapper;
import com.ideaout.im.dao.AppMapper;
import com.ideaout.im.domain.Account;
import com.ideaout.im.domain.AccountExample;
import com.ideaout.im.domain.App;
import com.ideaout.im.domain.AppExample;
import com.ideaout.im.enumtype.UserRoleType;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.http.param.AdminLoginParam;
import com.ideaout.im.http.param.CreateAppParam;
import com.ideaout.im.service.AdminService;
import com.ideaout.im.service.StatisticsService;
import com.ideaout.im.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AccountMapper accountMapper;
    @Resource
    RedisUtils redisUtils;
    @Autowired
    AppMapper appMapper;
    @Autowired
    StatisticsService statisticsService;


    @Override
    public void login(AdminLoginParam param, ResponseDataBase responseDataBase) {
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andEmailEqualTo(param.username);
        criteria.andPasswordEqualTo(param.password);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if (!CollectionUtils.isEmpty(accounts)){
            //登录成功后返回access_token
            Account account = accounts.get(0);
            String token = TokenUtils.token(new TokenAttr(UserRoleType.Admin.value,account.getId(), param.clientType, account.getType(),null));
            account.setToken(token);  //设置token
            responseDataBase.data = account;

            //redis存入token
            redisUtils.set(CacheUtil.getAdminTokenRedisKey(account.getId(), param.clientType),token, Config.adminTokenExpireDay, TimeUnit.DAYS);  //7天
        }
        else {
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            responseDataBase.errorDec = "用户名或密码错误";
        }
    }

    @Override
    public Object queryAppList(int userId) {
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStateEqualTo(1);
        List<App> apps = appMapper.selectByExample(example);

        return statisticsService.queryAppStatisticsData(apps,userId);
    }

    @Override
    public int createApp(CreateAppParam param, ResponseDataBase responseDataBase) {
        AppExample example = new AppExample();
        example.createCriteria().andUserIdEqualTo(param.userId);
        int count = appMapper.countByExample(example);
        if (count>=10){
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            responseDataBase.errorDec = "最多新建10个应用";
            return -1;
        }

        App app = new App();
        app.setUserId(param.userId);
        app.setName(param.appName);
        app.setDatetime(new Date());
        app.setTimestamp(System.currentTimeMillis());
        app.setState(1); //0审核中，1通过
        app.setAppKey(AppUtils.getAppId());
        app.setAppSecret(AppUtils.getAppSecret(app.getAppKey()));
        return appMapper.insert(app);
    }
}
