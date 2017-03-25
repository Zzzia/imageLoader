类的链接在这里：https://github.com/Zzzia/imageLoader/blob/master/app/src/main/java/com/zia/test/ImgUtil.java

recyclerAdapter链接：https://github.com/Zzzia/imageLoader/blob/master/app/src/main/java/com/zia/test/recyclerAdapter.java



example:

~~~
//加载网络图片(参考Glide.with(content).load(url).into(imageView))
ImgUtil.loadUrl(url,imageView,content);

//获取磁盘缓存大小
int size = ImgUtil.getDiskSize();

//清除缓存
ImgUtil.clearDisk();

//设置内存缓存大小
ImgUtil.setSize(10);

//还有md5加密解密工具
String md5 = ImgUtil.getMD5("abcdef");//加密
String str = ImgUtil.getMd5(md5);//解密
~~~



RecyclerAdapter中加载:

~~~
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        ImgUtil.loadUrl(urlList.get(position),holder.imageView,mContext);
    }
~~~

