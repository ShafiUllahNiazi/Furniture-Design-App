package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign_in extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signIn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_in_function();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Toast.makeText(this, "already sign in " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Sign_in.this, MainActivity.class));
        }
        else {

            Toast.makeText(this, "Please login to perform as admin ", Toast.LENGTH_SHORT).show();
        }

    }

    public  void  log_in_function() {   

        String emaill = email.getText().toString();
        String passwordd = password.getText().toString();

        if (TextUtils.isEmpty(emaill)) {
            Toast.makeText(Sign_in.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            email.setError("Write Your Email");
            email.setText(null);
            return;
        }
        if (TextUtils.isEmpty(passwordd)) {
            Toast.makeText(Sign_in.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            password.setError("Write Your Password");
            password.setText(null);
            return;
        }

        ProgressBar.showProgressBar(Sign_in.this, "Signing in", "Please wait a while");

        mAuth.signInWithEmailAndPassword(emaill, passwordd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("sss", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Sign_in.this, "login successful ", Toast.LENGTH_SHORT).show();
                            ProgressBar.hideProgressBar();
                            startActivity(new Intent(Sign_in.this, MainActivity.class));
                        } else {

                            ProgressBar.hideProgressBar();
                            Toast.makeText(Sign_in.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void goToAdmin() {

        ProgressBar.hideProgressBar();

    }

}