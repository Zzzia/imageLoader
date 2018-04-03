# ImageLoader

[apk](http://qiniu.zzzia.net/imageLoader.apk)(可能需要手动给文件读写权限)

<img src="https://github.com/Zzzia/imageLoader/blob/master/screenshot.png">

~~~java
//自定义配置
RequestOptions options = new RequestOptions()
            .setPreloadPic(R.mipmap.head)//正在加载图片
            .setAutoSizeByWidth(400)//自动调整分辨率
            .setErrorPic(R.mipmap.ic_launcher);//加载错误图片
//链式加载图片
ImageLoader
        .with(mContext)
        .load(url))
        .apply(options)
        .into(imageView)
        .display();
~~~

