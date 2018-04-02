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
    public void load(Object o, final View imageView, final Object tag) {
        if (!checkSource(o) || !checkEngine()) return;
        final String url = (String) o;
        getCache().setConfig(getConfig());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {//设置透明占位图
                ((ImageView) imageView).setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8));
            }
        });
        getConfig().getTreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getCache().get(url);
                if (bitmap == null) {
                    //设置错误图片
                    return;
                }
//                Log.e("zia", imageView.getTag().toString() + tag.toString() + imageView.getTag().equals(tag) + "");
                if (imageView.getTag() == tag) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) imageView).setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
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
