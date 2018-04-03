package com.zia.test.imageloader.cache.strategy.net;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.util.MD5Helper;

/**
 * Created by zia on 2018/4/1.
 * LruCache缓存
 */
public class MemoryCache implements ICache {
    private LruCache<String, Bitmap> lruCache;

    public MemoryCache() {
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()) / 6) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        lruCache.put(MD5Helper.getMD5(url), bitmap);
    }

    @Override
    public Bitmap get(String url) {
        return lruCache.get(MD5Helper.getMD5(url));
    }
}
