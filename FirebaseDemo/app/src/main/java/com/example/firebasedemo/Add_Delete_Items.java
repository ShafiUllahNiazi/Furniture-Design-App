package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Add_Delete_Items extends AppCompatActivity {

    Button addImgs, seeImgs;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__delete__items);
        addImgs=findViewById(R.id.addImgs);
        seeImgs=findViewById(R.id.seeImgs);
        category = getIntent().getStringExtra("category");

        addImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Delete_Items.this, AddImgs.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });



        seeImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(Add_Delete_Items.this, "click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_Delete_Items.this, SeeImgs.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });


    }
}