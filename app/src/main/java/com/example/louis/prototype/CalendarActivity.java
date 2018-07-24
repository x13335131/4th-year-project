package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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
    DatabaseReference notesDb;
    DatabaseReference moodsDb;
    DatabaseReference symptomsDb;
    DatabaseReference medicationDb;
    FirebaseDatabase database;
    String userID;
    String date;
    String panicDate;
    String noteDate;
    String moodDate;
    String symptomDate;
    String medDate;
    String note;
    String med;
    String medDosage;
    String moodAfraid;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    long milliseconds;
    String loc = "";
    String string_date;
    String formatedEventDate;
    Event event, noteEvent, otherNoteEvent, moodEvent, otherMoodEvent, symptomEvent, otherSymptomEvent, medEvent, otherMedEvent;
    Event currentEvent;
    Event currentNoteEvent;
    Event currentMoodEvent;
    Event currentSymptomEvent;
    Event currentMedEvent;
    int panicCount = 0;
    int noteCount = 0;
    int medCount = 0;
    // int moodCount=0;
    int panicLength;
    String locLength = "";
    boolean panicBool;
    boolean noteBool;
    boolean moodBool;
    boolean symptomBool;
    boolean medBool;
    String prevNoteDate = "00/00/00";
    String prevMoodDate = "00/00/00";
    String prevMedDate = "00/00/00";
    String prevSymptomDate = "00/00/00";
    private TextView displayDataTv;
    ArrayList<Event> events;
    ArrayList<Event> noteEvents;
    ArrayList<Event> moodEvents;
    ArrayList<Event> symptomEvents;
    ArrayList<Event> medEvents;
    ArrayList<String> moodList;
    ArrayList<String> panicList;
    ArrayList<String> symptomList;
    ArrayList<String> currentNoteList;
    ArrayList<String> currentPanicList;
    ArrayList<String> currentMedList;
    ArrayList<String> collectedDates;
    String formattedCurrentNoteList;
    String formattedCurrentPanicList;
    String formattedCurrentMedList;

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
        displayDataTv.setMovementMethod(new ScrollingMovementMethod());
        displayDataTv.setScrollbarFadingEnabled(false);

        //referencing databases being used in graphs
        panicAttackDb = database.getReference("panic");
        notesDb = database.getReference("notes");
        moodsDb = database.getReference("moods");
        symptomsDb = database.getReference("symptoms");
        medicationDb = database.getReference("meds");
        //events
        events = new ArrayList<Event>();
        noteEvents = new ArrayList<Event>();
        moodEvents = new ArrayList<Event>();
        symptomEvents = new ArrayList<Event>();
        medEvents = new ArrayList<Event>();


        moodList = new ArrayList<String>();
        panicList = new ArrayList<String>();
        symptomList = new ArrayList<String>();
        currentNoteList = new ArrayList<String>();
        currentPanicList = new ArrayList<String>();
        currentMedList = new ArrayList<String>();
        collectedDates = new ArrayList<String>();

        //action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        //setting views
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //getting panic dates from db
        getPanicDate();
        getNotes();
        getMoods();
        getSymptoms();
        getMedication();

        //when calendar is clicked
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                //testing what day is clicked
                System.out.println("Date clicked: " + dateClicked);
                //convert date clicked to string
                String clickedDateString = dateClicked.toString();

                //convert to Clicked date to correct format
                DateFormat clickedDateFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                Date clickedDate = null;
                try {
                    clickedDate = (Date) clickedDateFormatter.parse(clickedDateString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(clickedDate);
                int day = cal.get(Calendar.DATE);
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);

                String m;
                if (month < 10) {
                    m = "0" + month;
                } else {
                    m = Integer.toString(month);
                    ;
                }
                String d;
                if (day < 10) {
                    d = "0" + day;
                } else {
                    d = Integer.toString(day);
                    ;
                }
                String formatedDate = d + "/" + m + "/" + year;

                //finished formatting clicked date to match date in db

                panicBool = false;
                noteBool = false;
                moodBool = false;
                symptomBool = false;
                medBool = false;
                //iterating through array
                for (Event ev : events) {

                    long t = ev.getTimeInMillis();

                    //convert back out of milliseconds
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(t);

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH) + 1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    String mm;
                    if (mMonth < 10) {
                        mm = "0" + mMonth;
                    } else {
                        mm = Integer.toString(mMonth);
                        ;
                    }
                    String dd;
                    if (mDay < 10) {
                        dd = "0" + mDay;
                    } else {
                        dd = Integer.toString(mDay);
                    }
                    formatedEventDate = dd + "/" + mm + "/" + mYear;
                    //OUTPUT TO USER
                    //if date in events == to date clicked
                    if (formatedEventDate.equals(formatedDate)) {
                        panicBool = true;
                        panicCount = panicCount + 1;
                        currentEvent = ev;
                        currentPanicList.add(currentEvent.getData().toString());
                        formattedCurrentPanicList = currentPanicList.toString()
                                .replace("[", "<br/>\u2022")  //remove the left bracket
                                .replace("]", "")  //remove the right bracket
                                .replace(".,", "<br/>\u2022")  //remove commas
                                .trim();           //remove trailing spaces from partially initialized arrays
                    }
                }

                for (Event noteEv : noteEvents) {
                    long t = noteEv.getTimeInMillis();
                    //convert back out of milliseconds
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(t);

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH) + 1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    String mm;
                    if (mMonth < 10) {
                        mm = "0" + mMonth;
                    } else {
                        mm = Integer.toString(mMonth);
                    }
                    String dd;
                    if (mDay < 10) {
                        dd = "0" + mDay;
                    } else {
                        dd = Integer.toString(mDay);
                    }
                    formatedEventDate = dd + "/" + mm + "/" + mYear;
                    if (formatedEventDate.equals(formatedDate)) {
                        noteBool = true;
                        noteCount = noteCount + 1;
                        currentNoteEvent = noteEv;
                        currentNoteList.add(currentNoteEvent.getData().toString());
                        formattedCurrentNoteList = currentNoteList.toString()
                                .replace("[", "<br/>\u2022")  //remove the left bracket
                                .replace("]", "")  //remove the right bracket
                                .replace(",", "<br/>\u2022")  //remove commas
                                .trim();           //remove trailing spaces from partially initialized arrays
                    }
                }

                for (Event moodEv : moodEvents) {
                    long t = moodEv.getTimeInMillis();
                    //convert back out of milliseconds
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(t);

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH) + 1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    String mm;
                    if (mMonth < 10) {
                        mm = "0" + mMonth;
                    } else {
                        mm = Integer.toString(mMonth);
                    }
                    String dd;
                    if (mDay < 10) {
                        dd = "0" + mDay;
                    } else {
                        dd = Integer.toString(mDay);
                    }
                    formatedEventDate = dd + "/" + mm + "/" + mYear;
                    if (formatedEventDate.equals(formatedDate)) {
                        moodBool = true;
                        currentMoodEvent = moodEv;
                    }
                }

                for (Event symptomEv : symptomEvents) {
                    long t = symptomEv.getTimeInMillis();

                    //convert back out of milliseconds
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(t);

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH) + 1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    String mm;
                    if (mMonth < 10) {
                        mm = "0" + mMonth;
                    } else {
                        mm = Integer.toString(mMonth);
                    }
                    String dd;
                    if (mDay < 10) {
                        dd = "0" + mDay;
                    } else {
                        dd = Integer.toString(mDay);
                    }
                    formatedEventDate = dd + "/" + mm + "/" + mYear;
                    if (formatedEventDate.equals(formatedDate)) {
                        symptomBool = true;
                        currentSymptomEvent = symptomEv;
                    }

                }

                for (Event medEv : medEvents) {
                    long t = medEv.getTimeInMillis();
                    //convert back out of milliseconds
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(t);

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH) + 1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    String mm;
                    if (mMonth < 10) {
                        mm = "0" + mMonth;
                    } else {
                        mm = Integer.toString(mMonth);
                    }
                    String dd;
                    if (mDay < 10) {
                        dd = "0" + mDay;
                    } else {
                        dd = Integer.toString(mDay);
                    }
                    formatedEventDate = dd + "/" + mm + "/" + mYear;
                    if (formatedEventDate.equals(formatedDate)) {
                        medBool = true;
                        medCount = medCount + 1;
                        currentMedEvent = medEv;
                        currentMedList.add(currentMedEvent.getData().toString());
                        formattedCurrentMedList = currentMedList.toString()
                                .replace("[", "<br/>\u2022")  //remove the left bracket
                                .replace("]", "")  //remove the right bracket
                                .replace(",", "<br/>\u2022")  //remove commas
                                .trim();           //remove trailing spaces from partially initialized arrays
                    }
                }

                if (panicBool == true && noteBool == true && moodBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    noteBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentNoteList.clear();
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && noteBool == true && moodBool == true && symptomBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData()));
                    panicBool = false;
                    noteBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    currentNoteList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && noteBool == true && moodBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    noteBool = false;
                    moodBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentMedList.clear();
                    ;
                    currentNoteList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && noteBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    noteBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentNoteList.clear();
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && moodBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    panicCount = 0;
                    medCount = 0;
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (noteBool == true && moodBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    noteBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    medCount = 0;
                    currentNoteList.clear();
                    currentMedList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && noteBool == true && moodBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString()));
                    panicBool = false;
                    noteBool = false;
                    panicCount = 0;
                    noteCount = 0;

                    currentNoteList.clear();
                    currentPanicList.clear();

                } else if (panicBool == true && noteBool == true && symptomBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/> <b>Symptoms:</b> " + currentSymptomEvent.getData()));
                    panicBool = false;
                    noteBool = false;
                    symptomBool = false;
                    panicCount = 0;
                    noteCount = 0;

                    currentNoteList.clear();
                    currentPanicList.clear();

                } else if (panicBool == true && moodBool == true && symptomBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + " <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> " + currentSymptomEvent.getData()));
                    panicBool = false;
                    moodBool = false;
                    symptomBool = false;
                    panicCount = 0;
                    noteCount = 0;

                    currentPanicList.clear();

                } else if (panicBool == true && noteBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    noteBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && moodBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    panicCount = 0;
                    medCount = 0;
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (panicBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    noteBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentNoteList.clear();
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (moodBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentMedList.clear();
                    currentPanicList.clear();
                    //   moodCount=0;
                } else if (noteBool == true && symptomBool == true && medBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount
                            + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> <br/> " + currentSymptomEvent.getData() + "<br/><br/><b>Medication:</b> " + medCount
                            + "<br/>" + formattedCurrentMedList));
                    panicBool = false;
                    noteBool = false;
                    moodBool = false;
                    symptomBool = false;
                    medBool = false;
                    noteCount = 0;
                    panicCount = 0;
                    medCount = 0;
                    currentNoteList.clear();
                    currentMedList.clear();
                    currentPanicList.clear();
                } else if (noteBool == true && moodBool == true && symptomBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"
                            + "<b>Note(s):</b> " + noteCount + "<br/>" + formattedCurrentNoteList + " <br/>  <br/><b>Mood(s):</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b><br/> " + currentSymptomEvent.getData()));
                    noteBool = false;
                    moodBool = false;
                    symptomBool = false;
                    panicCount = 0;
                    noteCount = 0;
                    currentNoteList.clear();

                } else if (panicBool == true && noteBool == true) {

                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + panicCount + " panic attack(s) have been recorded. <br/><br/> <b>Location:</b><br/> " + formattedCurrentPanicList + "<br/><br/><b>Note(s):</b> " + noteCount + "<br/>" + formattedCurrentNoteList));
                    panicBool = false;
                    noteBool = false;
                    panicCount = 0;
                    noteCount = 0;
                    currentNoteList.clear();
                    currentPanicList.clear();

                } else if (panicBool == true && moodBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + panicCount + " panic attack(s) have been recorded.<br/><br/> <b>Location:</b> " + currentEvent.getData().toString() + "<br/><br/> <b>Mood(s):</b> " + currentMoodEvent.getData().toString()));
                    panicBool = false;
                    moodBool = false;
                    panicCount = 0;
                    currentPanicList.clear();

                } else if (panicBool == true && symptomBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + panicCount + " panic attack(s) have been recorded.<br/><br/> <b>Location:</b> " + currentEvent.getData().toString() + " <br/> <b>Symptoms:</b> " + currentSymptomEvent.getData()));
                    panicBool = false;
                    symptomBool = false;
                    panicCount = 0;
                    currentPanicList.clear();

                } else if (noteBool == true && moodBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>"  + "<b> Note(s):</b> " + noteCount + formattedCurrentNoteList + "<br/> <br/> <b>Mood(s)" + ":</b> " + currentMoodEvent.getData().toString()));
                    noteBool = false;
                    moodBool = false;
                    noteCount = 0;
                    currentNoteList.clear();

                } else if (noteBool == true && symptomBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<b> Note(s):</b> " + noteCount + formattedCurrentNoteList + " <br/> <b>Symptoms:</b> " + currentSymptomEvent.getData()));
                    noteBool = false;
                    symptomBool = false;
                    noteCount = 0;
                    currentNoteList.clear();

                } else if (moodBool == true && symptomBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<br/> <br/> <b>Mood(s)" + ":</b> " + currentMoodEvent.getData().toString() + " <br/> <b>Symptoms:</b> " + currentSymptomEvent.getData()));
                    moodBool = false;
                    symptomBool = false;
                } else if (noteBool == true && medBool == true) {
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<b>Note(s):</b> " + noteCount + formattedCurrentNoteList + "<br/> <b>Med(s):</b> " + medCount + formattedCurrentMedList));
                    noteBool = false;
                    medBool = false;
                    noteCount = 0;
                    medCount = 0;
                    currentNoteList.clear();
                    currentMedList.clear();
                } else if (panicBool == true || noteBool == true || moodBool == true || symptomBool == true || medBool) {

                    if (panicBool == true) {
                        displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + panicCount + " panic attacks(s) have been recorded.<br/><br/>" + " <b>Location</b> " + currentEvent.getData().toString()));
                        panicBool = false;
                        panicCount = 0;
                    }
                    if (noteBool == true) {
                        displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<b>Note(s):</b> " + noteCount + formattedCurrentNoteList));
                        noteBool = false;
                        noteCount = 0;
                        currentNoteList.clear();
                    }
                    if (moodBool == true) {
                        displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<b>Mood(s):</b> " + currentMoodEvent.getData().toString()));
                        moodBool = false;
                    }
                    if (symptomBool == true) {
                        displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<b>Symptom(s):</b> " + currentSymptomEvent.getData().toString()));
                        symptomBool = false;
                    }
                    if (medBool == true) {
                        displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> <br/><br/>" + "<b>Med(s):</b> " + medCount + formattedCurrentMedList));
                        medBool = false;
                        medCount = 0;
                        currentMedList.clear();
                    }
                } else {
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
        fab.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) { //when clicked return to home screen
                                       Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                       startActivity(mainIntent);
                                   }
                               }
        );

    }

    //*Methods*

    public void getPanicDate() {
        final Query panicQuery = panicAttackDb.orderByChild("userID").equalTo(userID);
        panicQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if (key.equals("length")) {
                            String pl = child.getValue().toString();
                            panicLength = Integer.parseInt(pl);

                        }
                        if (key.equals("location")) {
                            loc = child.getValue().toString();
                        }

                        if (key.equals("panicDate")) {//&& value.contains(date)) {
                            panicDate = child.getValue().toString();
                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            try {
                                Date d = f.parse(panicDate);
                                milliseconds = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("no matches ");
                        }

                        if (panicLength != 0 & !loc.isEmpty() & milliseconds != 0) {
                            locLength = loc + " Length: " + panicLength + ".";
                            event = new Event(Color.RED, milliseconds, locLength);
                            compactCalendarView.addEvent(event);
                            events.add(event);
                            loc = "";
                            panicLength = 0;
                            milliseconds = 0;
                            panicLength = 0;
                        }
                    }

                } catch (Exception e) {
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

    public void getNotes() {
        final Query notesQuery = notesDb.orderByChild("userID").equalTo(userID);
        notesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if (key.equals("note")) {
                            note = child.getValue().toString();
                        }
                        if (key.equals("noteDate")) {//&& value.contains(date)) {
                            noteDate = child.getValue().toString();
                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            try {
                                //converting into milliseconds for event constructor
                                Date d = f.parse(noteDate);
                                milliseconds = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("no matches ");
                        }

                        if (!note.isEmpty() && milliseconds != 0) {
                            String subDate = noteDate.substring(0, 10);
                            if (!noteDate.contains(prevNoteDate) && !collectedDates.contains(subDate)) {
                                System.out.println("IN THE BLUE");
                                noteEvent = new Event(Color.BLUE, milliseconds, note);
                                compactCalendarView.addEvent(noteEvent);
                                //adding event to array list of events
                                noteEvents.add(noteEvent);
                                collectedDates.add(noteDate);
                            } else {
                                otherNoteEvent = new Event(Color.TRANSPARENT, milliseconds, note);
                                noteEvents.add(otherNoteEvent);
                            }
                            prevNoteDate = noteDate.substring(0, 10);//date part only
                            note = "";
                            milliseconds = 0;
                        }
                    }

                } catch (Exception e) {
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

    public void getMoods() {
        final Query moodsQuery = moodsDb.orderByChild("userID").equalTo(userID);
        moodsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if (key.equals("afraidCb") && value.equals("true")) {
                            moodList.add("afraid");
                        }
                        if (key.equals("aggrevatedCb") && value.equals("true")) {
                            moodList.add("aggrevated");
                        }
                        if (key.equals("angryCb") && value.equals("true")) {
                            moodList.add("angry");
                        }
                        if (key.equals("anxiousCb") && value.equals("true")) {
                            moodList.add("anxious");
                        }
                        if (key.equals("awkwardCb") && value.equals("true")) {
                            moodList.add("awkward");
                        }
                        if (key.equals("braveCb") && value.equals("true")) {
                            moodList.add("brave");
                        }
                        if (key.equals("calmCb") && value.equals("true")) {
                            moodList.add("calm");
                        }
                        if (key.equals("confidentCb") && value.equals("true")) {
                            moodList.add("confident");
                        }
                        if (key.equals("contentCb") && value.equals("true")) {
                            moodList.add("content");
                        }
                        if (key.equals("depressedCb") && value.equals("true")) {
                            moodList.add("depressed");
                        }
                        if (key.equals("discouragedCb") && value.equals("true")) {
                            moodList.add("discouraged");
                        }
                        if (key.equals("distantCb") && value.equals("true")) {
                            moodList.add("distant");
                        }
                        if (key.equals("energizedCb") && value.equals("true")) {
                            moodList.add("energized");
                        }
                        if (key.equals("fatiguedCb") && value.equals("true")) {
                            moodList.add("fatigued");
                        }
                        if (key.equals("gloomyCb") && value.equals("true")) {
                            moodList.add("gloomy");
                        }
                        if (key.equals("grumpyCb") && value.equals("true")) {
                            moodList.add("grumpy");
                        }
                        if (key.equals("grouchyCb") && value.equals("true")) {
                            moodList.add("grouchy");
                        }
                        if (key.equals("happyCb") && value.equals("true")) {
                            moodList.add("happy");
                        }
                        if (key.equals("hesitantCb") && value.equals("true")) {
                            moodList.add("hesitant");
                        }
                        if (key.equals("impatientCb") && value.equals("true")) {
                            moodList.add("impatient");
                        }
                        if (key.equals("insecureCb") && value.equals("true")) {
                            moodList.add("insecure");
                        }
                        if (key.equals("symptomDate")) {//&& value.contains(date)) {
                            moodDate = child.getValue().toString();
                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            try {
                                //converting into milliseconds for event constructor
                                Date d = f.parse(moodDate);
                                milliseconds = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (moodList.isEmpty()) {
                                milliseconds = 0;
                            }
                        } else {
                            System.out.println("no matches ");
                        }
                        if (!moodList.isEmpty() && milliseconds != 0) {

                            String formattedMoodList = moodList.toString()
                                    .replace("[", "")  //remove the left bracket
                                    .replace("]", "")  //remove the right bracket
                                    .trim();           //remove trailing spaces from partially initialized arrays

                            String subDate = moodDate.substring(0, 10);
                            if (!moodDate.contains(prevMoodDate) && !collectedDates.toString().contains(subDate)) {
                                System.out.println("IN THE MOOD GREEN");
                                moodEvent = new Event(Color.BLUE, milliseconds, formattedMoodList);
                                compactCalendarView.addEvent(moodEvent);
                                moodEvents.add(moodEvent);
                                collectedDates.add(moodDate);
                            } else {
                                otherMoodEvent = new Event(Color.TRANSPARENT, milliseconds, formattedMoodList);
                                moodEvents.add(otherMoodEvent);
                            }
                            prevMoodDate = moodDate.substring(0, 10);//date part only
                            milliseconds = 0;
                            moodList.clear();
                        }
                    }
                } catch (Exception e) {
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

    public void getSymptoms() {
        final Query symptomsQuery = symptomsDb.orderByChild("userID").equalTo(userID);
        symptomsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();
                        if (key.equals("symptomDate")) { //&& value.contains(date)) {
                            symptomDate = child.getValue().toString();
                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            try {
                                //converting into milliseconds for event constructor
                                Date d = f.parse(symptomDate);
                                milliseconds = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("no matches ");
                        }
                        if (key.equals("value1") && !value.equals("0")) {
                            symptomList.add("acne: " + value + "/10");

                        }
                        if (key.equals("value2") && !value.equals("0")) {
                            symptomList.add("bloating: " + value + "/10");
                        }
                        if (key.equals("value3") && !value.equals("0")) {
                            symptomList.add("cramps: " + value + "/10");
                        }
                        if (key.equals("value4") && !value.equals("0")) {
                            symptomList.add("dizziness: " + value + "/10");
                        }
                        if (key.equals("value5") && !value.equals("0")) {
                            symptomList.add("spots: " + value + "/10");
                        }
                        if (key.equals("value6") && !value.equals("0")) {
                            symptomList.add("headache: " + value + "/10");
                        }
                        if (key.equals("value7") && !value.equals("0")) {
                            symptomList.add("insomnia: " + value + "/10");
                        }
                        if (key.equals("value8") && !value.equals("0")) {
                            symptomList.add("sweating: " + value + "/10");
                        }
                        if (symptomList.isEmpty() && key.equals("value8")) {
                            milliseconds = 0;
                        }
                        if (!symptomList.isEmpty() && milliseconds != 0 && key.equals("value8")) {
                            String formattedSymptomList = symptomList.toString()
                                    .replace("[", "\u2022")  //remove the left bracket
                                    .replace("]", "")  //remove the right bracket
                                    .replace(",", "<br/>\u2022")  //remove commas
                                    .trim();           //remove trailing spaces from partially initialized arrays
                            String subDate = symptomDate.substring(0, 10);
                            if (!symptomDate.contains(prevSymptomDate) && !collectedDates.toString().contains(subDate)) {
                                symptomEvent = new Event(Color.BLUE, milliseconds, formattedSymptomList);
                                compactCalendarView.addEvent(symptomEvent);
                                symptomEvents.add(symptomEvent);
                                collectedDates.add(medDate);
                            } else {
                                otherSymptomEvent = new Event(Color.TRANSPARENT, milliseconds, formattedSymptomList);
                                symptomEvents.add(otherSymptomEvent);
                            }
                            prevSymptomDate = symptomDate.substring(0, 10);//date part only
                            milliseconds = 0;
                            symptomList.clear();
                        }
                    }

                } catch (Exception e) {
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

    public void getMedication() {
        final Query medQuery = medicationDb.orderByChild("userID").equalTo(userID);
        medQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if (key.equals("medDate")) { //&& value.contains(date)) {
                            medDate = child.getValue().toString();
                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            try {
                                //converting into milliseconds for event constructor
                                Date d = f.parse(medDate);
                                milliseconds = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (key.equals("medDosage")) {
                            medDosage = child.getValue().toString();
                        }
                        if (key.equals("medName")) {
                            med = child.getValue().toString();
                        }

                        System.out.println("MED: " + med + " med dosage: " + medDosage + " milliseconds: " + milliseconds);
                        if ((med != "" && med != null) && (medDosage != "" && medDosage != null) && milliseconds != 0) {
                            String dateTime = medDate.substring(10, 16);//date part only
                            String medInfo = med + " Dosage: " + medDosage + " Time: " + dateTime;
                            String subDate = medDate.substring(0, 10);
                            if (!medDate.contains(prevMedDate) && !collectedDates.toString().contains(subDate)) {
                                medEvent = new Event(Color.BLUE, milliseconds, medInfo);
                                compactCalendarView.addEvent(medEvent);
                                medEvents.add(medEvent);
                                collectedDates.add(medDate);
                            } else {
                                otherMedEvent = new Event(Color.TRANSPARENT, milliseconds, medInfo);
                                medEvents.add(otherMedEvent);
                            }
                            prevMedDate = medDate.substring(0, 10);//date part only
                            med = "";
                            medDosage = "";
                            milliseconds = 0;
                        }
                    }

                } catch (Exception e) {
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
