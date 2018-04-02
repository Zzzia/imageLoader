package com.zia.test.imageloader;

import android.graphics.Bitmap;
import android.net.Uri;

import com.zia.test.imageloader.loader.BitmapLoader;
import com.zia.test.imageloader.loader.NetLoader;

/**
 * Created by zia on 2018/4/1.
 * 处理请求的类
 */
public class RequestManager {

    //请求处理者
    private RequestBuilder requestBuilder;

    RequestManager() {
        requestBuilder = new RequestBuilder();
    }

    public RequestBuilder load(String url) {
        return requestBuilder.init(url, new NetLoader());
    }

    public RequestBuilder load(Uri uri) {
        return requestBuilder.init(uri, new NetLoader());
    }

    public RequestBuilder load(Bitmap bitmap) {
        return requestBuilder.init(bitmap, new BitmapLoader());
    }

    protected void clearCache() {

    }

}
