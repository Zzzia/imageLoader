package com.zia.test.imageloader.config.gif;

import com.zia.test.imageloader.config.IConfig;

import java.util.concurrent.Executor;

/**
 * Created by zia on 2018/4/2.
 */
public class GifOptions implements IGifOptions {

    @Override
    public String getSavePath() {
        return null;
    }

    @Override
    public Executor getTreadPoolExecutor() {
        return null;
    }

    @Override
    public long getDiskCacheSize() {
        return 0;
    }

    @Override
    public boolean isPreloadPic() {
        return false;
    }
}
