package com.example.firebasedemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.Home;
import com.example.firebasedemo.ImageInfo;
import com.example.firebasedemo.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private Context mContext;
    private List<String> categorieslist;

    public CategoryListAdapter(Context mContext, List<String> categorieslist) {
        this.mContext = mContext;
        this.categorieslist = categorieslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categories_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        Log.d("tttttt", String.valueOf(categorieslist.size()));
        holder.tvCategoryListItem.setText(categorieslist.get(position).toString());
        final int position1 = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Home.class);
                intent.putExtra("category", categorieslist.get(position1).toString());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categorieslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryListItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryListItem = itemView.findViewById(R.id.tvCategoryListItem);
        }
    }
}
