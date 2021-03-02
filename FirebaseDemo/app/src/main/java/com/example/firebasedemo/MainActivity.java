package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    Button choose;
    Button btDeleteCategory;
    Button logout;

    ArrayList<Uri> imageList = new ArrayList<Uri>();
    private Uri imageUri;
    private int upload_count=0;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    TextView tvAddCategoty;
    List<String> categories;
    String selectedCategory;
    String selectedDeleteCategory;

    DatabaseReference databaseRef1;
    DatabaseReference databaseRef2;

    private ValueEventListener eventListener;
    private ValueEventListener eventListener2;

    Button adminCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");

        adminCategories = findViewById(R.id.adminCategories);
        adminCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, adminCategoriesList.class);

                startActivity(intent);
            }
        });

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinnerDelete = (Spinner) findViewById(R.id.spinnerDelete);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,CategoriesListActivity.class));
            }
        });
        tvAddCategoty = findViewById(R.id.tvAddCategoty);

        tvAddCategoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
        // Spinner click listener
//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
//        final List<String> categories = new ArrayList<String>();
//        categories = new ArrayList<String>();

        categories = new ArrayList<String>();

        databaseRef1 = FirebaseDatabase.getInstance().getReference().child("category");
        ProgressBar.showProgressBar(MainActivity.this,"Loading", "Please wait a while");
        eventListener = databaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {


                categories.clear();
//                Toast.makeText(MainActivity.this, "alarm", Toast.LENGTH_SHORT).show();
                Log.d("alarm", "alarm");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String categoryItem = postSnapshot.getValue().toString();
                    categories.add(categoryItem);
                }
            }
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        categories.add("Select_Item");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, i+ " position", Toast.LENGTH_SHORT).show();
//                spinner.setSelection(i);
//                spinner.setSelection();
                selectedCategory = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "selected "+ selectedCategory, Toast.LENGTH_SHORT).show();
//                adapterView.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        ///////////////////////////////////
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerDelete.setAdapter(dataAdapter1);
        spinnerDelete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDeleteCategory = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(MainActivity.this, selectedDeleteCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///////////////////////////////////////

        btDeleteCategory = findViewById(R.id.btDeleteCategory);
        btDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory();
            }
        });

        //////////////////////////

        choose = findViewById(R.id.choose);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCategory == null || selectedCategory.toLowerCase().equals("select_item")) {
                    Toast.makeText(MainActivity.this, "You have not select any category", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(intent, PICK_IMAGE);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseRef1.removeEventListener(eventListener);
        databaseRef2.removeEventListener(eventListener2);


    }

    private void deleteCategory() {
        if(selectedDeleteCategory == null || selectedDeleteCategory.toLowerCase().equals("select_item")){
            Toast.makeText(this, "You have not select any category", Toast.LENGTH_SHORT).show();
        }else {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Category");
        builder.setMessage("Do you want to delete "+selectedDeleteCategory+" ?");
        TextView textView = new TextView(this);

        builder.setView(textView);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param1DialogInterface, int param1Int) {

                        databaseRef2 = FirebaseDatabase.getInstance().getReference().child("category");
                        ProgressBar.showProgressBar(MainActivity.this,"Deleting","Please wait a while");
                        eventListener2 = databaseRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                        final String categoryItem = postSnapshot.getValue().toString();
                                        String keyval = postSnapshot.getKey();
                                        if(categoryItem.equals(selectedDeleteCategory.toLowerCase())){
                                            databaseRef2.child(keyval).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MainActivity.this, "Successfully Deleted "+categoryItem, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                                ProgressBar.hideProgressBar();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
        );
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
            }
        });
        builder.show();
    }
    }

    private void addCategory() {AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Category Name");
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        inputText.setMaxLines(1);
        inputText.setSingleLine(true);
        builder.setView(inputText);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                String str = inputText.getText().toString();
                if (!str.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("category").push().setValue(str.toLowerCase()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "ddsd", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
            }
        });
        builder.show();

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

                            StorageReference fileReference = mStorageRef.child(selectedCategory).child(timeSting);
                            final int finalI = i;
                            final StorageReference fileReference1 = fileReference;
                            ///////////////////////////////////////////

                            ProgressBar.showProgressBar(MainActivity.this, "Uploading", "Please wait a while");
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
                                        mDatabaseRef.child(selectedCategory).child(uploadId).setValue(imageInfo);

                                        ProgressBar.hideProgressBar();
//                                        Toast.makeText(MainActivity.this, imageInfo.getmImageUrl().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                    StorageReference fileReference = mStorageRef.child(selectedCategory).child(timeSting);
                    final StorageReference fileReference1 = fileReference;
                    ProgressBar.showProgressBar(MainActivity.this, "Uploading", "Please wait a while");
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
                                mDatabaseRef.child(selectedCategory).child(uploadId).setValue(imageInfo);
                                ProgressBar.hideProgressBar();
//                                Toast.makeText(MainActivity.this, imageInfo.getmImageUrl().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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