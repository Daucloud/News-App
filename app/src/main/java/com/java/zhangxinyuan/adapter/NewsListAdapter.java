package com.java.zhangxinyuan.adapter;
import com.bumptech.glide.Glide;
import com.java.zhangxinyuan.R;
import com.java.zhangxinyuan.ui.home.TabNewsFragment;
import com.java.zhangxinyuan.utils.NewsInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.*;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyHolder> {
    private List<NewsInfo.DataDTO> mDataDTOList= new ArrayList<>();
    private TabNewsFragment mContext;


    public NewsListAdapter(TabNewsFragment context) {
        this.mContext = context;
    }


    /**
     * 为adapter 设置数据源
     */
    public void setListData(List<NewsInfo.DataDTO> list) {
        this.mDataDTOList= list;
        //一定要调用
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //加载布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_one_img, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        NewsInfo.DataDTO dataDTO= mDataDTOList.get(position);

        //设置数据
        holder.title.setText(dataDTO.getTitle());
        holder.description.setText(dataDTO.getPublisher()+" "+dataDTO.getPublishTime());
        //加载图片
        Glide.with(mContext).load(dataDTO.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataDTOList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView description;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.image_one_image);
            title = itemView.findViewById(R.id.title_one_image);
            description= itemView.findViewById(R.id.description_one_image);

        }
    }
}
