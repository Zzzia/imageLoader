package com.zia.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zia.test.imageloader.ImageLoader;
import com.zia.test.imageloader.config.RequestOptions;

import java.util.List;

/**
 * Created by zia on 2017/3/18.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {


    private Context mContext;
    private List<String> urlList;
    private int width = 0;
    private int height = 0;

    recyclerAdapter(List<String> urlList) {
        this.urlList = urlList;
    }

    public void refresh(List<String> urlList) {
        this.urlList.clear();
        this.urlList = urlList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    private final RequestOptions options = new RequestOptions()
            .setPreloadPic(R.mipmap.head)
            .setAutoSizeByWidth(400)
            .setErrorPic(R.mipmap.ic_launcher);

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ImageView imageView = holder.imageView;
        ImageLoader
                .with(mContext)
                .load(urlList.get(holder.getAdapterPosition()))
                .apply(options)
                .into(imageView)
                .display();
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item);
        }
    }
}
