package com.java.zhangxinyuan.ui;

import com.java.zhangxinyuan.databinding.ActivitySearchBinding;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View view;
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        view=binding.getRoot();
        setContentView(view);
        toolbar=binding.toolbar;

        // Hide the top ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //返回主界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}