/**
 * example in onBindViewHolder:
 *
 * MimageLoader.build(mContext).
     setMultiple(4).//设置内存缓存的大小，4代表最大内存的1/4
     setImagePlace(R.mipmap.ic_launcher).//设置未加载出来之前显示的图片
     setDiskCacheSize(100).//设置磁盘缓存大小，这里是100Mb
     setBitmap(urlList.get(position),imageView);//设置图片，url和imageview
 */

package com.zia.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by xushuzhan on 2017/3/29.
 */

public class MimageLoader {
    private static final String TAG = "MimageLoader";
    //obtainMessage的TAG
    private static final int MESSAGE_POST_RESULT = 1;
    //cpu的核心数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池的核心线程数量
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //线程池的最大线程数量
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //线程的等待时长
    private static final long KEEP_ALIVE = 10L;
    //硬盘缓存的最大值50M
    private static long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    //创建一个ThreadFactory，为线程池做准备
    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        //线程安全的加减操作
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    //创建一个线程池
    public static final Executor THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    mThreadFactory
            );

    //创建一个在主线程运行的Handler
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult rusult = (LoaderResult) msg.obj;
            ImageView imageView = rusult.imageView;
            String url = (String) imageView.getTag(imageView.getId());
            if (url.equals(rusult.url)) {
                imageView.setImageBitmap(rusult.bitmap);
            } else {
                Log.d(TAG, "url匹配失败，bitmap已经更新");
            }
        }
    };
    private static int imagePlaceID = -1;
    private Context mContext;
    //设置缓存倍数，1/MULTIPLE
    private static int MULTIPLE = 8;
    private static int cacheSize = (int) (Runtime.getRuntime().maxMemory()) / MULTIPLE;//取最大内存的1／8；
    private static LruCache<String, Bitmap> mMemoryCache =  new LruCache<String ,Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            //在每次存入缓存的时候调用
            return value.getByteCount();
        }
    };
    //硬盘缓存的地址
    private String mFilePath = null;
    private MimageLoader(Context context) {
        mContext = context;
        mFilePath = mContext.getExternalCacheDir().getPath();
    }

    /**
     * 用一个静态方法来初始化类
     *
     * @param context
     * @return
     */
    public static MimageLoader build(Context context) {
        return new MimageLoader(context);
    }

    /**
     * 将Bitmap加入LruCache
     *
     * @param url
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            mMemoryCache.put(url, bitmap);
            Log.d("zzzia","add To memory"+url);
        }
    }

    /**
     * 从LruCache中取出bitmap
     *
     * @param url
     * @return
     */
    private Bitmap getBitmapFromMemoryCache(String url) {
        if(mMemoryCache.get(url) != null){
            Log.d("zzzia","从memory中找到"+url);
        }
        else {
            Log.d("zzzia","get  == null"+url);
        }
        return mMemoryCache.get(url);
    }

    /**
     * 从网络加载图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) {
        //Log.d(TAG, "fromHttp");
        Log.d(TAG, "loadBitmap: 从internet得到了Bitmap");
        if (Looper.myLooper() == Looper.getMainLooper()) { //如果当前在主线程上
            throw new RuntimeException("不能在主线程进行网络操作");
        }
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        Bitmap bitmap = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            //bitmap = mImageResizer.decodeSampleBitmapFromInputStream(inputStream, reqWidth, reqHeight);
            bitmap = BitmapFactory.decodeStream(inputStream);
            if(bitmap != null) {
                saveBitmap(bitmap, mFilePath, url, reqWidth, reqHeight);//将bitmap保存到本地
                addBitmapToMemoryCache(url,bitmap);//保存到缓存
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        return bitmap;
    }

    /**
     * 从硬盘加载图片
     *
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromDisk(String url) {
        Bitmap bitmap = null;
        //文件名字
        String key = getMD5(url);
        //获取缓存文件夹
        File file = new File(mFilePath);
        //获取所有文件
        File[] files = file.listFiles();
        //遍历磁盘缓存中找到这个图片
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(key)) {
                bitmap = BitmapFactory.decodeFile(files[i].getPath());
                Log.d("zzzia", "从磁盘找到"+key);
                break;
            }
        }
        addBitmapToMemoryCache(url, bitmap);
        return bitmap;
    }

    /**
     * 加载bitmap(同步加载)
     *
     * @param url
     * @param reqWidth  ImageView所期望的宽度（就是ImageView的宽度）
     * @param reqHeight ImageView所期望的高度（就是ImageView的高度）
     * @return
     */
    public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        bitmap = getBitmapFromMemoryCache(url);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmap: 从缓存中找出了Bitmap");
            return bitmap;
        }
        bitmap = loadBitmapFromDisk(url);
        if (bitmap!=null){
            Log.d(TAG, "loadBitmap: 从disk中找出了Bitmap");
            return bitmap;
        }
        bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
        return bitmap;
    }

    /**
     * 给ImageView设置（异步加载）
     *
     * @param url
     * @param imageview
     */
    public void setBitmap(final String url, final ImageView imageview) {
        if(imagePlaceID != -1){
            imageview.setImageResource(imagePlaceID);
        }
        final int width = imageview.getWidth();
        final int height = imageview.getHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if(imageview.getMinimumHeight() == 0)
            imageview.setMinimumHeight(10);
        }
        imageview.setTag(imageview.getId(), url);
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, width, height);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageview, url, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 保存Bitmap到本地
     * @param bitmap
     * @param url
     */
    public void saveBitmap(Bitmap bitmap, String url) {
        saveBitmap(bitmap, null, url, 0, 0);
    }

    /**
     * 保存Bitmap到本地
     * @param bitmap
     * @param path
     * @param url
     * @param reqWidth
     * @param reqHeight
     */
    public void saveBitmap(Bitmap bitmap, String path, String url, int reqWidth, int reqHeight) {
        String fileName = getMD5(url);
        File bitmapDir = new File(path);
        if (!bitmapDir.exists()) {
            bitmapDir.mkdir();
        }
        try {
            if (getDiskSize(path) > DISK_CACHE_SIZE) {
                clearDisk(path);
            }
            File file = new File(path, fileName);
            if (file.exists()) {
                return;
            }
            //向磁盘保存数据
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //Bitmap bitmap = mImageResizer.decodeSampleBitmapFromInputStream(in, reqWidth, reqHeight);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取文件的总大小
     *
     * @return size
     */
    public long getDiskSize(String path) {
        long size = 0;
        try {
            File file = new File(path);//获取缓存文件夹
            File[] files = file.listFiles();//获取所有文件
            for (int i = 0; i < files.length; i++) {
                size = size + files[i].length();
            }
        } catch (Exception e) {

        }
        return size;
    }

    /**
     * 清除目录所有文件
     */
    public void clearDisk(String path) {
        File file = new File(path);//获取缓存文件夹
        File[] files = file.listFiles();//获取所有文件
        for (File f : files) {
            f.delete();
        }
    }


    /**
     * MD5加密
     * @param val 待加密字符串
     * @return 加密后的string
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m.length; i++) {
            sb.append(m[i]);
        }
        return sb.toString() + ".jpg";
    }

    /**
     * 更改磁盘缓存大小
     * @param size mb
     */
    public MimageLoader setDiskCacheSize(int size){
        DISK_CACHE_SIZE = 1024 * 1024 * size;
        return this;
    }

    /**
     * 设置
     * @param multiple 倍数
     * @return
     */
    public MimageLoader setMultiple(int multiple){
        MULTIPLE = multiple;
        return this;
    }

    public MimageLoader setImagePlace(int id){
        imagePlaceID = id;
        return this;
    }

    private static class LoaderResult {
        public ImageView imageView;
        public String url;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            this.imageView = imageView;
            this.url = url;
            this.bitmap = bitmap;
        }
    }

    /**
     * 图片大小适应屏幕的工具，使用后没法储存在本地，暂时不用
     */
    public class ImageResizer {
        private static final String TAG = "ImageResizer";

        /**
         * 计算采样率
         *
         * @param options
         * @param reqWidth  ImageView的宽度
         * @param reqHeight ImageView的高度
         * @return
         */
        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            if (reqHeight == 0 || reqWidth == 0) {
                return 1;
            }
            //图片的宽高
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= width) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

        /**
         * 从资源文件加载bitmap并且压缩
         *
         * @param res
         * @param resId
         * @param reqWidth
         * @param reqHeight
         * @return
         */
        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
            //首先测量图片的基本信息，以便计算采样率
            BitmapFactory.Options options = new BitmapFactory.Options();
            //仅仅测量图片，而不加载
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            //计算采样率
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            //加载图片了
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        /**
         * 从FileDescriptor加载并压缩
         *
         * @param fd
         * @param reqWidth
         * @param reqHeight
         * @return
         */
        public Bitmap decodeSampleBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
            //首先测量图片的基本信息，以便计算采样率
            BitmapFactory.Options options = new BitmapFactory.Options();
            //仅仅测量图片，而不加载
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            //计算采样率
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            //加载图片了
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        }

        /**
         * 从InputStreamr加载并压缩
         * @param in
         * @param reqWidth
         * @param reqHeight
         * @return
         */
        public Bitmap decodeSampleBitmapFromInputStream(InputStream in, int reqWidth, int reqHeight) {
            //首先测量图片的基本信息，以便计算采样率
            BitmapFactory.Options options = new BitmapFactory.Options();
            //仅仅测量图片，而不加载
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            //计算采样率
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            //加载图片了
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(in, null, options);
        }

    }
}