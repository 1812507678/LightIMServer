package com.ideaout.im.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 三重数据加密算法工具类
 * Created by charlin on 2017/9/11.
 */
public class V3DESUtil {
    // 加密/解密密钥，长度为16byte或者24byte。
    private static String keyStr = "yycg12345678901234567890";

    //numStr = 12345678
    public static String encrypt(String numStr,String message) {
        String result = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey key = new SecretKeySpec(keyStr.getBytes(), "DESede");
            result = null;
            byte[] textByte = null;
            byte[] messageByte = null;
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
            AlgorithmParameterSpec spec = new IvParameterSpec(numStr.getBytes());
            messageByte = message.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            textByte = cipher.doFinal(messageByte);
            result = encodeHex(textByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String numStr,String message) {
        String result = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey key = new SecretKeySpec(keyStr.getBytes(), "DESede");
            result = null;
            byte[] textByte = null;
            byte[] messageByte = null;
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
            AlgorithmParameterSpec spec = new IvParameterSpec(numStr.getBytes());
            messageByte = decodeHex(message);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            textByte = cipher.doFinal(messageByte);
            result = new String(textByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String encodeHex(byte bytes[]) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 16)
                buf.append("0");
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }
    private static final byte[] decodeHex(String hex) {
        char chars[] = hex.toCharArray();
        byte bytes[] = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            int newByte = 0;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte) newByte;
            byteCount++;
        }
        return bytes;
    }
    private static final byte hexCharToByte(char ch) {
        switch (ch) {
            case 48: // '0'
                return 0;
            case 49: // '1'
                return 1;
            case 50: // '2'
                return 2;
            case 51: // '3'
                return 3;
            case 52: // '4'
                return 4;
            case 53: // '5'
                return 5;
            case 54: // '6'
                return 6;
            case 55: // '7'
                return 7;
            case 56: // '8'
                return 8;
            case 57: // '9'
                return 9;
            case 97: // 'a'
                return 10;
            case 98: // 'b'
                return 11;
            case 99: // 'c'
                return 12;
            case 100: // 'd'
                return 13;
            case 101: // 'e'
                return 14;
            case 102: // 'f'
                return 15;
            case 58: // ':'
            case 59: // ';'
            case 60: // '<'
            case 61: // '='
            case 62: // '>'
            case 63: // '?'
            case 64: // '@'
            case 65: // 'A'
            case 66: // 'B'
            case 67: // 'C'
            case 68: // 'D'
            case 69: // 'E'
            case 70: // 'F'
            case 71: // 'G'
            case 72: // 'H'
            case 73: // 'I'
            case 74: // 'J'
            case 75: // 'K'
            case 76: // 'L'
            case 77: // 'M'
            case 78: // 'N'
            case 79: // 'O'
            case 80: // 'P'
            case 81: // 'Q'
            case 82: // 'R'
            case 83: // 'S'
            case 84: // 'T'
            case 85: // 'U'
            case 86: // 'V'
            case 87: // 'W'
            case 88: // 'X'
            case 89: // 'Y'
            case 90: // 'Z'
            case 91: // '['
            case 92: // '\\'
            case 93: // ']'
            case 94: // '^'
            case 95: // '_'
            case 96: // '`'
            default:
                return 0;
        }
    }

}

