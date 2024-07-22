package com.java.zhangxinyuan.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.zhangxinyuan.adapter.NewsListAdapter;
import com.java.zhangxinyuan.databinding.FragmentTabsNewsBinding;
import com.java.zhangxinyuan.service.FetchNewsAPI;
import com.java.zhangxinyuan.utils.NewsInfo;

import java.util.List;

public class TabNewsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private FragmentTabsNewsBinding binding;
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;


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
        newsListAdapter = new NewsListAdapter(this);
        recyclerView.setAdapter(newsListAdapter);
        String size = "",
                startDate = "",
                endDate = "",
                words = "",
                categories = requireArguments().getString(ARG_PARAM1),
                page = "";
        FetchNewsAPI fetchNewsAPI = new FetchNewsAPI();
        fetchNewsAPI.getHttpData(size, startDate, endDate, words, categories, page, new FetchNewsAPI.OnNewsFetchedListener() {
            @Override
            public void onSuccess(List<NewsInfo.DataDTO> newsList) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    newsListAdapter.setListData(newsList);
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("----------------------------", "onFailure: 111111111111111111111111111");
            }
        });
        return root;
}
}