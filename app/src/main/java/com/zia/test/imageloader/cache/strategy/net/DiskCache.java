package com.zia.test.imageloader.cache.strategy.net;

import android.graphics.Bitmap;
import android.util.Log;

import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.util.IoHelper;
import com.zia.test.imageloader.util.MD5Helper;

/**
 * Created by zia on 2018/4/1.
 * 磁盘缓存
 */
public class DiskCache extends ICache {

    @Override
    public void put(String url, Bitmap bitmap) {
        IoHelper.saveBitmap(
                bitmap, getConfig().getSavePath(), MD5Helper.getMD5(url));
        Log.e("strategy","put to disk");
    }

    @Override
    public Bitmap get(String url) {
        return IoHelper.getBitmapFromDisk(MD5Helper.getMD5(url)
                , getConfig().getSavePath());
    }
}
