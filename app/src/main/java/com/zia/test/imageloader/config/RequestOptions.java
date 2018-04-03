package com.zia.test.imageloader.config;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zia on 2018/4/1.
 * 基于builder模式的配置设置
 */
public class RequestOptions implements IConfig {

    private int preloadPic;//占位图，优先加载
    private int errorPic;//错误图片
    private static final int NO_PIC = -1;
    private long diskCacheSize;//磁盘缓存的最大容量
    private Executor treadPoolExecutor;//线程池
    private boolean isAutoSizeByView;//自动宽高
    private boolean isAutoSizeByHeight;
    private boolean isAutoSizeByWidth;
    private int width, height;//宽高

    /**
     * 初始化各个属性
     */
    public RequestOptions() {
        preloadPic = NO_PIC;
        errorPic = NO_PIC;
        diskCacheSize = 1024 * 1024 * 300;//300Mb最大磁盘缓存
        treadPoolExecutor = Executors.newCachedThreadPool();//可缓存的线程池
    }

    /**
     * 自定义改变宽高
     *
     * @param imageWidth
     * @param imageHeight
     */
    public RequestOptions reSize(int imageWidth, int imageHeight) {
        this.width = imageWidth;
        this.height = imageHeight;
        isAutoSizeByView = false;
        isAutoSizeByHeight = false;
        isAutoSizeByWidth = false;
        return this;
    }

    @Override
    public int getPreloadPic() {
        return preloadPic;
    }

    @Override
    public int getErrorPic() {
        return errorPic;
    }

    @Override
    public boolean isAutoSizeByView() {
        return isAutoSizeByView;
    }

    @Override
    public boolean isAutoSizeByHeight() {
        return isAutoSizeByHeight;
    }

    @Override
    public boolean isAutoSizeByWidth() {
        return isAutoSizeByWidth;
    }

    public RequestOptions setAutoSizeByView(boolean autoSize) {
        isAutoSizeByView = autoSize;
        return this;
    }

    public RequestOptions setAutoSizeByWidth(int width) {
        isAutoSizeByView = false;
        isAutoSizeByWidth = true;
        this.width = width;
        return this;
    }
    public RequestOptions setAutoSizeByHeight(int height) {
        isAutoSizeByView = false;
        isAutoSizeByHeight = true;
        this.height = height;
        return this;
    }

    public RequestOptions setErrorPic(int res) {
        this.errorPic = res;
        return this;
    }

    /**
     * 判断是否预加载占位图
     *
     * @return
     */
    @Override
    public boolean hasPreloadPic() {
        return preloadPic != NO_PIC;
    }

    public RequestOptions setPreloadPic(int res) {
        this.preloadPic = res;
        return this;
    }

    @Override
    public long getDiskCacheSize() {
        return diskCacheSize;
    }

    public RequestOptions setDiskCacheSize(long diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
        return this;
    }

    @Override
    public Executor getTreadPoolExecutor() {
        return treadPoolExecutor;
    }

    public RequestOptions setTreadPoolExecutor(Executor treadPoolExecutor) {
        this.treadPoolExecutor = treadPoolExecutor;
        return this;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
