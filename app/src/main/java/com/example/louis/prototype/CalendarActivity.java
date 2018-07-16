package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.widget.Toast;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    CompactCalendarView compactCalendarView;
   // private CalendarView mCalendarView;
    DatabaseReference panicAttackDb;
    FirebaseDatabase database;
    String userID;
    String date;
    String panicDate;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    long milliseconds;
    String loc="";
    String string_date;
    String formatedEventDate;
    Event event;
    Event currentEvent;
    int panicCount=0;
    int panicLength;
    String locLength="";
    boolean panicBool;
    boolean noteBool;
    private TextView displayDataTv;
    ArrayList<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //getting data from db
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //current user id~access specific info
        userID = currentFirebaseUser.getUid();
        displayDataTv = (TextView) findViewById(R.id.displayDataTv);
        displayDataTv.setText("");

        //referencing databases being used in graphs
        panicAttackDb = database.getReference("panic");

        //events
        events = new ArrayList<Event>();
        //action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        //setting views
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //getting panic dates from db
        getPanicDate();

        //when calendar is clicked
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                //testing what day is clicked
                System.out.println("Date clicked: "+dateClicked);
                //convert date clicked to string
                String clickedDateString = dateClicked.toString();

                //convert to Clicked date to correct format
                DateFormat clickedDateFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                Date clickedDate = null;
                try {
                    clickedDate = (Date)clickedDateFormatter.parse(clickedDateString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(clickedDate);
                int day = cal.get(Calendar.DATE);
                int month = cal.get(Calendar.MONTH)+1;
                int year = cal.get(Calendar.YEAR);

                String m;
                if(month<10){
                    m = "0"+month;
                }else{
                    m= Integer.toString(month);;
                }
                String d;
                if(day<10){
                    d = "0"+day;
                }else{
                    d= Integer.toString(day);;
                }
                String formatedDate = d+ "/" + m+ "/" +  year;

                //finished formatting clicked date to match date in db

                panicBool = false;
                noteBool=false;
                //iterating through array
                for(Event ev : events){

                    long t = ev.getTimeInMillis();

                    //convert back out of milliseconds
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(t);

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH)+1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    String mm;
                    if(mMonth<10){
                        mm = "0"+mMonth;
                    }else{
                        mm= Integer.toString(mMonth);;
                    }
                    String dd;
                    if(mDay<10){
                        dd = "0"+mDay;
                    }else{
                        dd= Integer.toString(mDay);
                    }
                    formatedEventDate = dd+ "/" + mm+ "/" +  mYear;
                    System.out.println("FORMATTED EVENT DATE: "+formatedEventDate);
                    System.out.println("FORMATTED CLICKED DATE "+formatedDate);

                    System.out.println("DATA FROM CURRENT EVENT UP: "+event.getData().toString());
                    //OUTPUT TO USER
                    //if date in events == to date clicked
                    if (formatedEventDate.equals(formatedDate)) {
                        System.out.println("IN IF FORMATTED EVENT DATE: "+formatedEventDate);
                        System.out.println("FORMATTED CLICKED DATE "+formatedDate);

                       panicBool = true;
                        System.out.println("BOOL "+panicBool);
                       panicCount=panicCount+1;
                        System.out.println("panic count "+panicCount);
                        System.out.println("TESTINGGGGG-getdata "+ ev.getData());
                        currentEvent = ev;
                    }

                }
               // System.out.println("size of array list"+events.size());
              //  System.out.println("panic boolean is: "+panicBool);
                    if(panicBool==true){

                        // Toast.makeText(context,  panicCount+" panic attacks(s)have been recorded. "+currentEvent.getData().toString(), Toast.LENGTH_LONG).show();
                        displayDataTv.setText(panicCount+" panic attacks(s) have been recorded.\n"+currentEvent.getData().toString());
                        panicBool=false;
                        panicCount=0;
                    }if(noteBool==true){
                    displayDataTv.setText("notes");
                    noteBool=false;
                }
                    else {
                         Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                         displayDataTv.setText("");
                    }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
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

    //*Methods*

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

                        if (key.equals("length")) {
                            String pl = child.getValue().toString();
                            panicLength = Integer.parseInt(pl);

                        }
                        if(key.equals("location")){
                            loc= child.getValue().toString();
                        }

                        if (key.equals("panicDate")){//&& value.contains(date)) {
                            panicDate= child.getValue().toString();

                            /*SETTING EVENTS*/
                            //setting event
                             string_date = panicDate;
                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            try {
                                //converting into milliseconds for event constructor
                                Date d = f.parse(string_date);
                                milliseconds = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                        else {
                            System.out.println("no matches ");
                        }

                        if(panicLength!=0 & !loc.isEmpty() & milliseconds!=0) {
                            System.out.println("Panic Date "+ panicDate+" Milliseconds:: " + milliseconds + " location " + loc + " length: "+panicLength);
                            locLength = loc+" Length: "+panicLength;
                            //System.out.println("locLength "+locLength);
                            event = new Event(Color.RED, milliseconds, locLength);
                            compactCalendarView.addEvent(event);
                            //adding event to array list of events
                            events.add(event);
                            loc="";
                            panicLength=0;
                            milliseconds=0;
                            panicLength=0;
                            System.out.println("size of arraylist " + events.size());
                            System.out.println("array to string " + events.toString());
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


    //return to previous activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
