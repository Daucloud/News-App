package com.java.zhangxinyuan.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.zhangxinyuan.R;
import com.java.zhangxinyuan.databinding.FragmentHomeBinding;
import com.java.zhangxinyuan.databinding.FragmentSelectListDialogItemBinding;
import com.java.zhangxinyuan.databinding.FragmentSelectListDialogBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SelectFragment extends BottomSheetDialogFragment {

    private static final Logger log = LoggerFactory.getLogger(SelectFragment.class);
    private FragmentSelectListDialogBinding binding;
    private FragmentHomeBinding homeBinding;

    // 定义固定分类列表
    private static final List<String> CATEGORIES = Arrays.asList(
            "娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"
    );

    // 定义分类列表
    public static List<String> categories=new ArrayList<>();


    public static SelectFragment newInstance() {
        return new SelectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectListDialogBinding.inflate(inflater, container, false);

        Bundle bundle=getArguments();
        assert bundle != null;
        categories.clear();
        ArrayList<String>foo=bundle.getStringArrayList("categories");
        categories.addAll(foo);
        Log.d("======================", "onCreateView: "+categories.size());

        //回退事件
        binding.toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                }
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置 RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ItemAdapter());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle1=new Bundle();
        bundle1.putStringArrayList("categories", (ArrayList<String>) categories);
        Log.d("------------------", "onClick: "+categories.size());
        getParentFragmentManager().setFragmentResult("requestKey",bundle1);
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        ViewHolder(FragmentSelectListDialogItemBinding binding) {
            super(binding.getRoot());
            button= binding.categoryCheck;
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentSelectListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String category =CATEGORIES.get(position);
            holder.button.setText(category);
            boolean ok=categories.contains(category);
            holder.button.setSelected(ok);

            holder.button.setOnClickListener(v -> {
                holder.button.setSelected(!holder.button.isSelected());

             if(!holder.button.isSelected()&&ok)   {
                    categories.remove(category);
             }
             else if(holder.button.isSelected()&&!ok){
                 categories.add(category);
             }

            });
        }

        @Override
        public int getItemCount() {
            return CATEGORIES.size();
        }
    }
}
