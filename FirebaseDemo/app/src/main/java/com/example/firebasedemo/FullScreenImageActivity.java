package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {
    ImageView ivFullScreen;


    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    String imgUrl;
    String imgName;
    String mkey;
    String category;
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imgUrl = getIntent().getStringExtra("imgUrl");
        imgName = getIntent().getStringExtra("imgName");
        mkey = getIntent().getStringExtra("mkey");
        category = getIntent().getStringExtra("category");
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");


        ivFullScreen = findViewById(R.id.ivFullScreen);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerInside()
                .into(ivFullScreen);
    }

    private void deleteImage() {
        StorageReference storageRef2 = mStorage.getReferenceFromUrl(imgUrl);
        storageRef2.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("failee","delete");
                        Toast.makeText(FullScreenImageActivity.this, "Successfully Deleted from storage " + imgName, Toast.LENGTH_SHORT).show();
                        mDatabaseRef.child(category).child(mkey).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("failee","Successfully deleted from database");
                                        Toast.makeText(FullScreenImageActivity.this, "Successfully deleted from database", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("failee",e.getMessage());
                        Toast.makeText(FullScreenImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            ivFullScreen.setScaleX(mScaleFactor);
            ivFullScreen.setScaleY(mScaleFactor);
            return true;
        }
    }
}