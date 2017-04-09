[类的链接在这里](https://github.com/Zzzia/imageLoader/blob/master/app/src/main/java/com/zia/test/MimageLoader.java)

[recyclerAdapter链接](https://github.com/Zzzia/imageLoader/blob/master/app/src/main/java/com/zia/test/recyclerAdapter.java)

没毛病的三级缓存...

example:

~~~
MimageLoader.build(mContext).
     setMultiple(4).                                //设置内存缓存的大小，4代表最大内存的1/4
     setImagePlace(R.mipmap.ic_launcher).           //设置未加载出来之前显示的图片
     setDiskCacheSize(100).                         //设置磁盘缓存大小，这里是100Mb
     setBitmap(urlList.get(position),imageView);    //设置图片，url和imageview

