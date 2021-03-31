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
//    List<ImageInfo> mUploads;
    private  List<ImgInfoWithSelect> mImgInfoWithSelectList;
    LinearLayout seeImgsbar;
    TextView seeImgsSelectAll,seeImgsDeleteMore,seeImgsCancel;
    int k;

    ImgInfoWithSelect imgInfoWithSelect;




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
//        mUploads = new ArrayList<>();
        mImgInfoWithSelectList = new ArrayList<>();
        seeImgsAdapter = new SeeImgsAdapter(SeeImgs.this,mImgInfoWithSelectList,category,seeImgsbar);
        mRecyclerView.setAdapter(seeImgsAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images").child(category);
        mStorageRef = FirebaseStorage.getInstance().getReference("images").child(category);
        

        fetchImgs();


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
//                Toast.makeText(SeeImgs.this, "Delete", Toast.LENGTH_SHORT).show();
                ProgressBar.showProgressBar(SeeImgs.this,"Deleting Images","Please wait a while");
                for(int i=0;i<mImgInfoWithSelectList.size();i++){
                    k=i;
//                    Toast.makeText(SeeImgs.this, k, Toast.LENGTH_SHORT).show();
                    if(mImgInfoWithSelectList.get(k).isSelected){
                        imgInfoWithSelect = mImgInfoWithSelectList.get(k);
                        mDatabaseRef.child(imgInfoWithSelect.imageInfo.getmKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(SeeImgs.this, "Img ref Successfully Deleted", Toast.LENGTH_SHORT).show();
                                mStorageRef.child(imgInfoWithSelect.imageInfo.getmName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        seeImgsbar.setVisibility(View.GONE);
                                        ProgressBar.hideProgressBar();
                                        fetchImgs();
                                        Toast.makeText(SeeImgs.this, "Img  Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });



                        Toast.makeText(SeeImgs.this, "gggfg+\n"+mImgInfoWithSelectList.get(k).isSelected, Toast.LENGTH_SHORT).show();
//                        mStorageRef.child(mImgInfoWithSelectList.get(k).imageInfo.getmName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(SeeImgs.this, "Img Successfully Deleted", Toast.LENGTH_SHORT).show();
//                                ProgressBar.hideProgressBar();
//                                ProgressBar.showProgressBar(SeeImgs.this,"Deleting Images Ref","Please wait a while");
//                                mDatabaseRef.child(mImgInfoWithSelectList.get(k).imageInfo.getmKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
////                                        mImgInfoWithSelectList.remove(k);
//
//                                        seeImgsbar.setVisibility(View.GONE);
//                                        ProgressBar.hideProgressBar();
//
//                                    }
//                                });
//                            }
//                        });







                    }
                }
//                seeImgsAdapter.notifyDataSetChanged();
//                ProgressBar.hideProgressBar();
//                fetchImgs();




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

    private void fetchImgs() {

        ProgressBar.showProgressBar(SeeImgs.this,"Fetching Images","Please wait a while");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                mUploads = new ArrayList<>();


                mImgInfoWithSelectList.clear();
//                seeImgsAdapter.notifyDataSetChanged();
//                mImgInfoWithSelectList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ImageInfo imageInfo = postSnapshot.getValue(ImageInfo.class);


                    mImgInfoWithSelectList.add(new ImgInfoWithSelect(imageInfo,false));
//                    Toast.makeText(Home.this, "addedd", Toast.LENGTH_SHORT).show();

//                    Picasso.get()
//                            .load(imageInfo.getmImageUrl())
//                            .fit()
//                            .centerCrop()
//                            .placeholder(R.drawable.ic_baseline_brightness_high_24)
//                            .into(imageViewtest);
                }

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mDatabaseRef.removeEventListener(eventListener);
    }
}