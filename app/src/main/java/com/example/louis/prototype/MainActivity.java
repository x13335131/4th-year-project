package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //private TextView tv;
    DatabaseReference OdsisDb;
    TextView tv;
    private static Timer myTimer;
    int secondsPassed = 0;
    int secondsCaptured;
    List<PanicButton> panicList;
    DatabaseReference databasePanic;
    private static final String TAG = "MainActivity";
    String userID;
    String value;
    float daysBetween;
    private FirebaseAuth mAuth;

    private Toolbar mainToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        tv= (TextView)findViewById(R.id.textView35);
        Button b1 = (Button)findViewById(R.id.button6); //diary button
        Button b2 = (Button)findViewById(R.id.button7); //calendar button
        Button b3 = (Button)findViewById(R.id.button8); //chart button
        Button b4 = (Button)findViewById(R.id.button); //social button
        final Button survay = (Button)findViewById(R.id.survayBtn); //to oasis and odsis
        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("My Mental Health");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID= currentFirebaseUser.getUid();

        OdsisDb= database.getReference("odsis");

        Query lastQuery = OdsisDb.orderByChild("user").equalTo(userID).limitToLast(1);//.orderByKey().limitToLast(1);
        lastQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    value = child.getValue().toString();
                    if (key.equals("todaysDate")) {
                        System.out.println("key " + key + " Value " + value);
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        String dateBeforeString =  value;
                        String dateAfterString =new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
                        System.out.println("Date Before String: "+dateBeforeString);
                        System.out.println("Date After String: "+dateAfterString);
                        try {
                            Date dateBefore = myFormat.parse(dateBeforeString);
                            Date dateAfter = myFormat.parse(dateAfterString);

                            long difference = dateAfter.getTime() - dateBefore.getTime();
                            daysBetween = (difference / (1000*60*60*24));

                            System.out.println("Number of Days between dates: "+daysBetween);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if( daysBetween < 7.0){ //it has been less than 7 days since user took odsis survay then set button to invisible
                            System.out.println("survay set to invisible ");
                            survay.setVisibility(View.INVISIBLE);
                        }else { //it has been more than 7 days than they may take survay
                            System.out.println("Survay set to visible ");
                            survay.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Switch s = (Switch)findViewById(R.id.switch2);
        databasePanic = FirebaseDatabase.getInstance().getReference("panic");
        final Button panicButton = (Button) findViewById(R.id.button11);
        panicList = new ArrayList<>();
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
        survay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(getApplicationContext(), Oasis.class);
                startActivity(i5);
            }
        });
        s.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (panicButton.isShown()) {//if clicked and panic button is already showing, make it disappear
                    //myTimer = new Timer();
                    secondsPassed=0;
                   // myTimer.cancel();

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
                    tv.setText("Timer Start");
                }
                else{
                    System.out.println("ending timer");
                    end();
                    pressed=true;
                }
            }

            //start timer
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

           //end timer
           public void end(){
               addPanicToDiary();
               getUserData();
               //a = tv.getText().toString();
               secondsPassed=0;
               myTimer.cancel();
               tv.setText("Timer Stopped"+secondsCaptured);
           }
           public void addPanicToDiary(){
               secondsCaptured = secondsPassed;
               DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
               java.util.Calendar cal = java.util.Calendar.getInstance();
               String todaysDate = df.format(cal.getTime());

               FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
               userID= currentFirebaseUser.getUid();

                   String id= databasePanic.push().getKey();
                   PanicButton panic = new PanicButton(secondsCaptured, todaysDate, userID);
                   databasePanic.child(id).setValue(panic);
                   Toast.makeText(MainActivity.this ,"panic attack added", Toast.LENGTH_LONG).show();
           }
            public void getUserData(){
                // Read from the database
                //ordering output by length
               // final Query userQuery = databasePanic.orderByChild("length");
                final Query userQuery = databasePanic.orderByChild("userID").equalTo(userID);
                userQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       // map.clear();
                        //Get the node from the datasnapshot
                        String myParentNode = dataSnapshot.getKey();
                        for (DataSnapshot child: dataSnapshot.getChildren())
                        {
                            String key = child.getKey().toString();
                            String value = child.getValue().toString();
                            //map.put(key,value);
                            System.out.println("key "+key+" Value "+value);
                        }

               /* databasePanic.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for(DataSnapshot panicSnapshot : dataSnapshot.getChildren()){
                            PanicButton p = panicSnapshot.getValue(PanicButton.class);
                            System.out.print("length of panic button pressed"+ p.getLength());
                        }

                        //String value = dataSnapshot.getValue(String.class);
                        //Log.d(TAG, "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });*/
            }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
           });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_btn:
                logOut();
                return true;

            case R.id.action_settings_btn:
                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
