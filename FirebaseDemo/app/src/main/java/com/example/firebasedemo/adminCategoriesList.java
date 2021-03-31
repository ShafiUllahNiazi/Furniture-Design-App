package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.firebasedemo.adapters.AdminCategoryListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminCategoriesList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> categorieslist;
    private AdminCategoryListAdapter adminCategoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories_list);
        fetchList();
        mRecyclerView = findViewById(R.id.admincategoryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categorieslist = new ArrayList<>();
//        categorieslist.add("sdd");

//        adminCategoryListAdapter = new AdminCategoryListAdapter(seeCatbar, seeCatbar, adminCategoriesList.this,categorieslist);
        mRecyclerView.setAdapter(adminCategoryListAdapter);

    }

    private void fetchList() {
        ProgressBar.showProgressBar(adminCategoriesList.this,"Fetching Categories","Please wait a while");
        DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference().child("category");
        databaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categorieslist.clear();
//                categorieslist.add("Select_Item");
//                Toast.makeText(MainActivity.this, "alarm", Toast.LENGTH_SHORT).show();
                Log.d("alarm","alarm");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String categoryItem = postSnapshot.getValue().toString();

                    categorieslist.add(categoryItem);
                    adminCategoryListAdapter.notifyDataSetChanged();
                }
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}