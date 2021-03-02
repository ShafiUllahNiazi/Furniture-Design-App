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
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.adapters.HomeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private DatabaseReference mDatabaseRef;
    List<ImageInfo> mUploads;

    TextView categoryTitle;

    private ValueEventListener eventListener;



//    Button adminView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        categoryTitle = findViewById(R.id.categoryTitle);
        String category = getIntent().getStringExtra("category");
        categoryTitle.setText(category);
//        Toast.makeText(this, "categorry? "+category, Toast.LENGTH_SHORT).show();

//        imageViewtest = findViewById(R.id.imageViewtest);
        mRecyclerView = findViewById(R.id.homeRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(Home.this,mUploads,category);
        mRecyclerView.setAdapter(mHomeAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images").child(category);
//        Toast.makeText(this, "bbbb", Toast.LENGTH_SHORT).show();

        ProgressBar.showProgressBar(Home.this,"Fetching Images","Please wait a while");
        eventListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                mUploads = new ArrayList<>();
                Log.d("sizee", String.valueOf(mUploads.size()));

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ImageInfo imageInfo = postSnapshot.getValue(ImageInfo.class);

                    mUploads.add(imageInfo);
//                    Toast.makeText(Home.this, "addedd", Toast.LENGTH_SHORT).show();

//                    Picasso.get()
//                            .load(imageInfo.getmImageUrl())
//                            .fit()
//                            .centerCrop()
//                            .placeholder(R.drawable.ic_baseline_brightness_high_24)
//                            .into(imageViewtest);
                }
                Log.d("sizee", String.valueOf(mUploads.size()));
                Collections.reverse(mUploads);
                mHomeAdapter.notifyDataSetChanged();
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(eventListener);
    }
}