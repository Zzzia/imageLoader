package com.zia.test.imageloader.config;

import java.util.concurrent.Executor;

/**
 * Created by zia on 2018/4/1.
 * 配置接口
 */
public interface IConfig {
    Executor getTreadPoolExecutor();
    long getDiskCacheSize();
    boolean hasPreloadPic();
    int getWidth();
    int getHeight();
    boolean isAutoSizeByView();
    boolean isAutoSizeByHeight();
    boolean isAutoSizeByWidth();
    int getErrorPic();
    int getPreloadPic();
}
