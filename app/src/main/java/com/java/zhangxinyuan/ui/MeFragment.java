package com.java.zhangxinyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.zhangxinyuan.databinding.FragmentMeBinding;

public class MeFragment extends Fragment {

    private FragmentMeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnHistory.setSelected(true);
        binding.btnCollection.setSelected(true);

        binding.btnHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: 跳转到历史记录页面
                Intent intent=new Intent(getContext(),HistoryActivity.class);
                                startActivity(intent);
            }
        });

        binding.btnCollection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: 跳转到收藏页面
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}