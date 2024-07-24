package com.java.zhangxinyuan.ui;

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
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
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
    private int position;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailsBinding.inflate(getLayoutInflater());
        dataDTO = (NewsInfo.DataDTO) getIntent().getSerializableExtra("dataDTO");
        position=getIntent().getIntExtra("position",0);
        View root = binding.getRoot();
        setContentView(root);
        Intent intent=new Intent();
        intent.putExtra("position",position);
        setResult(RESULT_OK,intent);

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
                Intent intent=new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
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
                new Thread(() -> {
                    // 切换到主线程显示 Toast
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(NewsDetailsActivity.this, "摘要生成失败", Toast.LENGTH_SHORT).show()
                    );
                }).start();
                Log.e("NewsDetailsActivity", "Error fetching summary", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        executorService.shutdown();
    }

    public String getSummary(String content) {
        final String API_KEY = "4396d92d403d36d546790c1d9da8d6cc.kCxpZSgF5tnxs3Qq";
        final ClientV4 client = new ClientV4.Builder(API_KEY).build();
        List<ChatMessage> messages = List.of(new ChatMessage[]{
                new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "你的任务是为用户给定的新闻文本生成一篇总结摘要。注意，除了摘要的正文，不需要添加任何额外内容。")
                , new ChatMessage(ChatMessageRole.USER.value(),
                content)});

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
    }

}
