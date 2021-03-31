package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebasedemo.adapters.CategoryListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriesListActivity extends AppCompatActivity {

    ImageView ivAdmin;
    ImageView ivRefresh;
    Button btnAboutUs;
//    private DatabaseReference mDatabaseRef;

    private List<String> categorieslist;
    private RecyclerView mRecyclerView;
    private CategoryListAdapter categoryListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        ivRefresh = findViewById(R.id.ivRefresh);
        ivAdmin = findViewById(R.id.ivAdmin);
        mRecyclerView = findViewById(R.id.categoryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnAboutUs = findViewById(R.id.btnAboutUs);
        categorieslist = new ArrayList<>();

        categoryListAdapter = new CategoryListAdapter(CategoriesListActivity.this,categorieslist);
        mRecyclerView.setAdapter(categoryListAdapter);
        fetchList();

        ivAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriesListActivity.this, Sign_in.class);
                startActivity(intent);
            }
        });


        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CategoriesListActivity.this, "Refresh clicked", Toast.LENGTH_SHORT).show();
                fetchList();
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriesListActivity.this, DisplayContactInfo.class);
                startActivity(intent);
            }
        });
    }

    private void fetchList() {
        ProgressBar.showProgressBar(CategoriesListActivity.this,"Fetching Categories","Please wait a while");
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
                    categoryListAdapter.notifyDataSetChanged();
                }
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
    }
}