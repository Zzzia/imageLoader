package com.zia.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    recyclerAdapter adapter;
    Data data = null;
    List<String> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        getData();
        setRecyclerView();

    }

    private void bind(){
        imageView = (ImageView)findViewById(R.id.image);
        recyclerView = (RecyclerView)findViewById(R.id.recylcerview);
    }

    private void setRecyclerView(){
                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(manager);
                adapter = new recyclerAdapter(new ArrayList<String>());
                recyclerView.setAdapter(adapter);
    }

    private void getData(){
            HttpUtils.sendHttpRequest("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/0/0", new HttpUtils.CallBack() {
                @Override
                public void onFinish(final String response) {
                    Gson gson = new Gson();
                    Log.d("zzzia",response);
                    data = gson.fromJson(response, Data.class);
                    int i=0;
                    for(;i<data.getResults().length;i++){
                        urlList.add(data.getResults()[i].getUrl());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.refresh(urlList);
                        }
                    });
                }
            });
    }


    /*private void Sgson(){
        if(data != null){
            imgDataList.clear();
            int k = j+10;
            ImgData imgData = new ImgData();
            for(;j<k;j++){
                Results result = data.getResults()[j];
                imgData.setName(result.get_id());
                imgData.setUrl(result.getUrl());
                imgDataList.add(imgData);
            }
            download();
            adapter.fresh(imgDataList,lruCache);
        }
    }*/

    /*private void download(){
        ImageGeter imageGeter = new ImageGeter(imgDataList);
        imageGeter.Download(new ImageGeter.Callback() {
            @Override
            public void onFinish(LruCache<String, Bitmap> mMemoryCache) {
                lruCache = mMemoryCache;
            }

            @Override
            public void Erorr(IOException e) {
                e.printStackTrace();
            }
        });
    }*/
}
