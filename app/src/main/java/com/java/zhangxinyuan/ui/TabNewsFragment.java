package com.java.zhangxinyuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.java.zhangxinyuan.utils.NewsListAdapter;
import com.java.zhangxinyuan.databinding.FragmentTabsNewsBinding;
import com.java.zhangxinyuan.utils.FetchNewsAPI;
import com.java.zhangxinyuan.utils.NewsInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TabNewsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final int REQUEST_CODE = 100;
    private static final Logger log = LoggerFactory.getLogger(TabNewsFragment.class);
    private FragmentTabsNewsBinding binding;
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;


    public static TabNewsFragment newInstance(String param1) {
        TabNewsFragment fragment = new TabNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabsNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.newsRecyclerView;
        swipeRefreshLayout = binding.swipeRefreshLayout;
        progressBar = binding.progressBar;
        newsListAdapter = new NewsListAdapter(getContext());
        recyclerView.setAdapter(newsListAdapter);

        AtomicInteger pageSize = new AtomicInteger(1);
        String size = "";
        String startDate = "2000-01-01 00:00:00";
        AtomicReference<String> endDate = new AtomicReference<>(getEndDate());
        String words = "";
        String categories = requireArguments().getString(ARG_PARAM1);
        AtomicReference<String> page = new AtomicReference<>(Integer.toString(100));

        //获取新闻
        FetchNewsAPI fetchNewsAPI = new FetchNewsAPI(getContext());
        fetchNewsAPI.getHttpData(size, startDate, endDate.get(), words, categories, page.get(), new FetchNewsAPI.OnNewsFetchedListener() {
            @Override
            public void onSuccess(List<NewsInfo.DataDTO> newsList) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    newsListAdapter.setListData(newsList);
                });
            }

            @Override
            public void onFailure(Exception e) {
                new Thread(() -> {
                    // 切换到主线程显示 Toast
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(getActivity(), "数据获取失败", Toast.LENGTH_SHORT).show()
                    );
                }).start();
            }
        });

        //设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    endDate.set(getEndDate());
                    pageSize.set(1);
                    page.set(Integer.toString(pageSize.get()));
                    fetchNewsAPI.getHttpData(size, startDate, endDate.get(), words, categories, page.get(), new FetchNewsAPI.OnNewsFetchedListener() {
                        @Override
                        public void onSuccess(List<NewsInfo.DataDTO> newsList) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                newsListAdapter.setListData(newsList);
                                Toast.makeText(getActivity(), "新闻刷新成功", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(), "新闻获取失败，请稍后重试", Toast.LENGTH_SHORT).show();
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
                                newsListAdapter.setListData(newsList);
                                pageSize.getAndIncrement();
                                progressBar.setVisibility(View.GONE);
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "新闻获取失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        //设置recyclerView的点击事件
        newsListAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsInfo.DataDTO dataDTO, int position) {
                Log.d("-----------------------------", "onItemClick: recyclerView clicked");
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                intent.putExtra("dataDTO", dataDTO);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_CODE);
                Log.d("---------------", "onItemClick: asfjadslkfjalksdjflakdsjflkajf");
                newsListAdapter.notifyItemChanged(position);
            }
        });


        return root;
    }

    public static String getEndDate() {
        // 创建日期对象并获取当前时间
        Date currentDate = new Date();
        // 创建日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 格式化日期对象为字符串
        return dateFormat.format(currentDate);
    }

    public void updateColor(int position) {
        newsListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 更新 RecyclerView 子项
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof NewsListAdapter) {
                    Log.d("=======================", "onActivityResult: 99999999999999999999999999");
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }
}