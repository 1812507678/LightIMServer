package com.ideaout.im;

import com.ideaout.im.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
public class LightIMApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Resource
    private RedisUtils redisUtils;


    @Test
    public void test() {
        /*String key = "yycg12345678901234567890";
        String oldstring = "test" + "#" + "test" + "#" + System.currentTimeMillis();
        V3DESUtil tempDesEn = new V3DESUtil(1,  key,oldstring);
        String strTemp = tempDesEn.V3DESChiper("12345678");
        System.out.println("strTemp=== " + strTemp);

        V3DESUtil tempDe = new V3DESUtil(0, key,strTemp);
        String strTempDe = tempDe.V3DESChiper("12345678");
        System.out.println("strTempDe===" + strTempDe);*/

        /*String encrypt = V3DESUtil.encrypt("12345678", "123");
        System.out.println("encrypt:" + encrypt);

        String decrypt = V3DESUtil.decrypt("12345678", encrypt);
        System.out.println("decrypt:" + decrypt);*/
        //System.out.println("订单号:" + TextUtil.getOrderNum(1));


       /* String result = HttpRequestProxy.sendGet("https://webapi.tencent_sms.mob.com/tencent_sms/verify",
                "appkey=347a18b33cb5a&phone=17726936959&zone=86&code=xx");
        System.out.println(result);*/





    }


}
