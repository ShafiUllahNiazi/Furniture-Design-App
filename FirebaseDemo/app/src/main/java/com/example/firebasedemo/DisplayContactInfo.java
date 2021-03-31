package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayContactInfo extends AppCompatActivity {

    private TextView tvName, tvContact,tvOpen,tvClose,tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact_info);
        tvName = findViewById(R.id.tvName);
        tvContact = findViewById(R.id.tvContact);
        tvOpen = findViewById(R.id.tvOpen);
        tvClose = findViewById(R.id.tvClose);
        tvAddress = findViewById(R.id.tvAddress);

        ProgressBar.showProgressBar(DisplayContactInfo.this, "Fetching Details", "Please wait a while");
        FirebaseDatabase.getInstance().getReference().child("admin_Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdminInfo adminInfo = dataSnapshot.getValue(AdminInfo.class);
                tvName.setText("Name: "+adminInfo.name);
                tvContact.setText("Contact: "+adminInfo.contact);
                tvOpen.setText("Opens at: "+adminInfo.open);
                tvClose.setText("Closes at: "+adminInfo.close);
                tvAddress.setText("Address: "+adminInfo.address);
                ProgressBar.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ProgressBar.hideProgressBar();
                Toast.makeText(DisplayContactInfo.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}