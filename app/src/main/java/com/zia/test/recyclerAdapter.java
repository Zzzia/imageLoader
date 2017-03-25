package com.zia.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zia on 2017/3/18.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    public interface CallBack{
        void onFinish();
    }

    private Context mContext;
    List<String> urlList = null;

    recyclerAdapter(List<String> urlList){
        this.urlList = urlList;
    }

    public void refresh(List<String> urlList){
        this.urlList.clear();
        this.urlList = urlList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        ImgUtil.loadUrl(urlList.get(position),holder.imageView,mContext);
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.img_item);
        }
    }
}
