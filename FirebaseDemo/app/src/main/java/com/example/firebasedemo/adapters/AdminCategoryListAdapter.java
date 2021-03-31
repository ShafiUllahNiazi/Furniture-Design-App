package com.example.firebasedemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.Add_Delete_Items;
import com.example.firebasedemo.CategoryInfoWithSelect;
import com.example.firebasedemo.R;

import java.util.List;

public class AdminCategoryListAdapter extends RecyclerView.Adapter<AdminCategoryListAdapter.ViewHolder> {
    private Context mContext;
    private List<CategoryInfoWithSelect> categorieslist;

    private static int SELECTED=2;
    private static int UN_SELECTED=3;
    int position1;
    LinearLayout seeCatbar;



    public AdminCategoryListAdapter(LinearLayout seeCatbar, LinearLayout catbar, Context mContext, List<CategoryInfoWithSelect> categorieslist) {
        this.mContext = mContext;
        this.categorieslist = categorieslist;
        this.seeCatbar = seeCatbar;

    }

    @NonNull
    @Override
    public AdminCategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.categories_list_item, parent, false);
//        AdminCategoryListAdapter.ViewHolder viewHolder = new AdminCategoryListAdapter.ViewHolder(view);
//
        if(viewType==SELECTED){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.categories_list_item2, parent, false);
            AdminCategoryListAdapter.ViewHolder viewHolder = new AdminCategoryListAdapter.ViewHolder(view);
            return viewHolder;

        }else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.categories_list_item, parent, false);
            AdminCategoryListAdapter.ViewHolder viewHolder = new AdminCategoryListAdapter.ViewHolder(view);
            return viewHolder;

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(categorieslist.get(position).isSelected()==true){
//            Toast.makeText(mContext, "ff "+ mImgInfoWithSelectList.get(position).isSelected(), Toast.LENGTH_SHORT).show();
            return SELECTED;

        }else {
            return UN_SELECTED;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryListAdapter.ViewHolder holder, final int position) {
        holder.tvCategoryListItem.setText(categorieslist.get(position).getCategoryName().toString().toUpperCase());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                categorieslist.get(position).setSelected(!(categorieslist.get(position).isSelected()));
                notifyDataSetChanged();

                if(isAnyItemChecked(categorieslist)){
                    seeCatbar.setVisibility(View.VISIBLE);
                }else {
                    seeCatbar.setVisibility(View.GONE);
                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Add_Delete_Items.class);
                intent.putExtra("category", categorieslist.get(position).getCategoryName().toString());
                mContext.startActivity(intent);
            }
        });



    }

    private boolean isAnyItemChecked(List<CategoryInfoWithSelect> categorieslist) {
        for(int i= 0; i< categorieslist.size();i++){
            if(categorieslist.get(i).isSelected()){
                return true;
            }
        }
        return false;
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
