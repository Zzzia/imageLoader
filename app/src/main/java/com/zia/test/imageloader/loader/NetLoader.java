package com.zia.test.imageloader.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zia.test.imageloader.ImageLoader;

/**
 * Created by zia on 2018/4/2.
 * 普通网络图片加载驱动
 */
public class NetLoader extends ILoader {

    @Override
    public void load(Object o, final View imageView) {
        if (!checkSource(o) || !checkEngine()) return;
        final String url = (String) o;
        getCache().setConfig(getConfig());
        getConfig().getTreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getCache().get(url);
                if (bitmap == null) {
                    Log.e(ImageLoader.TAG, "bitmap from netWork is null");
                    return;
                }
                ((Activity) ImageLoader.getInstance().getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) imageView).setImageBitmap(bitmap);
                    }
                });
            }
        });
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
