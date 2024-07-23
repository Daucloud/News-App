package com.java.zhangxinyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangxinyuan.R;
import com.java.zhangxinyuan.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ViewPager2 viewPager2;
    private Button button;
    private ImageButton imageButton;
    private TabLayout tabLayout;
    private final String[] categories = {"全部", "娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        button = binding.searchButton;
        imageButton = binding.imageButton;
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager2;

        //设置适配器
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabNewsFragment.newInstance(categories[position]);
            }

            @Override
            public int getItemCount() {
                return categories.length;
            }
        });

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
                tab.setText(categories[position]);
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
                                               SelectFragment selectFragment = SelectFragment.newInstance(30);
                                               getChildFragmentManager().beginTransaction()
                                                       .replace(R.id.fragment_home, selectFragment)
                                                       .addToBackStack(null)
                                                       .commit();
                                           }
                                       }
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}