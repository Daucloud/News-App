package com.java.zhangxinyuan.ui;

import com.java.zhangxinyuan.databinding.ActivitySearchBinding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View view;
    private ActivitySearchBinding binding;
    private SearchView searchView;
    private GridLayout gridLayout;
    private DatePicker startDatePicker, endDatePicker;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        toolbar = binding.toolbar;
        searchView = binding.homeSearchView;
        gridLayout = binding.selections;
        startDatePicker = binding.datePicker1;
        endDatePicker = binding.datePicker2;


        //返回事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //搜索事件

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                                              @Override
                                              public boolean onQueryTextSubmit(String query) {
                                                  Log.d("SearchFragment", query);
                                                  List<String> categories = new ArrayList<>();
                                                  int k = gridLayout.getChildCount();
                                                  for (int i = 0; i < k; i++) {
                                                      CheckBox checkBox = (CheckBox) gridLayout.getChildAt(i);
                                                      if (checkBox.isChecked()) {
                                                          categories.add((String) checkBox.getText());
                                                      }
                                                  }
String categoriesString = String.join(",", categories);
                                                  Calendar startTime = Calendar.getInstance();
                                                  startTime.set(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth());

                                                  Calendar endTime = Calendar.getInstance();
                                                  endTime.set(endDatePicker.getYear(), endDatePicker.getMonth(), endDatePicker.getDayOfMonth());

                                                  endTime.add(Calendar.DAY_OF_MONTH, 1);


                                                  String startTimeString = dateFormat.format(startTime.getTime());
                                                  String endTimeString = dateFormat.format(endTime.getTime());

                                                  Log.d("SearchFragment", query+ categories + startTimeString + endTimeString);

                                                  Intent intent=new Intent(SearchActivity.this, SearchedNewsActivity.class);
                                                  intent.putExtra("words", query);
                                                  intent.putExtra("categories", categoriesString);
                                                  intent.putExtra("startTime", startTimeString);
                                                  intent.putExtra("endTime", endTimeString);
                                                  startActivity(intent);
                                                  return false;
                                              }

                                              @Override
                                              public boolean onQueryTextChange(String newText) {
                                                  // TODO Auto-generated method stub
                                                  return false;
                                              }
                                          }
        );

    }

}