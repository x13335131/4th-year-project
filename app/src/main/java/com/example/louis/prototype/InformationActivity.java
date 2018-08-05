package com.example.louis.prototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {


    private android.support.v7.widget.Toolbar mainToolbar;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Information");
        info = (TextView) findViewById(R.id.guide_tv);

        info.setMovementMethod(new ScrollingMovementMethod());
        info.setScrollbarFadingEnabled(false);
        info.setText(Html.fromHtml("The main objective of <i>My Mental Health Tracker</i> is to allow people with mental health illnesses" +
                " (particularly Anxiety and Depression) to document or keep a diary of their day to day activities and experiences as well as emotional wellbeing. " +
                " The idea is to capture these experiences and feelings to produce a visual outcome " +
                "where you the user can monitor yourself over a certain period of time. You can choose the how they want to view this data," +
                "  whether it be in a chart or text format and whether you want to view your progress over a week, month etc. <br/>" +
                "As well as documenting your own day to day experiences," +
                " you can also view live stats on other people around the world who are in similar situations as well as asking questions or" +
                " giving helpful tips/advice to other users. This is all done anonymously. No personal information is given out."+
                "<h3>Features</h3>"+
                "<h6>Diary</h6> " +
                "•\tFrom a list of emotions/moods, users can rate how they are feeling on an emotional level (eg. Angry, said, gloomy, depressed, anxious, insecure, tired etc) on a scale from 1 to 10.<br/>" +
                "•\tFrom a list of symptoms, select which symptoms (if any) that you are feeling. Symptoms include headaches, dizziness, joint pains, insomnia etc and again scale the severity of each.<br/>" +
                "•\tSide notes which may contain info on your day or medication you are on etc <br/> " +
                "<h6>Calendar</h6> " +
                "•\tFor every day of every month users can access and edit previous diary records for a specific day of the year as well as set reminders (ie appointments or for medication consumption)<br/>" +
                "•\tColour coded visualisation referring to the information a user entered into the diary for a specific day.  For example, if a user had a successful day, the box for that date will be bright green, if it was an “ok” day it would be orange and red for a bad day. <br/>" +
                "•\tKey consisting of different icons and their meaning. There will be icons or symbols that’ll appear on the calendar is the event of some of the following: sick, panic attack, hospital as well as what the different colours stand for.<br/>"+
                "<h6>Charts</h6>" +
                "Here users can track their own progress visually, see how they are doing, if they are improving or getting worse. Pinpoint where and when they feel bad (ie. What time of the day and where you were at that time) and maybe get a deeper understanding as to why. For example, for someone with depression, they may notice that every weekend or when they are out of work/college their mood starts to get worse and the graph will show a dip. As for someone with anxiety, they may notice it occurs every morning at 8am and every evening at 5pm, this may be down to rush hour and crowds. <br/>"+
                "<h6>Social</h6> " +
                "\u2022 Forum: <br/>" +
                "Communicate with other people in the same shoes as you and maybe even share some tips! <br/>" +
                "\u2022 Statistics: <br/>" +
                "Get live statistics of how people around the world are feeling." +
                "<h6>Panic Button</h6>" +
                "The purpose of this feature is if someone was having a panic attack they hit this button and the app will record when it was hit, how long it lasted (the panic attack), and locate where it happened (using the user’s location once enabled) "));
    }
    //return to previous activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
