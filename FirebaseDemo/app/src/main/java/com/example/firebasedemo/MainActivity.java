package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.adapters.AdminCategoryListAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    Button choose;

    Button logout,btnUpdateInfo;
    int k;

    ArrayList<Uri> imageList = new ArrayList<Uri>();
    private Uri imageUri;
    private int upload_count=0;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    ImageButton btnAddCategoty;
    List<String> categories;
    String selectedCategory;
    String selectedDeleteCategory;

    DatabaseReference databaseRef1;
    DatabaseReference databaseRef2;

    private ValueEventListener eventListener;
    private ValueEventListener eventListener2;

    Button adminCategories;

    LinearLayout seeCatbar;
    TextView seeCatSelectAll,seeCatDeleteMore,seeCatCancel;
    CategoryInfoWithSelect imgInfoWithSelect;

    private RecyclerView mRecyclerView;
    private List<String> categorieslist;
    private List<CategoryInfoWithSelect> categorieslistWithSelectInfo;
    private AdminCategoryListAdapter adminCategoryListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchList();
        seeCatbar = findViewById(R.id.seeCatbar);
        seeCatSelectAll = findViewById(R.id.seeCatSelectAll);
        seeCatDeleteMore = findViewById(R.id.seeCatDeleteMore);
        seeCatCancel = findViewById(R.id.seeCatCancel);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);

        mRecyclerView = findViewById(R.id.admincategoryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categorieslist = new ArrayList<>();
        categorieslistWithSelectInfo = new ArrayList<>();
//        categorieslist.add("sdd");

        adminCategoryListAdapter = new AdminCategoryListAdapter(seeCatbar,seeCatbar,MainActivity.this,categorieslistWithSelectInfo);
        mRecyclerView.setAdapter(adminCategoryListAdapter);

        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");


        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateContactInfo.class);
                startActivity(intent);
            }
        });

        seeCatSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<categorieslistWithSelectInfo.size();i++){
                    categorieslistWithSelectInfo.get(i).setSelected(true);
                }
                adminCategoryListAdapter.notifyDataSetChanged();
            }
        });

        seeCatDeleteMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Delete " , Toast.LENGTH_SHORT).show();
//                ProgressBar.showProgressBar(MainActivity.this,"Deleting Images","Please wait a while");
                for(int i=0;i<categorieslistWithSelectInfo.size();i++){
                    k=i;
//                    Toast.makeText(SeeImgs.this, k, Toast.LENGTH_SHORT).show();
                    if(categorieslistWithSelectInfo.get(k).isSelected){
                        imgInfoWithSelect = categorieslistWithSelectInfo.get(k);
                        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!(dataSnapshot.hasChild(imgInfoWithSelect.categoryName))) {
                                    ProgressBar.showProgressBar(MainActivity.this,"Deleting Category","Please wait a while");
//                                    Toast.makeText(MainActivity.this, "available", Toast.LENGTH_SHORT).show();
                                    databaseRef2=FirebaseDatabase.getInstance().getReference("category");
                                    databaseRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                                    final String categoryItem = postSnapshot.getValue().toString();
                                                    String keyval = postSnapshot.getKey();
                                                    if(categoryItem.equals(imgInfoWithSelect.categoryName)){
                                                        databaseRef2.child(keyval).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                ProgressBar.hideProgressBar();
                                                                Toast.makeText(MainActivity.this, "Successfully Deleted "+categoryItem, Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Please Delete Images from this Category", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

            }
        });

        seeCatCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<categorieslistWithSelectInfo.size();i++){
                    categorieslistWithSelectInfo.get(i).setSelected(false);
                }
                seeCatbar.setVisibility(View.GONE);
                adminCategoryListAdapter.notifyDataSetChanged();
            }
        });


        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,CategoriesListActivity.class));
            }
        });
        btnAddCategoty = findViewById(R.id.btnAddCategoty);

        btnAddCategoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });


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



    }

    private void deleteFromImgNode() {
        mDatabaseRef.child(imgInfoWithSelect.categoryName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                                Toast.makeText(SeeImgs.this, "Img ref Successfully Deleted", Toast.LENGTH_SHORT).show();

                mStorageRef.child(imgInfoWithSelect.categoryName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        seeCatbar.setVisibility(View.GONE);
                        ProgressBar.hideProgressBar();
                        fetchList();
                        Toast.makeText(MainActivity.this, "item  Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseRef1.removeEventListener(eventListener);
        databaseRef2.removeEventListener(eventListener2);


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
                    for(int i=0;i<categorieslistWithSelectInfo.size();i++){
                        if(str.toLowerCase().equals(categorieslistWithSelectInfo.get(i).getCategoryName().toLowerCase())){
                            Toast.makeText(MainActivity.this, str + " category already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
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

    private void fetchList() {
        ProgressBar.showProgressBar(MainActivity.this,"Fetching Categories","Please wait a while");
        DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference().child("category");
        databaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                categorieslist.clear();
                categorieslistWithSelectInfo.clear();
//                categorieslist.add("Select_Item");
//                Toast.makeText(MainActivity.this, "alarm", Toast.LENGTH_SHORT).show();
                Log.d("alarm","alarm");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String categoryItem = postSnapshot.getValue().toString();

                    categorieslistWithSelectInfo.add(new CategoryInfoWithSelect(false,categoryItem));
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