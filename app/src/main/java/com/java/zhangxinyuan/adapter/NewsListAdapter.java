package com.java.zhangxinyuan.adapter;
import com.bumptech.glide.Glide;
import com.java.zhangxinyuan.R;
import com.java.zhangxinyuan.ui.home.TabNewsFragment;
import com.java.zhangxinyuan.utils.NewsInfo;

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
        View view = null;
        switch (viewType){
            case 0:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_no_img,parent,false);
                break;
            case 1:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_one_img,parent,false);
                break;
            case 3:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_three_img,parent,false);
                break;
        }
        return new MyHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        NewsInfo.DataDTO dataDTO= mDataDTOList.get(position);

        //设置数据
        holder.title.setText(dataDTO.getTitle());
        holder.description.setText(dataDTO.getPublisher()+" "+dataDTO.getPublishTime());
        //加载图片
        ArrayList<String>images=dataDTO.getImage();
        switch (holder.type){
            case 1:
                Glide.with(mContext).load(images.get(0)).error(R.drawable.baseline_image_not_supported_24).into(holder.image1);
                break;
            case 3:
                Glide.with(mContext).load(images.get(0)).error(R.drawable.baseline_image_not_supported_24).into(holder.image1);
                Glide.with(mContext).load(images.get(1)).error(R.drawable.baseline_image_not_supported_24).into(holder.image2);
                Glide.with(mContext).load(images.get(2)).error(R.drawable.baseline_image_not_supported_24).into(holder.image3);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataDTOList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        ImageView image1,image2,image3;
        TextView title;
        TextView description;
        int type;

        public MyHolder(@NonNull View itemView,int type) {
            super(itemView);
            this.type=type;
            switch (type){
                case 0:
                    title=itemView.findViewById(R.id.title_no_image);
                    description=itemView.findViewById(R.id.description_no_image);
                    break;
                case 1:
                    title=itemView.findViewById(R.id.title_one_image);
                    description=itemView.findViewById(R.id.description_one_image);
                    image1=itemView.findViewById(R.id.image_one_image);
                    break;
                case 3:
                    title=itemView.findViewById(R.id.title_three_image);
                    description=itemView.findViewById(R.id.description_three_image);
                    image1=itemView.findViewById(R.id.image1_three_image);
                    image2=itemView.findViewById(R.id.image2_three_image);
                    image3=itemView.findViewById(R.id.image3_three_image);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int size=mDataDTOList.get(position).getImage().size();
        switch(size){
            case 0:
                return  0;
            case 1: case 2:
                return 1;
            default:
                return 3;
        }
    }
}
