package com.java.zhangxinyuan.ui;


import static com.java.zhangxinyuan.ui.TabNewsFragment.getEndDate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.zhangxinyuan.databinding.ActivitySearchedNewsBinding;
import com.java.zhangxinyuan.utils.FetchNewsAPI;
import com.java.zhangxinyuan.utils.NewsInfo;
import com.java.zhangxinyuan.utils.NewsListAdapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SearchedNewsActivity extends AppCompatActivity {

    private ActivitySearchedNewsBinding binding;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    NewsListAdapter newListAdapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchedNewsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        recyclerView = binding.newsRecyclerView;
        swipeRefreshLayout = binding.swipeRefreshLayout;
        progressBar = binding.progressBar;
        newListAdapter = new NewsListAdapter(this);
        recyclerView.setAdapter(newListAdapter);
        toolbar = binding.toolbar;

        AtomicInteger pageSize = new AtomicInteger(1);
        String size = "";
        String startDate = getIntent().getStringExtra("startTime");
        AtomicReference<String> endDate = new AtomicReference<>(getIntent().getStringExtra("endTime"));
        String words = getIntent().getStringExtra("words");
        String categories = getIntent().getStringExtra("categories");
        AtomicReference<String> page = new AtomicReference<>(getIntent().getStringExtra(String.valueOf(pageSize)));

        //获取新闻
        FetchNewsAPI FetchNewsAPI = new FetchNewsAPI(this);
        FetchNewsAPI.getHttpData(size, startDate, endDate.get(), words, categories, page.get(), new FetchNewsAPI.OnNewsFetchedListener() {
            @Override
            public void onSuccess(List<NewsInfo.DataDTO> newsList) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    newListAdapter.setListData(newsList);
                    if(newsList.isEmpty()){
                        Toast.makeText(SearchedNewsActivity.this, "没有找到相关新闻", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("-------------------", "onSuccess: " + newsList.size());
                });
            }


            @Override
            public void onFailure(Exception e) {
                Log.d("----------------------------", "onFailure: 111111111111111111111111111");
                SearchedNewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(() -> {
                            // 切换到主线程显示 Toast
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(SearchedNewsActivity.this, "搜索失败", Toast.LENGTH_SHORT).show()
                            );
                        }).start();
                    }
                });
            }
        });

        //设置下拉刷新的监听器
        com.java.zhangxinyuan.utils.FetchNewsAPI fetchNewsAPI = new FetchNewsAPI(this);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    endDate.set(getEndDate());
                    pageSize.set(1);
                    page.set(Integer.toString(pageSize.get()));
                    fetchNewsAPI.getHttpData(size, startDate, endDate.get(), words, categories, page.get(), new FetchNewsAPI.OnNewsFetchedListener() {
                        @Override
                        public void onSuccess(List<NewsInfo.DataDTO> newsList) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                newListAdapter.setListData(newsList);
                                Toast.makeText(SearchedNewsActivity.this, "新闻刷新成功", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            new Thread(() -> {
                                // 切换到主线程显示 Toast
                                new Handler(Looper.getMainLooper()).post(() ->
                                        Toast.makeText(SearchedNewsActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show()
                                );
                            }).start();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
        );

        //设置上拉加载的监听器
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    progressBar.setVisibility(View.VISIBLE);
                    page.set(Integer.toString(pageSize.get() + 1));
                    fetchNewsAPI.getHttpData(size, startDate, endDate.get(), words, categories, page.get(), new FetchNewsAPI.OnNewsFetchedListener() {
                        @Override
                        public void onSuccess(List<NewsInfo.DataDTO> newsList) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                newListAdapter.setListData(newsList);
                                pageSize.getAndIncrement();
                                progressBar.setVisibility(View.GONE);
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            new Thread(() -> {
                                // 切换到主线程显示 Toast
                                new Handler(Looper.getMainLooper()).post(() ->
                                        Toast.makeText(SearchedNewsActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show()
                                );
                            }).start();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        //设置recyclerView的点击事件
        newListAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsInfo.DataDTO dataDTO, int position) {
                Log.d("-----------------------------", "onItemClick: recyclerView clicked");
                Intent intent = new Intent(SearchedNewsActivity.this, NewsDetailsActivity.class);
                intent.putExtra("dataDTO", dataDTO);
                startActivity(intent);
            }
        });

        //返回事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 public void onClick(View view) {
                                                     finish();
                                                 }
                                             }
        );
        setContentView(root);
    }
}