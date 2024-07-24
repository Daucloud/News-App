package com.java.zhangxinyuan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.*;

public class FetchNewsAPI {
    private final String baseUrl = "https://api2.newsminer.net/svc/news/queryNewsList?";
    private Context context; // Context 用于检查网络状态

    public FetchNewsAPI(Context context) {
        this.context = context;
    }

    public void getHttpData(String size, String startDate, String endDate, String words, String categories, String page, final OnNewsFetchedListener listener) {
        if (!isNetworkAvailable()) {
            // 如果没有网络连接，直接调用 onFailure 方法
            Log.d("-------------", "No network connection.");
            listener.onFailure(new IOException("No network connection"));
            return;
        }

        // 创建 OkHttpClient 对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 构造 Request 对象
        if (categories.equals("全部")) {
            categories = "娱乐,军事,教育,文化,健康,财经,体育,汽车,科技,社会";
        }
        String url = baseUrl + "size=" + size + "&startDate=" + startDate + "&endDate=" + endDate + "&words=" + words + "&categories=" + categories + "&page=" + page;
        Log.d("-----------------------------", "getHttpData: " + url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        // 通过 OkHttpClient 和 Request 对象来构建 Call 对象
        Call call = okHttpClient.newCall(request);
        // 通过 Call 对象的 enqueue(Callback) 方法来执行异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("-------------", "onFailure: " + e.toString());
                listener.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsInfo.DataDTO> newsList = new Gson().fromJson(response.body().string(), NewsInfo.class).getData();
                    listener.onSuccess(newsList);
                } else {
                    listener.onFailure(new IOException("Unexpected response code: " + response.code()));
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public interface OnNewsFetchedListener {
        void onSuccess(List<NewsInfo.DataDTO> newsList);

        void onFailure(Exception e);
    }
}
