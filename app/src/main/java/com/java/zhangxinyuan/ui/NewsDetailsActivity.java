package com.java.zhangxinyuan.ui;

import static com.java.zhangxinyuan.utils.Assistant.getSummary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.java.zhangxinyuan.R;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.java.zhangxinyuan.databinding.ActivityNewsDetailsBinding;
import com.java.zhangxinyuan.utils.NewsInfo;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class NewsDetailsActivity extends AppCompatActivity {
    private NewsInfo.DataDTO dataDTO;
    private ActivityNewsDetailsBinding binding;
    private TextView title, source, time, summary, content;
    private ImageView image;
    private Toolbar toolbar;

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

        assert dataDTO != null;
        title.setText(dataDTO.getTitle());
        source.setText(dataDTO.getPublisher());
        time.setText(dataDTO.getPublishTime());
        content.setText(dataDTO.getContent());

        ArrayList<String> images = dataDTO.getImage();
        if (images.isEmpty()) {
            image.setVisibility(View.GONE);
        } else {
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

        // 异步获取摘要
        getSummaryInBackground(dataDTO.getContent());
    }

    private void getSummaryInBackground(String content) {
        executorService.submit(() -> {
            try {
                String summaryText = getSummary(content);
                mainHandler.post(() -> summary.setText(summaryText));
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
