package com.java.zhangxinyuan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangxinyuan.R;
import com.java.zhangxinyuan.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ViewPager2 viewPager2;
    private Button button;
    private ImageButton imageButton;
    private TabLayout tabLayout;
    private FragmentStateAdapter fragmentStateAdapter;
    private ArrayList<String> categories = new ArrayList<>(Arrays.asList("全部", "娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        button = binding.searchButton;
        imageButton = binding.imageButton;
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager2;
        fragmentStateAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabNewsFragment.newInstance(categories.get(position));
            }

            @Override
            public int getItemCount() {
                return categories.size();
            }
        };

        //设置适配器
        viewPager2.setAdapter(fragmentStateAdapter);

        //tabLayout点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //设置tabLayout和viewPager2联动
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(categories.get(position));
            }
        });
        tabLayoutMediator.attach();

        //SearchView点击事件：跳转到一个新的搜索界面
        // 在MainActivity中找到SearchView

// button点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个Intent来启动SearchResultsActivity
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                // 启动目标活动
                startActivity(intent);
            }
        });

//imageButton点击事件

        imageButton.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View v) {
                                               SelectFragment selectFragment = SelectFragment.newInstance();
                                               Bundle bundle = new Bundle();
                                               bundle.putStringArrayList("categories", new ArrayList<>(categories.subList(1, categories.size())));
                                               selectFragment.setArguments(bundle);
                                               getChildFragmentManager().beginTransaction()
                                                       .replace(R.id.select_fragment_container, selectFragment)
                                                       .addToBackStack(null)
                                                       .commit();
                                               binding.selectFragmentContainer.setBackgroundColor(Color.WHITE);
                                           }
                                       }
        );
//         监听子 Fragment 传递的数据
        getChildFragmentManager().setFragmentResultListener("requestKey", this,
                new FragmentResultListener() {
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        // 获取数据
                        categories.clear();
                        categories.add("全部");
                        categories.addAll(Objects.requireNonNull(result.getStringArrayList("categories")));
                        Log.d("------------------------------------", "onFragmentResult: " + categories.size());
                        if (categories != null) {
                            // Refresh the TabLayout
                            tabLayout.removeAllTabs(); // Remove all existing tabs
                            for (String category : categories) {
                                tabLayout.addTab(tabLayout.newTab().setText(category));
                            }
                        }
                        binding.selectFragmentContainer.setBackgroundColor(Color.TRANSPARENT);
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