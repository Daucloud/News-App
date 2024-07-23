package com.java.zhangxinyuan.ui;

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
import com.java.zhangxinyuan.databinding.FragmentSelectListDialogItemBinding;
import com.java.zhangxinyuan.databinding.FragmentSelectListDialogBinding;
import java.util.Arrays;
import java.util.List;

public class SelectFragment extends BottomSheetDialogFragment {

    private FragmentSelectListDialogBinding binding;

    // 定义固定分类列表
    private static final List<String> CATEGORIES = Arrays.asList(
            "娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"
    );

    public static SelectFragment newInstance() {
        return new SelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置主题样式
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置 RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ItemAdapter(CATEGORIES));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        private final List<String> categories;

        ItemAdapter(List<String> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentSelectListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String category = categories.get(position);
            holder.button.setText(category);

            // TODO: 处理按钮点击事件，例如更改颜色或选中状态
            holder.button.setOnClickListener(v -> {
                holder.button.setSelected(!holder.button.isSelected());
                Log.d("----------------------------", "onBindViewHolder: "+holder.button.isSelected());
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }
}
