package com.zia.test.imageloader.util;

import android.util.Log;

import com.zia.test.imageloader.ImageLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zia on 2018/4/2.
 */
public class MD5Helper {

    /**
     * MD5加密
     *
     * @param val 待加密字符串
     * @return MD5加密
     */
    public static String getMD5(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md5 != null) {
            md5.update(val.getBytes());
            byte[] m = md5.digest();//加密
            StringBuilder sb = new StringBuilder();
            for (byte aM : m) {
                sb.append(aM);
            }
            return sb.toString() + ".jpg";
        }
        Log.e(ImageLoader.TAG, "MD5加密失败");
        return null;
    }
}
