package com.example.mies_dinapen.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mies_dinapen.MainActivity;
import com.example.mies_dinapen.R;

public class Select_inst extends AppCompatActivity implements View.OnClickListener {

    ImageButton Mies1, Police1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_inst);
        Mies1=(ImageButton) findViewById(R.id.buttonMies);
        Mies1.setOnClickListener(this);
        Police1=(ImageButton) findViewById(R.id.buttonPolice);
        Police1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==Mies1){
            Intent i =new Intent(Select_inst.this, MainActivity.class);
            i.putExtra("Variable","1");
            startActivity(i);
        }
        if (view==Police1){
            Intent i =new Intent(Select_inst.this, MainActivity.class);
            i.putExtra("Variable","2");
            startActivity(i);
        }

    }
}
