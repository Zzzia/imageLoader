package com.zia.test.imageloader.config;

import java.util.concurrent.Executor;

/**
 * Created by zia on 2018/4/1.
 * 配置接口
 */
public interface IConfig {
    String getSavePath();
    Executor getTreadPoolExecutor();
    long getDiskCacheSize();
    boolean isPreloadPic();
}
