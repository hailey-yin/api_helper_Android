package mobi.wonders.apps.android.budrest.utils;

import android.util.*;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @class DES3Utils
 * @brief 3DES加密工具类
 */
public class DES3Utils {

    // 定义加密算法3DES
    private static final String Algorithm = "DESede";
    // 加密密钥
    private static final String PASSWORD_CRYPT_KEY = "user";

    /**
     * 加密方法
     * @param str 需要加密的字符串
     * @return
     * @author zl(import)
     */
    public static String encryptMode(String str) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            // 实例化Cipher
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] encryptData = cipher.doFinal(str.getBytes("utf-8"));
            return Base64.encode(encryptData);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param str
     *            密文的字节数组
     * @return
     */
    public static String decryptMode(String str) {
        try {
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] decryptData = c1.doFinal(Base64.decode(str));
            return new String(decryptData,"utf-8");
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    /*public static void main(String[] args) {
        System.out.println("-------------------------");
        String testName = "11";
        String testPwd = "123456";
        System.out.println("原用户名是: " + testName);
        System.out.println("原密码是: " + testPwd);
        String encodeName = encryptMode(testName);
        String encodePwd = encryptMode(testPwd);
        System.out.println("加密后的用户名是: " + encodeName);
        System.out.println("加密后的密码是: " + encodePwd);
        System.out.println("解密后的用户名是: " + decryptMode(encodeName));
        System.out.println("解密后的密码是: " + decryptMode(encodePwd));
        System.out.println("-------------------------");
    }*/
}