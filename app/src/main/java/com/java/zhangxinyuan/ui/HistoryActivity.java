package com.java.zhangxinyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangxinyuan.utils.adapter.NewsListAdapter;
import com.java.zhangxinyuan.databinding.ActivityHistoryBinding;
import com.java.zhangxinyuan.utils.HistoryManager;
import com.java.zhangxinyuan.utils.NewsInfo;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding binding;
    RecyclerView recyclerView;
    NewsListAdapter newsListAdapter;
    List<NewsInfo.DataDTO> dataDTOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        recyclerView = binding.newsRecyclerView;
        newsListAdapter = new NewsListAdapter(this);

        //获取浏览记录的数据
        HistoryManager historyManager = new HistoryManager(this);
        dataDTOList = historyManager.getAllHistoryJson();
        newsListAdapter.setListData(dataDTOList);
        recyclerView.setAdapter(newsListAdapter);

        //设置RecyclerView的点击事件
        newsListAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(NewsInfo.DataDTO dataDTO, int position) {
                Intent intent = new Intent(HistoryActivity.this, NewsDetailsActivity.class);
                intent.putExtra("dataDTO", dataDTO);
                startActivity(intent);
            }
        });

        //返回事件
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}