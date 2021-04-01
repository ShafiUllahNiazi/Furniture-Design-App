package com.example.firebasedemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.FullScreenImageActivity;
import com.example.firebasedemo.Home;
import com.example.firebasedemo.ImageInfo;
import com.example.firebasedemo.ProgressBar;
import com.example.firebasedemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter  extends RecyclerView.Adapter<HomeAdapter.ViewHolder>   {

    private Context mContext;
    private List<ImageInfo> mUploads;

    ImageInfo uploadCurrent11;
    String category;

    public HomeAdapter(Context mContext, List<ImageInfo> mUploads, String category) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        this.category = category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ImageInfo uploadCurrent = mUploads.get(position);
        uploadCurrent11 = mUploads.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                intent.putExtra("imgName",mUploads.get(position).getmName());
                intent.putExtra("imgUrl",mUploads.get(position).getmImageUrl());
                intent.putExtra("mkey",mUploads.get(position).getmKey());
                intent.putExtra("category",category);
                mContext.startActivity(intent);

            }
        });

        Picasso.get()
                .load(mUploads.get(position).getmImageUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
//        ProgressBar.hideProgressBar();


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }
    }
}
