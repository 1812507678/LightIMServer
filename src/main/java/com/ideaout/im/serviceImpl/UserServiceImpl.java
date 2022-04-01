package com.ideaout.im.serviceImpl;

import com.ideaout.im.bean.TokenAttr;
import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;
import com.ideaout.im.dao.AppMapper;
import com.ideaout.im.dao.UserMapper;
import com.ideaout.im.domain.App;
import com.ideaout.im.domain.AppExample;
import com.ideaout.im.domain.User;
import com.ideaout.im.domain.UserExample;
import com.ideaout.im.enumtype.ImUserType;
import com.ideaout.im.enumtype.UserRoleType;
import com.ideaout.im.enumtype.UserState;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.http.param.InitSdkParam;
import com.ideaout.im.http.param.InitSdkResult;
import com.ideaout.im.service.UserService;
import com.ideaout.im.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AppMapper appMapper;
    @Autowired
    UserMapper userMapper;
    @Resource
    RedisUtils redisUtils;


    @Override
    public void initSdk(InitSdkParam param, ResponseDataBase responseDataBase) {
        //聊天im初始化，游客/管理员/普通用户 都调用此方法进行初始化
        if (!initVerification(param,responseDataBase)) {
            return;
        }

        User user = getImUser(param.appKey,param.outUserId,param.clientType,param.deviceUniqueId,param.imUserType);

        InitSdkResult initSdkResult = new InitSdkResult();
        //注册成功
        if (user!=null){
            initSdkResult.imUserId = user.getId();

            String token = TokenUtils.token(new TokenAttr(UserRoleType.IMUser.value,user.getId(), param.clientType, user.getType(),param.deviceUniqueId));
            initSdkResult.token = token;

            //redis存入token
            redisUtils.set( CacheUtil.getImUserTokenRedisKey(user.getId(),param.clientType),token, Config.imUserTokenExpireDay, TimeUnit.DAYS);  //7天

            //对方用户不为空时注册im
            if (!TextUtil.isEmpty(param.otherOutUserId)){
                User otherUser = getImUser(param.appKey,param.otherOutUserId,0,"",0);
                if (otherUser!=null){
                    initSdkResult.otherImUserId = otherUser.getId();
                }
            }
        }
        responseDataBase.data = initSdkResult;

    }

    private boolean initVerification(InitSdkParam param,ResponseDataBase responseDataBase){
        if (TextUtil.isEmpty(param.appKey)){
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            responseDataBase.errorDec = "初始化异常：appKey为空";
            return false;
        }
        /*else if (TextUtil.isEmpty(param.outUserId)){
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            responseDataBase.errorDec = "外部应用用户id为空";
            return;
        }*/
        else if (param.clientType<=0){
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            responseDataBase.errorDec = "初始化异常：客户端类型为空";
            return false;
        }

        //校验appKey
        AppExample appExample = new AppExample();
        AppExample.Criteria criteria = appExample.createCriteria();
        criteria.andAppKeyEqualTo(param.appKey);
        criteria.andStateEqualTo(1);
        List<App> apps = appMapper.selectByExample(appExample);
        if (CollectionUtils.isEmpty(apps)){
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            responseDataBase.errorDec = "初始化异常：appKey无效";
            return false;
        }

        return true;
    }

    private User getImUser(String appKey,String outUserId,int clientType,String deviceUniqueId,int imUserType){
        UserExample userExample = new UserExample();
        UserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andAppKeyEqualTo(appKey);
        userCriteria.andOutUserIdEqualTo(outUserId);
        List<User> users = userMapper.selectByExample(userExample);
        int updateUserResult = -1;
        User user = null;
        if (!CollectionUtils.isEmpty(users)){
            user = users.get(0);
            user.setLastTimestamp(System.currentTimeMillis());

            //在被注册的情况下，这些信息初始化的时候没有，需要补充上
            if (TextUtil.isEmpty(user.getDeviceUniqueId())){
                user.setDeviceUniqueId(deviceUniqueId);
            }
            if (user.getClientType()==null || user.getClientType()==0){
                user.setClientType(clientType);
            }
            if (user.getType()==null || user.getType()==0){
                user.setType(imUserType);
            }

            updateUserResult = userMapper.updateByPrimaryKeySelective(user);
        }
        else {
            user = new User();
            user.setAppKey(appKey);
            user.setOutUserId(outUserId);
            user.setClientType(clientType);
            user.setDeviceUniqueId(deviceUniqueId);
            user.setState(UserState.Normal.value); //状态为默认
            user.setChannel(0);
            user.setType(ImUserType.getImUserType(imUserType).value); //im类型
            user.setDatetime(new Date());
            user.setTimestamp(System.currentTimeMillis());
            user.setLastTimestamp(user.getTimestamp());
            updateUserResult = userMapper.insert(user);
        }
        return user;
    }
}
