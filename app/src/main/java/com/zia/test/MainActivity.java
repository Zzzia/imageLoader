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
        recyclerView = (RecyclerView)findViewById(R.id.recylcerView);
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

}
