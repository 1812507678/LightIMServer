package com.ideaout.im.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 敏感信息检测工具类
 * 敏感信息：微信、QQ
 *
 * */
public class SensitiveUtil {

    private static SensitiveUtil sensitiveUtil;

    public static SensitiveUtil getInstance() {
        if (sensitiveUtil == null) {
            sensitiveUtil = new SensitiveUtil();
        }
        return sensitiveUtil;
    }

    //敏感消息关键字
    private static String[] keyWords = new String[]{"聊骚","撩骚","wu","调教","调jiao", "约炮", "色情", "成人视频", "高清片源",
            "无码", "潜规则", "直播", "小黄鸭", "聊污", "聊wu", "sm","污","寂寞","满足","文爱","官方","一对一","福利","直播","pao",
            "刺激","主人","母狗","狗","奴","湿","女儿","晨勃","康康","剧情","sao","骚","互看","文的","tj","wen","片","聊w","聊S",
            "娇喘","约p","主人","hk","hukan","ghs","搞颜色","聊5","图文","文图"};





   /* public enum AccountState {
        Normal(0, "状态正常，未完成认证"),
        Unnormal(1, "状态存在异常，如有疑问请联系管理员"),
        Authorized(2, "状态已完成认证"),
        canceled(3, "已注销"),
        IdCardOCRVerified(4, "已完成身份证实名认证");

        public int value;
        public String desc;

        AccountState(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static AccountState getAccountState(int value) {
            for (AccountState type : AccountState.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return Normal;
        }
    }*/

    //封号状态：默认正常0，1封号，2临时封号
    public enum FrozenState {
        Normal(0, "默认正常0"),
        Frozen(1, "1封号"),
        TempFrozen(2, "2临时封号");

        public int value;
        public String desc;

        FrozenState(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    //是否包含QQ和微信
    /*public static boolean isContainsQQWeixin(String str) {
        //规则1：qq号微信号
        boolean containQqOrWxNumber = isContainQqOrWxNumber(str);
        //规则2：是否包含汉子一二...  ①②
        if (!containQqOrWxNumber){
            Pattern pattern = Pattern.compile("([零一二三四五六七八九贰肆陆扒玖①②③④⑤⑥⑦⑧⑨0-9]{6,16})");
            Matcher matcher = pattern.matcher(str);
            containQqOrWxNumber =  matcher.find();
        }

        return containQqOrWxNumber;
    }*/

    /**
     *
     * 过滤qq号微信号
     */
    private static boolean isContainQqOrWxNumber(String text) {
        //规则1：qq号微信号
        Pattern pattern = Pattern.compile("(微信|QQ|qq|weixin|1[0-9]{10}|[a-zA-Z0-9\\-\\_]{6,16}|[0-9]\n" +
                "{6,11})+");
        Matcher matcher = pattern.matcher(text);
        //text = text.replace(matcher.group(), "******");
        boolean containQqOrWxNumber = matcher.find();

        //规则2：是否包含汉子一二...  ①②
        if (!containQqOrWxNumber){
            Pattern patternCh = Pattern.compile("([零一二三四五六七八九壹贰肆陆扒玖①②③④⑤⑥⑦⑧⑨0-9]{6,16})");
            Matcher matcherCh = patternCh.matcher(text);
            containQqOrWxNumber =  matcherCh.find();
        }

        //规则2：将文本中所有的数字过滤出来，如果所有的数字的字符串长度超过6则认为是有QQ号
        if (!containQqOrWxNumber){
            int length = text.replaceAll("[^0-9]+", "").length();
            if (length>=6) {
                containQqOrWxNumber = true;
            }
        }

        return containQqOrWxNumber;
    }

    private static String getRegularByArray(String[] keyWords) {
        String regular = "";
        for (int i = 0; i < keyWords.length; i++) {
            if (i == 0) {
                regular += ".*";
                regular += "(" + keyWords[i] + ")";
            } else if (i == keyWords.length - 1) {
                regular += "|";
                regular += "(" + keyWords[i] + ")";
                regular += ".*";
            } else {
                regular += "|";
                regular += "(" + keyWords[i] + ")";
            }
        }
        return regular;
    }

    //是否有敏感内容,规则：内容是否包含特殊敏感词汇
    public static boolean isContainsSensitiveMsg(String str) {
        String regularString = getRegularByArray(keyWords);
        Pattern p = Pattern.compile(regularString);
        Matcher m = p.matcher(str.trim());
        return m.find();
    }

    private static String[] taskBlockWords = new String[]{"点30元支付","平台拆红包","必中800元以上","中两次就收手",
            "微信领800","拆一个50元的红包","③0元","③佰元","小学生都能赚钱", "小壆生都能挣钱","操作简单撸一千","撸一千"};
    //是否有屏蔽词
    public static boolean isContainsBlockWords(String str) {
        String regularString = getRegularByArray(taskBlockWords);
        Pattern p = Pattern.compile(regularString);
        Matcher m = p.matcher(str.trim());
        return m.find();
    }


    //内容是否合法
    public static int isMsgLegal(String str) {
        if (!TextUtil.isEmpty(str)){
            if (isContainQqOrWxNumber(str)) {
                return HttpUtil.ErrorDec.ContentContainPrivateMsg.value;
            } else if (isContainsSensitiveMsg(str)) {
                return HttpUtil.ErrorDec.ContentContainSensitiveMsg.value;
            }
        }
        return HttpUtil.ErrorDec.RequestSuccess.value;
    }

    private static String[] taskBlockUrl = new String[]{"qrqr.xingghh.top","www.jere.top","qrqr.xinhuojy.top",
            "qrqr.LanqingLin.top","dingzhi.rickycLoud.top","jdyouhuiquan","zhangxiaokai","s.artfoxlive.com"};
    public static boolean isContainsBlockUrl(String str) {
        if (str.contains("qrqr.") && str.contains(".top")){
            return true;
        }
        else if (str.contains("chaoren.") && str.contains(".top")){
            return true;
        }

        String regularString = getRegularByArray(taskBlockUrl);
        Pattern p = Pattern.compile(regularString);
        Matcher m = p.matcher(str.trim());
        return m.find();
    }

}
