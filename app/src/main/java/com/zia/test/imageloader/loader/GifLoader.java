package com.zia.test.imageloader.loader;

import android.view.View;

import com.zia.test.imageloader.config.gif.IGifOptions;

/**
 * Created by zia on 2018/4/2.
 */
public class GifLoader extends ILoader {
    @Override
    public void load(Object o, View imageView) {

    }

    @Override
    protected boolean checkSource(Object o) {
        return false;
    }
}