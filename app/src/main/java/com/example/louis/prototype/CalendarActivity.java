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
    DatabaseReference panicAttackDb, notesDb, moodsDb, symptomsDb, medicationDb;
    FirebaseDatabase database;
    String userID, date, panicDate, noteDate, moodDate, symptomDate, medDate, note, med, medDosage, formatedEventDate, formattedCurrentNoteList, formattedCurrentPanicList, formattedCurrentMedList;
    String panicOutput = "";
    String noteOutput = "";
    String moodOutput = "";
    String symptomOutput = "";
    String medOutput = "";
    String loc = "";
    String locLength = "";
    String prevNoteDate = "00/00/00";
    String prevMoodDate = "00/00/00";
    String prevMedDate = "00/00/00";
    String prevSymptomDate = "00/00/00";
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    long milliseconds;
    Event event, noteEvent, otherNoteEvent, moodEvent, otherMoodEvent, symptomEvent, otherSymptomEvent, medEvent, otherMedEvent, currentEvent, currentNoteEvent, currentMoodEvent, currentSymptomEvent, currentMedEvent;
    int panicCount, noteCount, medCount = 0;
    int panicLength;
    boolean panicBool, noteBool, moodBool, symptomBool, medBool;
    private TextView displayDataTv;
    ArrayList<Event> panicEvents, noteEvents, moodEvents, symptomEvents, medEvents;
    ArrayList<String> moodList, panicList, symptomList, currentNoteList, currentPanicList, currentMedList, collectedDates;

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
        //panicEvents
        panicEvents = new ArrayList<Event>();
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

       /* Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy");
        String date = dateFormat.format(c.getTime());
        //action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
       // actionBar.setTitle(null);
        actionBar.setTitle(date);*/
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

        compactCalendarView.callOnClick();
        //when calendar is clicked
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                //testing what day is clicked
                System.out.println("Date clicked: " + dateClicked);
                //convert date clicked to string
                String clickedDateString = dateClicked.toString();

                //convert clicked date to correct format
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
                }
                String d;
                if (day < 10) {
                    d = "0" + day;
                } else {
                    d = Integer.toString(day);
                }
                String formatedDate = d + "/" + m + "/" + year;
                //finished correcting date to correct format.
                panicBool = false;
                noteBool = false;
                moodBool = false;
                symptomBool = false;
                medBool = false;

                /* iterating through each of the event arrays*/
                //**panicAttack events
                for (Event panicEv : panicEvents) {
                    long t = panicEv.getTimeInMillis();
                    formatDate(t);
                    if (formatedEventDate.equals(formatedDate)) {
                        panicBool = true;
                        panicCount = panicCount + 1;
                        currentEvent = panicEv;
                        currentPanicList.add(currentEvent.getData().toString());
                        formattedCurrentPanicList = currentPanicList.toString()
                                .replace("[", "<br/>\u2022")  //remove the left bracket
                                .replace("]", "")  //remove the right bracket
                                .replace(".,", "<br/>\u2022")  //remove commas
                                .trim();           //remove trailing spaces from partially initialized arrays
                    }
                }
                //**note events
                for (Event noteEv : noteEvents) {
                    long t = noteEv.getTimeInMillis();
                    formatDate(t);
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
                //**mood events
                for (Event moodEv : moodEvents) {
                    long t = moodEv.getTimeInMillis();
                    formatDate(t);
                    if (formatedEventDate.equals(formatedDate)) {
                        moodBool = true;
                        currentMoodEvent = moodEv;
                    }
                }
                //**symptom events
                for (Event symptomEv : symptomEvents) {
                    long t = symptomEv.getTimeInMillis();
                    formatDate(t);
                    if (formatedEventDate.equals(formatedDate)) {
                        symptomBool = true;
                        currentSymptomEvent = symptomEv;
                    }
                }
                //**medication events
                for (Event medEv : medEvents) {
                    long t = medEv.getTimeInMillis();
                    formatDate(t);
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
                //if any of the above have events for the clicked day create the strings for the event(s)
                if (panicBool == true || noteBool == true || moodBool == true || symptomBool == true || medBool == true) {

                    if (panicBool == true) {
                        panicOutput = "<br/>" + panicCount + " panic attack(s) have been recorded.<br/><br/>" + " <b>Location(s)</b> " + formattedCurrentPanicList;
                    }
                    if (noteBool == true) {
                        noteOutput = "<br/><b>Note(s):</b> " + noteCount + formattedCurrentNoteList;
                    }
                    if (moodBool == true) {
                        moodOutput = "<br/> <b>Mood(s):</b> " + currentMoodEvent.getData().toString();
                    }
                    if (symptomBool == true) {
                        symptomOutput = "<br/><b>Symptom(s):</b> " + currentSymptomEvent.getData().toString();
                    }
                    if (medBool == true) {
                        medOutput = "<br/><b>Med(s):</b> " + medCount + formattedCurrentMedList;
                    }
                    //displaying strings created to user
                    displayDataTv.setText(Html.fromHtml("<br/><b>Diary Entry for " + formatedDate + "</b> " + panicOutput + noteOutput + moodOutput + symptomOutput + medOutput));
                    resetValues(); //set values back to false
                } else {//if the date selected has no events do:
                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                    displayDataTv.setText("");
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) { //when moving through calendar months
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth)); //set action bar title to the month selected
            }
        });

        //setting back btn in action bar to visible
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

    //*********METHODS************

    //formatting date from milliseconds to day/month/year
    public void formatDate(long t) {
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
    }

    public void resetValues() {
        panicOutput = "";
        noteOutput = "";
        moodOutput = "";
        symptomOutput = "";
        medOutput = "";
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
    }

    /*RETRIEVING PANIC/NOTE/MOOD/SYMPTOM AND MEDICATION DATES FOR USER X */
    //retrieving dates to which a given user had a panic attack & adding to an array list of events which will then be displayed on calendar
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
                        }
                        //if each of the values arent null, create the event
                        if (panicLength != 0 & !loc.isEmpty() & milliseconds != 0) {
                            locLength = loc + " Length: " + panicLength + ".";
                            event = new Event(Color.RED, milliseconds, locLength);
                            compactCalendarView.addEvent(event);
                            panicEvents.add(event);
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

    //retrieving dates to which a given user wrote a note in the diary & adding to an array list of events which will then be displayed on calendar
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
                        }
                        //if note and date arent empty create event
                        if (!note.isEmpty() && milliseconds != 0) {
                            String subDate = noteDate.substring(0, 10);
                            //checking if any diary events are listed on the same date, if so make event icon transparent. only 1 blue icon per diary entry
                            if (!noteDate.contains(prevNoteDate) && !collectedDates.contains(subDate)) {
                                noteEvent = new Event(Color.BLUE, milliseconds, note);
                                compactCalendarView.addEvent(noteEvent);
                                //adding event to array list of panicEvents
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

    //retrieving dates to which a given user entered mood to diary & adding to an array list of events which will then be displayed on calendar
    public void getMoods() {
        final Query moodsQuery = moodsDb.orderByChild("userID").equalTo(userID);
        moodsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if(key.contains("Cb") && value.equals("true")){
                            key=key.replace("Cb", "");
                            moodList.add(key);
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
                        }
                        if (!moodList.isEmpty() && milliseconds != 0) {

                            String formattedMoodList = moodList.toString()
                                    .replace("[", "")  //remove the left bracket
                                    .replace("]", "")  //remove the right bracket
                                    .trim();           //remove trailing spaces from partially initialized arrays

                            String subDate = moodDate.substring(0, 10);
                            if (!moodDate.contains(prevMoodDate) && !collectedDates.toString().contains(subDate)) {
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

    //retrieving dates to which a user x added symptoms to diary & adding to an array list of events which will then be displayed on calendar
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
                        }
                        //gathering individual values
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
                            //checking if other events are on that date
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

    //retrieving dates to which user x added medication to diary entry & adding to an array list of events which will then be displayed on calendar
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
                        if ((med != "" && med != null) && (medDosage != "" && medDosage != null) && milliseconds != 0) {
                            String dateTime = medDate.substring(10, 16);//date part only
                            String medInfo = med + " Dosage: " + medDosage + " Time: " + dateTime;
                            String subDate = medDate.substring(0, 10);
                            //adding event whilst checking for other events for the selected date
                            if (!medDate.contains(prevMedDate) && !collectedDates.toString().contains(subDate)) {
                                medEvent = new Event(Color.BLUE, milliseconds, medInfo);
                                compactCalendarView.addEvent(medEvent);
                                medEvents.add(medEvent);
                                collectedDates.add(medDate);
                            } else {
                                otherMedEvent = new Event(Color.TRANSPARENT, milliseconds, medInfo);
                                medEvents.add(otherMedEvent);
                            }
                            prevMedDate = medDate.substring(0, 10);// getting date part only
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
