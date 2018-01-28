package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //private TextView tv;
    TextView tv;
    private static Timer myTimer;
    int secondsPassed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView)findViewById(R.id.textView35);
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
                if (panicButton.isShown()) {//if clicked and panic button is already showing, make it disappear
                    panicButton.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);

                } else {
                    panicButton.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                }

            }
        });

       panicButton.setOnClickListener(new View.OnClickListener(){
            boolean pressed=true;

           //Timer  myTimer = new Timer();
            @Override
            public void onClick(View v) {
                //if clicked
                if(pressed==true){
                    System.out.println("starting timer");
                    start(); //start timer
                    pressed=false;
                    tv.setText("testing1");
                }
                else{
                    System.out.println("ending timer");
                    end();
                    pressed=true;
                }

            }

           public void start(){
               myTimer = new Timer();
               TimerTask task = new TimerTask() {
                   public void run() {
                       secondsPassed++;
                       System.out.println("Seconds passed " + secondsPassed);
                       tv.setText("Seconds Passed: " + String.valueOf(secondsPassed));

                   }
               };
               myTimer.scheduleAtFixedRate(task, 1000,1000);
           }
           public void end(){secondsPassed=0; myTimer.cancel(); tv.setText("testing2");}
       });
    }
}
