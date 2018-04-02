package com.zia.test.imageloader.config.net;

import android.util.Log;

import com.zia.test.imageloader.ImageLoader;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zia on 2018/4/1.
 * 基于builder模式的配置设置
 */
public class NetOptions implements INetOptions {

    private int preloadPic;//占位图，优先加载
    private static final int NO_PRELOAD_PIC = -1;
    private long diskCacheSize;//磁盘缓存的最大容量
    private Executor treadPoolExecutor;//线程池
    private String savePath;//缓存图片本地储存的路径
    private int imageWidth, imageHeight;//宽高

    /**
     * 初始化各个属性
     */
    public NetOptions() {
        preloadPic = NO_PRELOAD_PIC;
        diskCacheSize = 1024 * 1024 * 50;//50Mb最大磁盘缓存
        treadPoolExecutor = Executors.newCachedThreadPool();//可缓存的线程池
        savePath = ImageLoader.getInstance().getContext().getExternalCacheDir().getPath();
        imageHeight = 0;
        imageWidth = 0;
    }

    /**
     * 改变宽高
     * @param imageWidth
     * @param imageHeight
     */
    public NetOptions reSize(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        return this;
    }

    /**
     * 设置自适应ImageView的大小，降低分辨率，优化性能
     */
    public NetOptions AutoSize() {
        //计算分辨率
        return this;
    }

    /**
     * 判断是否预加载占位图
     *
     * @return
     */
    @Override
    public boolean isPreloadPic() {
        return preloadPic != NO_PRELOAD_PIC;
    }

    public NetOptions setPreloadPic(int preloadPic) {
        this.preloadPic = preloadPic;
        return this;
    }

    @Override
    public long getDiskCacheSize() {
        return diskCacheSize;
    }

    public NetOptions setDiskCacheSize(long diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
        return this;
    }

    @Override
    public Executor getTreadPoolExecutor() {
        return treadPoolExecutor;
    }

    public NetOptions setTreadPoolExecutor(Executor treadPoolExecutor) {
        this.treadPoolExecutor = treadPoolExecutor;
        return this;
    }

    @Override
    public String getSavePath() {
        return savePath;
    }

    public NetOptions setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    @Override
    public int getWidth() {
        return imageWidth;
    }

    @Override
    public int getHeight() {
        return imageHeight;
    }
}
