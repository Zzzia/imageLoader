package com.zia.test.imageloader.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zia.test.imageloader.ImageLoader;
import com.zia.test.imageloader.util.BmHelper;
import com.zia.test.imageloader.util.IoHelper;

/**
 * Created by zia on 2018/4/2.
 * 普通网络图片加载驱动
 */
public class NetLoader extends ILoader {

    @Override
    public void load(Object o, final View imageView, final Object tag) {
        if (!checkSource(o) || !checkEngine()) return;
        //开始加载图片
        final String url = (String) o;
        //设置占位图
        if (getConfig().hasPreloadPic()) {
            ((ImageView) imageView)
                    .setImageResource(getConfig().getPreloadPic());
        } else {
            ((ImageView) imageView)//设置透明占位图
                    .setImageBitmap(BmHelper.getEmptyBitmap());
        }
        //从三级缓存中加载图片
        getConfig().getTreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //如果超过指定disk缓存，清理
                IoHelper.cleanDiskCache(ImageLoader.getInstance().getContext()
                                .getExternalCacheDir().getPath()
                        , getConfig().getDiskCacheSize());
                //获得图片
                final Bitmap bitmap = getCache().get(url);
                if (bitmap == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) imageView)
                                    .setImageResource(getConfig().getErrorPic());
                        }
                    });
                    return;
                }
                if (imageView.getTag() == tag) {
                    final Bitmap fixedBitmap = resize(bitmap, imageView);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) imageView).setImageBitmap(fixedBitmap);
                        }
                    });
                }
            }
        });
    }

    private Bitmap resize(Bitmap bitmap, View imageView) {
        if (getConfig().getHeight() == 0 && getConfig().getWidth() == 0)
            return bitmap;
        if (getConfig().isAutoSizeByView()) {
            return BmHelper.autoResizeByWidth(bitmap, imageView.getWidth());
        } else if (getConfig().isAutoSizeByWidth()) {
            return BmHelper.autoResizeByWidth(bitmap, getConfig().getWidth());
        } else if (getConfig().isAutoSizeByHeight()) {
            return BmHelper.autoResizeByHeight(bitmap, getConfig().getHeight());
        } else {
            return BmHelper.resize(bitmap, getConfig().getWidth(), getConfig().getHeight());
        }
    }

    private void runOnUiThread(Runnable runnable) {
        ((Activity) ImageLoader.getInstance().getContext()).runOnUiThread(runnable);
    }

    @Override
    protected boolean checkSource(Object o) {
        if (!(o instanceof String)) {
            Log.e("NetLoader", "图片源格式错误...");
            return false;
        }
        return true;
    }
}
