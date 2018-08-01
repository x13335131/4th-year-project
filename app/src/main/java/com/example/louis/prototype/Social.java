package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Social extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_social);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Button b = (Button) findViewById(R.id.forumBtn); //social button
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {

                                       @Override
                                       public void onClick(View v) {
                                           Intent i8 = new Intent(getApplicationContext(), MainActivity.class);


                                           startActivity(i8);
                                       }
                                   }
            );
            b.setOnClickListener(new View.OnClickListener() {

                                     @Override
                                     public void onClick(View v) {
                                         Intent i5 = new Intent(getApplicationContext(), QandA.class);
                                         startActivity(i5);
                                     }
                                 }
            );
        }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
