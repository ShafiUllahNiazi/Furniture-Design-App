package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddImgs extends AppCompatActivity {
    String category ;
    Button choose;
    private static final int PICK_IMAGE = 1;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_imgs);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");
        choose = findViewById(R.id.chooseImg);
        category = getIntent().getStringExtra("category");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(intent, PICK_IMAGE);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
//                Toast.makeText(this, "result ok", Toast.LENGTH_SHORT).show();
                if(data.getClipData()!=null){

//                    Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();

                    int totalItemsSelected = data.getClipData().getItemCount();
                    for(int i =0; i<totalItemsSelected;i++){
                        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
                        dateFormatter.setLenient(false);
                        Date today = new Date();
                        final String timeSting = dateFormatter.format(today);

                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        if(fileUri!=null) {
//                            StorageReference fileReference = mStorageRef.child(timeSting + "_" + i);

                            StorageReference fileReference = mStorageRef.child(category).child(timeSting);
                            final int finalI = i;
                            final StorageReference fileReference1 = fileReference;
                            ///////////////////////////////////////////

                            ProgressBar.showProgressBar(AddImgs.this, "Uploading", "Please wait a while");
                            fileReference.putFile(fileUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return fileReference1.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        String uploadId = mDatabaseRef.push().getKey();
                                        ImageInfo imageInfo = new ImageInfo(timeSting , task.getResult().toString(),uploadId);
//                                        imageInfo.setmKey(up);
                                        mDatabaseRef.child(category).child(uploadId).setValue(imageInfo);

                                        ProgressBar.hideProgressBar();
//                                        Toast.makeText(MainActivity.this, imageInfo.getmImageUrl().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddImgs.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                        }else {
                            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                        }

                    }


                }else if(data.getData()!=null){
                    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
                    dateFormatter.setLenient(false);
                    Date today = new Date();
                    final String timeSting = dateFormatter.format(today);
                    final String timeString1= timeSting;
                    Uri fileUri1 = data.getData();
                    StorageReference fileReference = mStorageRef.child(category).child(timeSting);
                    final StorageReference fileReference1 = fileReference;
                    ProgressBar.showProgressBar(AddImgs.this, "Uploading", "Please wait a while");
                    fileReference.putFile(fileUri1).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String uploadId = mDatabaseRef.push().getKey();
                                ImageInfo imageInfo = new ImageInfo(timeSting, downloadUri.toString(),uploadId);
                                mDatabaseRef.child(category).child(uploadId).setValue(imageInfo);
                                ProgressBar.hideProgressBar();
//                                Toast.makeText(MainActivity.this, imageInfo.getmImageUrl().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddImgs.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
}