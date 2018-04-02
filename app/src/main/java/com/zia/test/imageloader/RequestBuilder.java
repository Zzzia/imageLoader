package com.zia.test.imageloader;

import android.widget.ImageView;

import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.cache.strategy.net.DefaultNetCache;
import com.zia.test.imageloader.config.IConfig;
import com.zia.test.imageloader.config.net.NetOptions;
import com.zia.test.imageloader.loader.ILoader;

/**
 * Created by zia on 2018/4/2.
 * 自定义设置类
 */
public class RequestBuilder {

    private Object source;
    private ICache cache;
    private IConfig config;
    private ILoader loader;
    private ImageView imageView;

    /**
     * 初始化
     * @param source 图片来源
     * @param loader 驱动
     */
    protected RequestBuilder init(Object source, ILoader loader) {
        this.source = source;
        this.loader = loader;
        this.cache = DefaultNetCache.getInstance();
        this.config = new NetOptions();
        return this;
    }

    /**
     * 设置参数
     * @param config 参数设置抽象类
     * @return
     */
    public RequestBuilder apply(IConfig config) {
        this.config = config;
        return this;
    }

    public RequestBuilder cacheStrategy(ICache cache) {
        this.cache = cache;
        return this;
    }

    public RequestBuilder into(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public void display() {
        loader.setCache(cache);
        loader.setConfig(config);
        loader.load(source, imageView);
    }
}
