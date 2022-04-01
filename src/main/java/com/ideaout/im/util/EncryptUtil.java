package com.ideaout.im.util;

public class EncryptUtil {

    //解密数据
    public static String decryptData(String requestOriginalData,String randomKey){
        String result;
        String aseDecrypt = AesEncodeUtil.decrypt(requestOriginalData);  //先用AES解密
        if (aseDecrypt.equals("")) { //解密失败，可能为不加密的请求方式
            result = requestOriginalData;
        }
        else {
            result = V3DESUtil.decrypt(randomKey,aseDecrypt);   // 解密出来后通过V3DES解密
        }
        return  result;
    }

}
