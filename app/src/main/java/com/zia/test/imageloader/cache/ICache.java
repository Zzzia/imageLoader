package com.zia.test.imageloader.cache;

import android.graphics.Bitmap;

import com.zia.test.imageloader.config.IConfig;


/**
 * Created by zia on 2018/4/1.
 * 缓存抽象类，自定义其它策略时需要继承
 */
public interface ICache {
    void put(String url, Bitmap bitmap);
    Bitmap get(String url);
}
