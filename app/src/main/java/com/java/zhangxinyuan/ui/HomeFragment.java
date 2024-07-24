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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        button = binding.searchButton;
        imageButton = binding.imageButton;
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager2;

        setupViewPagerAndTabs();

        // Search button click event
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        // Image button click event
        imageButton.setOnClickListener(v -> {
            SelectFragment selectFragment = SelectFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("categories", new ArrayList<>(categories.subList(1, categories.size())));
            selectFragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.select_fragment_container, selectFragment)
                    .addToBackStack(null)
                    .commit();
            binding.selectFragmentContainer.setBackgroundColor(Color.WHITE);
        });

        // Listen for results from child fragments
        getChildFragmentManager().setFragmentResultListener("requestKey", this,
                (requestKey, result) -> {
                    categories.clear();
                    categories.add("全部");
                    categories.addAll(Objects.requireNonNull(result.getStringArrayList("categories")));
                    updateTabs();
                    binding.selectFragmentContainer.setBackgroundColor(Color.TRANSPARENT);
                });

        return root;
    }

    private void setupViewPagerAndTabs() {
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

        viewPager2.setAdapter(fragmentStateAdapter);

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

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(categories.get(position))).attach();
    }

    private void updateTabs() {
        tabLayout.removeAllTabs(); // Remove all existing tabs
        for (String category : categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get the current page
        int currentItem = viewPager2.getCurrentItem();
        // Notify the adapter to refresh the current page
        if (fragmentStateAdapter != null && currentItem >= 0) {
            Log.d("==========================", "onResume: 08988909999999999");
            fragmentStateAdapter.notifyItemChanged(currentItem);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
