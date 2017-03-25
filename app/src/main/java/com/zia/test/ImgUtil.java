package com.zia.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zia on 2017/3/19.
 */

public class ImgUtil {
    public interface CallBack{
        void onFinish(Bitmap bitmap);
        void Error(Exception e);
    }

    public static Bitmap bitmap = null;
    private static String md5Url = null;
    private static Context mContext = null;
    public static LruCache<String,Bitmap> lruCache = new LruCache<>((int)Runtime.getRuntime().maxMemory()/1024/1024/8);

    /**
     * 自定义缓存大小
     * @param size Mb
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setSize(int size){
        lruCache.resize(size);
    }

    /**
     * 从url中下载并加载到imageView中
     * @param url 图片链接
     * @param imageView 加载到imageView
     * @param context Context
     */
    public static void loadUrl(final String url, final ImageView imageView, final Context context) {
        synchronized (context) {
            //将url进行md5加密
            if (url != null) {
                md5Url = getMD5(url);//加密
                //获取context
                if (mContext == null) mContext = context;
                //从缓存中查找
                if (lruCache.get(md5Url) != null) {
                    imageView.setImageBitmap(lruCache.get(md5Url));
                    Log.d("img", "从缓存中找到" + md5Url);
                }
                //从磁盘中查找
                else if (fromDisk(md5Url) != null) {
                    Log.d("img", "从磁盘中找到" + md5Url);
                    imageView.setImageBitmap(fromDisk(md5Url));
                } else {//从网络中下载
                    Log.d("img", "从网络中下载");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            InputStream inputStream = null;
                            HttpURLConnection urlConnection = null;
                            try {
                                urlConnection = (HttpURLConnection) new URL(url).openConnection();
                                urlConnection.setDoInput(true);
                                urlConnection.connect();
                                inputStream = urlConnection.getInputStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                //在ui线程中设置加载bitmap到imageView
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                                //将url作为key保存到缓存
                                if (md5Url != null && bitmap != null)
                                    lruCache.put(md5Url, bitmap);
                                save(bitmap, md5Url);
                                inputStream.close();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                urlConnection.disconnect();
                            }
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * 保存到本地缓存，图片为jpg格式
     * @param bitmap 要保存的bitmap
     * @param key 保存名字的前缀
     */
    private static void save(Bitmap bitmap,String key) {
        try {
            if(getDiskSize() > 10000000){
                clearDisk();
            }
            File appDir = new File(mContext.getExternalCacheDir().getPath());
            String fileName = key + ".jpg";
            File file = new File(appDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.d("img","已保存: "+ fileName);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘中获取缓存
     * @param md5 md5加密后的字符串，不包含图片后缀
     * @return bitmap
     */
    private static Bitmap fromDisk(String md5){
        Log.d("img","磁盘大小： "+getDiskSize());
        String key = md5 + ".jpg";//加上后缀
        File file = new File(mContext.getExternalCacheDir().getPath());//获取缓存文件夹
        File[] files = file.listFiles();//获取所有文件
        for(int i=0;i<files.length;i++){
            //磁盘缓存中找到这个图片
            if(getKey(files[i].getPath()).equals(key)){
                Bitmap bitmap1 = BitmapFactory.decodeFile(files[i].getPath());
                Log.d("img","fromDisk:　" + files[i].getPath());
                return bitmap1;//获取文件路径
            }
        }
        return null;
    }

    /**
     * 将文件路径转换成只有文件名的格式
     * @param path 文件路径
     * @return 只含文件名的字符串
     */
    private static String getKey(String path){
        String FileEnd = path.substring(path.lastIndexOf("/") + 1);
        return FileEnd;
    }

    /**
     * 获取缓存文件的总大小
     * @return size
     */
    public static long getDiskSize(){
        long size = 0;
        try {
            File file = new File(mContext.getExternalCacheDir().getPath());//获取缓存文件夹
            File[] files = file.listFiles();//获取所有文件
            for(int i=0;i<files.length;i++){
                size = size + files[i].length();
            }
        }catch (Exception e){

        }
        return size;
    }

    /**
     * 清除cache目录所有文件
     */
    public static void clearDisk(){
        File file = new File(mContext.getExternalCacheDir().getPath());//获取缓存文件夹
        File[] files = file.listFiles();//获取所有文件
        for (File f : files) {
            f.delete();
        }
    }

    /**
     * md5加密
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
        for(int i = 0; i < m.length; i ++){
            sb.append(m[i]);
        }
        return sb.toString();
    }

    /**
     * md5解密
     * @param str 待解密字符串
     * @return 解密后的string
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        //16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toUpperCase();
    }
}
