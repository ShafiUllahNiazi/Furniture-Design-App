package com.example.firebasedemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.FullScreenImageActivity;
import com.example.firebasedemo.ImageInfo;
import com.example.firebasedemo.ImgInfoWithSelect;
import com.example.firebasedemo.R;
import com.example.firebasedemo.SeeImgs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SeeImgsAdapter extends RecyclerView.Adapter<SeeImgsAdapter.ViewHolder>{
    private Context mContext;
//    int position;
//    private List<ImageInfo> mUploads;
    private static int SELECTED=2;
    private static int UN_SELECTED=3;
    ImageInfo uploadCurrent11;
    String category;
    LinearLayout seeImgsbar;
    private  List<ImgInfoWithSelect> mImgInfoWithSelectList;

    public SeeImgsAdapter(Context mContext, List<ImgInfoWithSelect> mImgInfoWithSelectList, String category, LinearLayout seeImgsbar) {
        this.mContext = mContext;
//        this.mUploads = mUploads;
        this.category = category;
        this.seeImgsbar = seeImgsbar;
        this.mImgInfoWithSelectList = mImgInfoWithSelectList;

//        mImgInfoWithSelectList = new ArrayList<>();


    }

    @NonNull
    @Override
    public SeeImgsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==SELECTED){
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.layout2, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }else {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.layout, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

    }



    @Override
    public int getItemViewType(int position) {


            if(mImgInfoWithSelectList.get(position).isSelected()==true){
                return SELECTED;

            }else {
                return UN_SELECTED;
            }




    }


    @Override
    public void onBindViewHolder(@NonNull SeeImgsAdapter.ViewHolder holder, final int position) {

        ImageInfo uploadCurrent = mImgInfoWithSelectList.get(position).getImageInfo();
        uploadCurrent11 = mImgInfoWithSelectList.get(position).getImageInfo();


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mImgInfoWithSelectList.get(position).setSelected(!(mImgInfoWithSelectList.get(position).isSelected()));
                notifyDataSetChanged();

                if(isAnyItemChecked(mImgInfoWithSelectList)){

                    seeImgsbar.setVisibility(View.VISIBLE);
                }else {
                    seeImgsbar.setVisibility(View.GONE);
                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
//                intent.putExtra("imgName",mUploads.get(position).getmName());
                intent.putExtra("imgName", mImgInfoWithSelectList.get(position).getImageInfo().getmName());
//                intent.putExtra("imgUrl",mUploads.get(position).getmImageUrl());
                intent.putExtra("imgUrl", mImgInfoWithSelectList.get(position).getImageInfo().getmImageUrl());
//                intent.putExtra("mkey",mUploads.get(position).getmKey());
                intent.putExtra("mkey", mImgInfoWithSelectList.get(position).getImageInfo().getmKey());
                intent.putExtra("category", category);
                mContext.startActivity(intent);

            }
        });


        Picasso.get()
                .load(mImgInfoWithSelectList.get(position).getImageInfo().getmImageUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
//        ProgressBar.hideProgressBar();
    }

    private boolean isAnyItemChecked(List<ImgInfoWithSelect> mImgInfoWithSelectList) {
        for(int i= 0; i< mImgInfoWithSelectList.size();i++){
            if(mImgInfoWithSelectList.get(i).isSelected()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mImgInfoWithSelectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }
    }
}
