package com.zia.test.imageloader.cache.strategy.net;

import android.graphics.Bitmap;

import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.util.IoHelper;

/**
 * Created by zia on 2018/4/1.
 * 从网络加载的缓存
 */
public class NetCache implements ICache {

    /**
     * 加载源，无input
     */
    @Override
    public void put(String url, Bitmap bitmap) {
    }

    @Override
    public Bitmap get(String url) {
        return IoHelper.loadBitmapFromHttp(url);
    }
}
