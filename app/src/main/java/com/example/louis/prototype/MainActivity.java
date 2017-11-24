package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int secondsPassed =0;
    Timer myTimer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            secondsPassed++;
            System.out.println("Seconds passed " + secondsPassed);
        }
    };
    public void start(){
        myTimer.scheduleAtFixedRate(task, 1000,1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button)findViewById(R.id.button6); //diary button
        Button b2 = (Button)findViewById(R.id.button7); //calendar button
        Button b3 = (Button)findViewById(R.id.button8); //chart button
        Button b4 = (Button)findViewById(R.id.button); //social button
        Switch s = (Switch)findViewById(R.id.switch2);

        final Button panicButton = (Button) findViewById(R.id.button11);
        b1.setOnClickListener(new View.OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      Intent i1 = new Intent(getApplicationContext(), MyDiary.class);
                                      startActivity(i1);
                                  }
                              }
        );
        b2.setOnClickListener(new View.OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      Intent i2 = new Intent(getApplicationContext(), Calendar.class);
                                      startActivity(i2);
                                  }
                              }
        );
        b3.setOnClickListener(new View.OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      Intent i3 = new Intent(getApplicationContext(), Chart.class);
                                      startActivity(i3);
                                  }
                              }
        );
        b4.setOnClickListener(new View.OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      Intent i4 = new Intent(getApplicationContext(), Social.class);
                                      startActivity(i4);
                                  }
                              }
        );
        s.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (panicButton.isShown()) {
                    panicButton.setVisibility(View.GONE);

                } else {
                    panicButton.setVisibility(View.VISIBLE);

                }

               // panicButton.setVisibility(View.VISIBLE);
            }
        });

        panicButton.setOnClickListener(new View.OnClickListener(){
            boolean pressed=true;
            @Override
            public void onClick(View v) {

                if(pressed==true){
                    start();
                    pressed=false;
                }
                else{
                    myTimer.cancel();
                }


            }
        });
    }
}
