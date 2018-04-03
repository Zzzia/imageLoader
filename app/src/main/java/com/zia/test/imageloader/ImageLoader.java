package com.zia.test.imageloader;

import android.content.Context;

/**
 * Created by zia on 2018/4/1.
 * 图片加载库
 */
public class ImageLoader {

    public static final String TAG = ImageLoader.class.getSimpleName();
    private volatile static ImageLoader instance;
    private RequestManager requestManager;
    private Context context;

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    private ImageLoader() {
        requestManager = new RequestManager();
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public static RequestManager with(Context context) {
        getInstance().setContext(context);
        return getInstance().requestManager;
    }

    public static void clearCache() {
        getInstance().context = null;
        getInstance().requestManager = null;
    }

    public Context getContext() {
        return context;
    }
}
