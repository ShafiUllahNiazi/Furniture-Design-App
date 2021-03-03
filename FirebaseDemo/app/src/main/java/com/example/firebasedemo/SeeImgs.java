package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.adapters.HomeAdapter;
import com.example.firebasedemo.adapters.SeeImgsAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeeImgs extends AppCompatActivity {

    TextView categoryTitle;
    String category;
    private RecyclerView mRecyclerView;
    private SeeImgsAdapter seeImgsAdapter;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    List<ImageInfo> mUploads;
    private  List<ImgInfoWithSelect> mImgInfoWithSelectList;
    LinearLayout seeImgsbar;
    TextView seeImgsSelectAll,seeImgsDeleteMore,seeImgsCancel;
    int k;





    private ValueEventListener eventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_imgs);
        categoryTitle = findViewById(R.id.seeImgscategoryTitle);
        seeImgsSelectAll = findViewById(R.id.seeImgsSelectAll);
        seeImgsDeleteMore = findViewById(R.id.seeImgsDeleteMore);
        seeImgsCancel = findViewById(R.id.seeImgsCancel);

        seeImgsbar=findViewById(R.id.seeImgsbar);
        category = getIntent().getStringExtra("category");
        categoryTitle.setText(category);
        mRecyclerView = findViewById(R.id.seeImgsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mImgInfoWithSelectList = new ArrayList<>();
        seeImgsAdapter = new SeeImgsAdapter(SeeImgs.this,mUploads,mImgInfoWithSelectList,category,seeImgsbar);
        mRecyclerView.setAdapter(seeImgsAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images").child(category);
        mStorageRef = FirebaseStorage.getInstance().getReference("images").child(category);
        




        ProgressBar.showProgressBar(SeeImgs.this,"Fetching Images","Please wait a while");
        eventListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                mUploads = new ArrayList<>();
                Log.d("sizee", String.valueOf(mUploads.size()));

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ImageInfo imageInfo = postSnapshot.getValue(ImageInfo.class);

                    mUploads.add(imageInfo);
                    mImgInfoWithSelectList.add(new ImgInfoWithSelect(imageInfo,false));
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
//                seeImgsAdapter.notifyDataSetChanged();
                Collections.reverse(mImgInfoWithSelectList);
                seeImgsAdapter.notifyDataSetChanged();
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SeeImgs.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        seeImgsSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<mImgInfoWithSelectList.size();i++){
                    mImgInfoWithSelectList.get(i).setSelected(true);
                }

                seeImgsAdapter.notifyDataSetChanged();
            }
        });

        seeImgsDeleteMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SeeImgs.this, "Delete", Toast.LENGTH_SHORT).show();
                ProgressBar.showProgressBar(SeeImgs.this,"Deleting Images","Please wait a while");
                for(int i=0;i<mImgInfoWithSelectList.size();i++){
                    k=i;
                    if(mImgInfoWithSelectList.get(i).isSelected){
                        mDatabaseRef.child(mImgInfoWithSelectList.get(k).imageInfo.getmKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mStorageRef.child(mImgInfoWithSelectList.get(k).imageInfo.getmName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SeeImgs.this, "Img Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }




            }
        });

        seeImgsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<mImgInfoWithSelectList.size();i++){
                    mImgInfoWithSelectList.get(i).setSelected(false);
                }
                seeImgsbar.setVisibility(View.GONE);
                seeImgsAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(eventListener);
    }
}