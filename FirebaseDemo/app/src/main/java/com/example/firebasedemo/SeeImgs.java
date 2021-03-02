package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.adapters.HomeAdapter;
import com.example.firebasedemo.adapters.SeeImgsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeeImgs extends AppCompatActivity {

    TextView categoryTitle;
    String category;
    private RecyclerView mRecyclerView;
    private SeeImgsAdapter seeImgsAdapter;
    private DatabaseReference mDatabaseRef;
    List<ImageInfo> mUploads;



    private ValueEventListener eventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_imgs);
        categoryTitle = findViewById(R.id.seeImgscategoryTitle);


        category = getIntent().getStringExtra("category");
        categoryTitle.setText(category);
        mRecyclerView = findViewById(R.id.seeImgsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        seeImgsAdapter = new SeeImgsAdapter(SeeImgs.this,mUploads,category);
        mRecyclerView.setAdapter(seeImgsAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images").child(category);

        ProgressBar.showProgressBar(SeeImgs.this,"Fetching Images","Please wait a while");
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
                seeImgsAdapter.notifyDataSetChanged();
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SeeImgs.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(eventListener);
    }
}