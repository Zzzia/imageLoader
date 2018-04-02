package com.zia.test.imageloader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import com.zia.test.imageloader.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zia on 2018/4/2.
 */
public class IoHelper {
    public static Bitmap loadBitmapFromHttp(String url) {
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
            bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bitmap;
    }

    public static void saveBitmap(Bitmap bitmap, String path, String fileName) {
        File bitmapDir = new File(path);
        if (!bitmapDir.exists()) {
            bitmapDir.mkdir();
        }
        try {
            File file = new File(path, fileName);
            if (file.exists()) {
                return;
            }
            //向磁盘保存数据
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(ImageLoader.TAG, fileName + "保存失败");
        }
    }

    public static Bitmap getBitmapFromDisk(String fileName, String path) {
        Bitmap bitmap = null;
        //文件名字
        //获取缓存文件夹
        File file = new File(path);
        //获取所有文件
        File[] files = file.listFiles();
        //遍历磁盘缓存中找到这个图片
        for (File file1 : files) {
            if (file1.getName().equals(fileName)) {
                bitmap = BitmapFactory.decodeFile(file1.getPath());
                break;
            }
        }
        return bitmap;
    }
}
