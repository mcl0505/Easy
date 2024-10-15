package com.mh55.httplibrary.encrypt;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private static final String SHA1PRNG = "SHA1PRNG";

    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    //AES 加密
    private static final String AES = "AES";

    /**
     * 加密
     * @param AES_KEY 加密密钥
     * @param AES_IV 加密向量
     * @param sSrc 加密内容
     * @return
     */
    public static String encrypt(String AES_KEY,String AES_IV,String sSrc) {
        try {

            byte[] raw = AES_KEY.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。

            return Base64Utils.encode(encrypted);
           //return  Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 解密
     * @param AES_KEY 加密密钥
     * @param AES_IV 加密向量
     * @param sSrc 加密内容
     * @return
     */
    public static String decrypt(String AES_KEY,String AES_IV,String sSrc) throws Exception {
        try {
            byte[] raw = AES_KEY.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = Base64.decode(sSrc.getBytes(), Base64.DEFAULT);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

}

