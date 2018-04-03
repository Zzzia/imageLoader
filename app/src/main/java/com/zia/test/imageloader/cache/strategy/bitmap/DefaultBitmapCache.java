package com.zia.test.imageloader.cache.strategy.bitmap;

import android.graphics.Bitmap;

import com.zia.test.imageloader.cache.ICache;

/**
 * Created by zia on 2018/4/2.
 * bitmap加载策略
 */
public class DefaultBitmapCache implements ICache {
    @Override
    public void put(String url, Bitmap bitmap) {

    }

    @Override
    public Bitmap get(String url) {
        return null;
    }
}
