package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Calendar extends AppCompatActivity {
    private TextView theDate;
    private CalendarView mCalendarView;
    DatabaseReference panicAttackDb;
    FirebaseDatabase database;
    String userID;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //current user id~access specific info
        userID = currentFirebaseUser.getUid();
        theDate = (TextView) findViewById(R.id.textView);

        //referencing databases being used in graphs
        panicAttackDb = database.getReference("panic");


        mCalendarView = (CalendarView) findViewById(R.id.calendarView);


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                int month= i1+1;
                date = i2 +"/"+"0"+month+"/"+i;
                theDate.setText(date);
                System.out.println("date: "+date);
                getPanicDate();
            }
        });
        //setting back btn to visible
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //floating home btn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

                                   @Override
                                   public void onClick(View v) { //when clicked return to home screen
                                       Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                       startActivity(mainIntent);
                                   }
                               }
        );

    }
    public void getPanicDate(){

        final Query panicQuery = panicAttackDb.orderByChild("userID").equalTo(userID);

        panicQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if (key.equals("panicDate")&& value.contains(date)) {
                            System.out.println("panic key " + key + " panic Value " + value);

                        } else {
                            System.out.println("no matches ");
                        }
                    }

                } catch(Exception e){
                    System.out.println(e);
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
    }

    //*Method*

    //return to previous activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
