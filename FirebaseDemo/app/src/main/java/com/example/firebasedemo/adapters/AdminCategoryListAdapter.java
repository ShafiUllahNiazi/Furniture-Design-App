package com.example.firebasedemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.Add_Delete_Items;
import com.example.firebasedemo.Home;
import com.example.firebasedemo.R;
import com.example.firebasedemo.adminCategoriesList;

import java.util.List;

public class AdminCategoryListAdapter extends RecyclerView.Adapter<AdminCategoryListAdapter.ViewHolder> {
    private Context mContext;
    private List<String> categorieslist;

    public AdminCategoryListAdapter(Context mContext, List<String> categorieslist) {
        this.mContext = mContext;
        this.categorieslist = categorieslist;
    }

    @NonNull
    @Override
    public AdminCategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categories_list_item, parent, false);
        AdminCategoryListAdapter.ViewHolder viewHolder = new AdminCategoryListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryListAdapter.ViewHolder holder, int position) {
        holder.tvCategoryListItem.setText(categorieslist.get(position).toString());
        final int position1 = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Add_Delete_Items.class);
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
