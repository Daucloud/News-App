package com.java.zhangxinyuan.ui;

import static com.java.zhangxinyuan.utils.Assistant.getSummary;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.java.zhangxinyuan.R;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.java.zhangxinyuan.databinding.ActivityNewsDetailsBinding;
import com.java.zhangxinyuan.utils.DBHelper;
import com.java.zhangxinyuan.utils.FavoritesManager;
import com.java.zhangxinyuan.utils.HistoryManager;
import com.java.zhangxinyuan.utils.NewsInfo;
import com.java.zhangxinyuan.utils.SummaryManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.widget.VideoView;

public class NewsDetailsActivity extends AppCompatActivity {
    private NewsInfo.DataDTO dataDTO;
    private ActivityNewsDetailsBinding binding;
    private TextView title, source, time, summary, content;
    private ImageView image;
    private Toolbar toolbar;
    private Button button;
    private VideoView videoView;
    private SummaryManager summaryManager;
    private ImageButton imageButton;
    private FavoritesManager favoritesManager=new FavoritesManager(this);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailsBinding.inflate(getLayoutInflater());
        dataDTO = (NewsInfo.DataDTO) getIntent().getSerializableExtra("dataDTO");
        View root = binding.getRoot();
        setContentView(root);

        title = binding.newsTitle;
        source = binding.newsSource;
        image = binding.newsImage;
        time = binding.newsTime;
        summary = binding.newsSummary;
        content = binding.newsContent;
        toolbar = binding.toolbar;
        button = binding.searchButton;
        videoView = binding.newsVideo;
        imageButton=binding.imageButton;

        assert dataDTO != null;
        title.setText(dataDTO.getTitle());
        source.setText(dataDTO.getPublisher());
        time.setText(dataDTO.getPublishTime());
        content.setText(dataDTO.getContent());

        ArrayList<String> images = dataDTO.getImage();
        String videos = dataDTO.getVideo();
        if (videos != null&&videos!= "") {
            videoView.setVideoPath(videos);
            videoView.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
        } else if (!images.isEmpty()) {
            image.setVisibility(View.VISIBLE);
            Glide.with(this).load(images.get(0)).error(R.drawable.baseline_image_not_supported_24).into(image);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets inset = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.newsContent.setPadding(0, inset.top, 0, inset.bottom);
            return insets;
        });

        // 获取摘要
        summaryManager=new SummaryManager(this);
        if(summaryManager.isSummaryExists(dataDTO.getNewsID())){
            summary.setText(summaryManager.getSummary(dataDTO.getNewsID()));
        }else{
            getSummaryInBackground(dataDTO.getContent());
        }

        //处理button点击
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个Intent来启动SearchResultsActivity
                Intent intent = new Intent(NewsDetailsActivity.this, SearchActivity.class);
                // 启动目标活动
                startActivity(intent);
            }
        });

        //处理imageButton的点击状态
        boolean ok=favoritesManager.isFavoriteExists(dataDTO.getNewsID());
        imageButton.setSelected(ok);

        //处理imageButton点击
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageButton.isSelected()) {
                    imageButton.setSelected(false);
                    favoritesManager.deleteFavorite(dataDTO.getNewsID());
                } else {
                    imageButton.setSelected(true);
                    favoritesManager.insertFavorite(dataDTO.getNewsID(),new Gson().toJson(dataDTO));
                }
            }
        });

        //在数据库中添加一条元素
        Gson gson = new Gson();
        HistoryManager historyManager = new HistoryManager(this);
        historyManager.insertHistory(dataDTO.getNewsID(), gson.toJson(dataDTO));
    }

    private void getSummaryInBackground(String content) {
        executorService.submit(() -> {
            try {
                String summaryText = getSummary(content);
                mainHandler.post(() -> summary.setText(summaryText));
                summaryManager.insertSummary(dataDTO.getNewsID(), summaryText);
            } catch (Exception e) {
                Log.e("NewsDetailsActivity", "Error fetching summary", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
