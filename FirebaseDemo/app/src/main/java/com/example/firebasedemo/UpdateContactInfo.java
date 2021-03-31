package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateContactInfo extends AppCompatActivity {

    private Button btnUpdate;
    private EditText etname, etcontact,etopen,etclose,etaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact_info);
        btnUpdate = findViewById(R.id.btnUpdate);
        etname = findViewById(R.id.etname);
        etcontact = findViewById(R.id.etcontact);
        etopen = findViewById(R.id.etopen);
        etclose = findViewById(R.id.etclose);
        etaddress = findViewById(R.id.etaddress);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
    }

    private void updateInfo() {
        final String name = etname.getText().toString();
        String contact = etcontact.getText().toString();
        String open = etopen.getText().toString();
        String close = etclose.getText().toString();
        String address = etaddress.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(UpdateContactInfo.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            etname.setError("Write Your Name");
            etname.setText(null);
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            Toast.makeText(UpdateContactInfo.this, "Please Enter Contact No", Toast.LENGTH_SHORT).show();
            etcontact.setError("Write Your Contact No");
            etcontact.setText(null);
            return;
        }
        if (TextUtils.isEmpty(open)) {
            Toast.makeText(UpdateContactInfo.this, "Please Enter Shop Opening Time", Toast.LENGTH_SHORT).show();
            etname.setError("Write Your Shop Opening Time");
            etname.setText(null);
            return;
        }
        if (TextUtils.isEmpty(close)) {
            Toast.makeText(UpdateContactInfo.this, "Please Enter Shop Closing Time", Toast.LENGTH_SHORT).show();
            etcontact.setError("Write Your Shop Closing Time");
            etcontact.setText(null);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(UpdateContactInfo.this, "Please Enter Shop Address", Toast.LENGTH_SHORT).show();
            etname.setError("Write Your Shop Address");
            etname.setText(null);
            return;
        }

        ProgressBar.showProgressBar(UpdateContactInfo.this, "Updating", "Please wait a while");
        AdminInfo adminInfo = new AdminInfo(name,contact,open,close,address);
        FirebaseDatabase.getInstance().getReference().child("admin_Info").setValue(adminInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ProgressBar.hideProgressBar();
                Intent intent = new Intent(UpdateContactInfo.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}