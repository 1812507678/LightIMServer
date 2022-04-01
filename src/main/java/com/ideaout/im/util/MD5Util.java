package com.ideaout.im.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by HP on 2016/11/30.
 */
public class MD5Util {

    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }

    //这个md5方法是对的
    public static String md5(String buffer) {
        String string  = null;
        char hexDigist[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(buffer.getBytes());
            byte[] datas = md.digest(); //16个字节的长整数

            char[] str = new char[2*16];
            int k = 0;

            for(int i=0;i<16;i++)
            {
                byte b   = datas[i];
                str[k++] = hexDigist[b>>>4 & 0xf];//高4位
                str[k++] = hexDigist[b & 0xf];//低4位
            }
            string = new String(str);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return string;
    }

    public static String getFileMd5Message(String path){
        String resultPassword="";
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-256");

            File file = new File(path);
            if (file.exists()){
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[1025];
                int read= -1;
                while ((read=fileInputStream.read(bytes,0,bytes.length))!=-1){
                    md5.update(bytes,0,read);
                }
                digest = md5.digest();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (digest!=null){
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b:digest) {
                int i = b & 0xff;
                String s = Integer.toHexString(i);
                if (s.length()==1){
                    stringBuffer.append(0);
                }
                stringBuffer.append(s);
            }
            resultPassword = stringBuffer.toString();
        }
        return resultPassword;
    }
}
