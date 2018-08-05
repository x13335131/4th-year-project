package com.example.louis.prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Social extends AppCompatActivity {
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_social);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.textView3));
            Button forumBtn = (Button) findViewById(R.id.forumBtn); //forum button
            Button statsBtn = (Button) findViewById(R.id.statisticsBtn); //stats button
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        spinner.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {

                                       @Override
                                       public void onClick(View v) {
                                           Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                           startActivity(mainIntent);
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
