package com.zia.test.imageloader.cache.strategy.net;

import android.content.Context;
import android.graphics.Bitmap;

import com.zia.test.imageloader.ImageLoader;
import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.util.IoHelper;
import com.zia.test.imageloader.util.MD5Helper;

/**
 * Created by zia on 2018/4/1.
 * 磁盘缓存
 */
public class DiskCache implements ICache {

    private String filePath;

    public DiskCache(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        IoHelper.saveBitmap(
                bitmap, filePath, MD5Helper.getMD5(url));
    }

    @Override
    public Bitmap get(String url) {
        return IoHelper.getBitmapFromDisk(MD5Helper.getMD5(url)
                , filePath);
    }
}
