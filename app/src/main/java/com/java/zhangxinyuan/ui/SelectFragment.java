package com.java.zhangxinyuan.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
import java.util.List;

public class SelectFragment extends BottomSheetDialogFragment {

    private static final Logger log = LoggerFactory.getLogger(SelectFragment.class);
    private FragmentSelectListDialogBinding binding;
    private FragmentHomeBinding homeBinding;

    // 定义固定分类列表
    private static final List<String> CATEGORIES = Arrays.asList(
            "娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"
    );

    // 定义分类列表
    public static List<String> selectedCategories = new ArrayList<>(CATEGORIES);
    public static List<String> unselectedCategories = new ArrayList<>();

    public static SelectFragment newInstance() {
        return new SelectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectListDialogBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        selectedCategories.clear();
        ArrayList<String> foo = bundle.getStringArrayList("categories");
        selectedCategories.addAll(foo);
        unselectedCategories.clear();
        unselectedCategories.addAll(CATEGORIES);
        unselectedCategories.removeAll(selectedCategories);

        Log.d("======================", "onCreateView: " + selectedCategories.size());

        //回退事件
        binding.toolbar.setNavigationOnClickListener(view -> dismiss());

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
        Bundle bundle1 = new Bundle();
        bundle1.putStringArrayList("categories", (ArrayList<String>) selectedCategories);
        Log.d("------------------", "onClick: " + selectedCategories.size());
        getParentFragmentManager().setFragmentResult("requestKey", bundle1);
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        ViewHolder(FragmentSelectListDialogItemBinding binding) {
            super(binding.getRoot());
            button = binding.categoryCheck;

            // 在构造函数中设置按钮的晃动动画
            ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", 0, 10, -10, 10, -10, 0);
            animator.setDuration(500);
            animator.setRepeatCount(ObjectAnimator.INFINITE);
            animator.setRepeatMode(ObjectAnimator.REVERSE);
            animator.start();
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
            // 根据 position 获取数据源中的对应分类
            String category;
            boolean isSelected;

            if (position < selectedCategories.size()) {
                category = selectedCategories.get(position);
                isSelected = true;
            } else {
                category = unselectedCategories.get(position - selectedCategories.size());
                isSelected = false;
            }

            holder.button.setText(category);
            holder.button.setSelected(isSelected);

            holder.button.setOnClickListener(v -> {
                // 创建移动动画
                ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(holder.button, "translationX", 0, 50, 0);
                moveAnimator.setDuration(300);
                moveAnimator.setInterpolator(new DecelerateInterpolator());
                moveAnimator.start();

                moveAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 更新数据源
                        if (isSelected) {
                            selectedCategories.remove(category);
                            unselectedCategories.add(category);
                        } else {
                            unselectedCategories.remove(category);
                            selectedCategories.add(category);
                        }

                        // 使用 notifyDataSetChanged() 强制刷新整个列表
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            });
        }

        @Override
        public int getItemCount() {
            return selectedCategories.size() + unselectedCategories.size();
        }
    }
}
