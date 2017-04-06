[类的链接在这里]https://github.com/Zzzia/imageLoader/blob/master/app/src/main/java/com/zia/test/MimageLoader.java)

[recyclerAdapter链接](https://github.com/Zzzia/imageLoader/blob/master/app/src/main/java/com/zia/test/recyclerAdapter.java)

无法从lrucache里读出数据，log显示貌似是null

example:

~~~
//所有设置
MimageLoader.build(mContext).setImagePlace(R.mipmap.ic_launcher).setDiskCacheSize(100).setBitmap(urlList.get(position),imageView);

~~~



RecyclerAdapter中加载:

~~~
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        MimageLoader.build(mContext).setImagePlace(R.mipmap.ic_launcher).setDiskCacheSize(100).setBitmap(urlList.get(position),imageView);
    }
~~~

