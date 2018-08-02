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
            Button forumBtn = (Button) findViewById(R.id.forumBtn); //forum button
            Button statsBtn = (Button) findViewById(R.id.statisticsBtn); //stats button
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {

                                       @Override
                                       public void onClick(View v) {
                                           Intent i8 = new Intent(getApplicationContext(), MainActivity.class);


                                           startActivity(i8);
                                       }
                                   }
            );
            forumBtn.setOnClickListener(new View.OnClickListener() {

                                     @Override
                                     public void onClick(View v) {
                                         Intent forumIntent = new Intent(getApplicationContext(), ForumActivity.class);
                                         startActivity(forumIntent);
                                     }
                                 }
            );
            statsBtn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            Intent statsIntent = new Intent(getApplicationContext(), StatsActivity.class);
                                            startActivity(statsIntent);
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
