package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Social extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

                                   @Override
                                   public void onClick(View v) {
                                       Intent i2 = new Intent(getApplicationContext(), MainActivity.class);


                                       startActivity(i2);
                                   }
                               }
        );

    }
}
