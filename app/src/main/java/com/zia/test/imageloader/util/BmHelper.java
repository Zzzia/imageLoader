package com.zia.test.imageloader.util;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by zia on 2018/4/3.
 * bitmap工具类
 */
public class BmHelper {

    public static Bitmap getEmptyBitmap() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
    }

    public static Bitmap resize(Bitmap bitmap, int width, int height) {
        Log.e("bmhelper", width + "  " + height);
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap autoResizeByWidth(Bitmap bitmap, int width) {
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int height = (int) (h / w * width);
        return resize(bitmap, width, height);
    }

    public static Bitmap autoResizeByHeight(Bitmap bitmap, int height) {
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int width = (int) (w / h * height);
        return resize(bitmap, width, height);
    }

}
