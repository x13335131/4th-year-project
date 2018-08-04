package com.example.louis.prototype;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mainToolbar;
    DatabaseReference panicAttackDb, moodsDb;
    FirebaseDatabase database;
    String todaysDate;
    TextView displayStatsTv;
    Thread thread;
    int todaysPanicCount, countryPanicCount, todayCountryPanicCount;
    private double latti, longi;
    private String userID, value, latitude, longitude, address, location, panicText, userCountryName, userAddress, postelCode, countryCode;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addresses;

    int afraidCbCount = 0;
    int aggrevatedCbCount = 0;
    int angryCbCount = 0;
    int anxiousCbCount = 0;
    int awkwardCbCount = 0;
    int braveCbCount = 0;
    int calmCbCount = 0;
    int confidentCbCount = 0;
    int contentCbCount = 0;
    int depressedCbCount = 0;
    int discouragedCbCount = 0;
    int distantCbCount = 0;
    int energizedCbCount = 0;
    int fatiguedCbCount = 0;
    int gloomyCbCount = 0;
    int grumpyCbCount = 0;
    int grouchyCbCount =0;
    int happyCbCount = 0;
    int hesitantCbCount =0;
    int impatientCbCount=0;
    int insecureCbCount =0;

    int percentageAfraid;
    int percentageAggrevated;
    int percentageAngry;
    int percentageAnxious;
    int percentageAwkward;
    int  percentageBrave;
    int  percentageCalm;
    int  percentageConfident;
    int  percentageContent;
    int percentageDepressed;
    int percentageDiscouraged;
    int percentageDistant;
    int percentageEnergized;
    int percentageFatigued;
    int percentageGloomy;
    int percentageGrumpy;
    int percentageGrouchy;
    int percentageHappy;
    int percentageHesitant;
    int percentageImpatient;
    int  percentageInsecure;

    boolean isAfraid;
    boolean isAggrevated;
    boolean isAngry;
    boolean isAnxious;
    boolean isAwkward;
    boolean  isBrave;
    boolean  isCalm;
    boolean  isConfident;
    boolean  isContent;
    boolean isDepressed;
    boolean isDiscouraged;
    boolean isDistant;
    boolean isEnergized;
    boolean isFatigued;
    boolean isGloomy;
    boolean isGrumpy;
    boolean isGrouchy;
    boolean isHappy;
    boolean isHesitant;
    boolean isImpatient;
    boolean  isInsecure;


    boolean morning = false;
    boolean  afternoon= false;
    boolean evening = false;
    boolean night= false;

    int morningCount=0;
    int afternoonCount=0;
    int eveningCount=0;
    int nightCount=0;

    int panicCountThisYear=0;

int childCount=0;
String moodString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        geocoder = new Geocoder(StatsActivity.this, Locale.getDefault());
        //referencing database
        database = FirebaseDatabase.getInstance();
        panicAttackDb = database.getReference("panic");
        moodsDb = database.getReference("moods");
        todaysPanicCount = 0;

        displayStatsTv = (TextView) findViewById(R.id.statsDisplayTv);
        displayStatsTv.setMovementMethod(new ScrollingMovementMethod());
        displayStatsTv.setScrollbarFadingEnabled(false);
        getCurrentLocation();
        getPanicAttacks();
        getMoodCount();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       Intent i8 = new Intent(getApplicationContext(), MainActivity.class);
                                       startActivity(i8);
                                   }
                               }
        );
       thread = new Thread() {

            @Override
            public void run() {

                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            System.out.println("displaying text..");
                            displayStatsTv.setText(Html.fromHtml("<h2>Panic Attacks</h2> "+panicText+ "<br /> The total number of panic attacks today in <i>"+userCountryName+"</i> is "+todayCountryPanicCount
                                    +"<br /> <h5>~~This year~~</h5> "
                                    +"Total panic attacks as of yet "+panicCountThisYear+
                                    "<br /> Total morning panic attacks: "+morningCount+
                                    "<br /> Total afternoon panic attacks: "+afternoonCount+"<br /> Total evening panic attacks: "+eveningCount+
                                    "<br /> Total night panic attacks: "+nightCount+"<br/><h2>Moods</h2>"
                                    +moodString));

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            System.out.println("sleeping...");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.start();
    }

    private void getMoodCount() {
          /*Getting current date*/
        Calendar cal = Calendar.getInstance();
        cal.getTime();
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
        final String todaysDate = d + "/" + m + "/" + year;
        System.out.println("todays date: "+todaysDate);

        //getting panic attacks for today
        final Query moodQuery = moodsDb.orderByChild("symptomDate");

        moodQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                resetValues();
                 try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if (key.equals("afraidCb") && value.equals("true")) {
                            isAfraid=true;
                        }
                        if (key.equals("aggrevatedCb") && value.equals("true")) {
                            isAggrevated=true;
                        }
                        if (key.equals("angryCb") && value.equals("true")) {
                            isAngry=true;
                        }
                        if (key.equals("anxiousCb") && value.equals("true")) {
                            isAnxious=true;
                        }
                        if (key.equals("awkwardCb") && value.equals("true")) {
                             isAwkward=true;
                        }
                        if (key.equals("braveCb") && value.equals("true")) {
                            isBrave=true;
                        }
                        if (key.equals("calmCb") && value.equals("true")) {
                            isCalm=true;
                        }
                        if(key.equals("confidentCb")&& value.equals("true")) {
                            isConfident=true;
                        }
                        if(key.equals("contentCb")&& value.equals("true")) {
                            isContent=true;
                        }
                        if(key.equals("depressedCb")&& value.equals("true")) {
                            isDepressed=true;
                        }
                        if(key.equals("discouragedCb")&& value.equals("true")) {
                            isDiscouraged=true;
                        }
                        if(key.equals("distantCb")&& value.equals("true")) {
                            isDistant=true;
                        }
                        if(key.equals("energizedCb")&& value.equals("true")) {
                            isEnergized=true;
                        }
                        if(key.equals("fatiguedCb")&& value.equals("true")) {
                            isFatigued=true;
                        }
                        if(key.equals("gloomyCb")&& value.equals("true")) {
                            isGloomy=true;
                        }
                        if(key.equals("grumpyCb")&& value.equals("true")) {
                            isGrumpy=true;
                        }
                        if(key.equals("grouchyCb")&& value.equals("true")) {
                            isGrouchy=true;
                        }
                        if(key.equals("happyCb")&& value.equals("true")) {
                            isHappy=true;
                        }
                        if(key.equals("hesitantCb")&& value.equals("true")) {
                            isHesitant=true;
                        }
                        if(key.equals("impatientCb")&& value.equals("true")) {
                            isImpatient=true;
                        }
                        if(key.equals("insecureCb")&& value.equals("true")) {
                           isInsecure=true;
                        }
                        if(key.equals("symptomDate") && value.contains(todaysDate)){
                            childCount= childCount+1;
                            if (isAfraid==true) {
                                afraidCbCount = afraidCbCount + 1;
                            }
                            if (isAggrevated==true) {
                                aggrevatedCbCount = aggrevatedCbCount + 1;
                            }
                            if (isAngry==true) {
                                angryCbCount = angryCbCount + 1;
                            }
                            if (isAnxious==true) {
                                anxiousCbCount = anxiousCbCount + 1;
                            }
                            if (isAwkward==true) {
                                awkwardCbCount = awkwardCbCount + 1;
                            }
                            if (isBrave==true) {
                                braveCbCount = braveCbCount + 1;
                            }
                            if (isCalm==true) {
                                calmCbCount = calmCbCount + 1;
                            }
                            if(isConfident==true) {
                                confidentCbCount = confidentCbCount + 1;
                            }
                            if(isContent==true) {
                                contentCbCount = contentCbCount + 1;
                            }
                            if(isDepressed==true) {
                                depressedCbCount = depressedCbCount + 1;
                            }
                            if(isDiscouraged==true) {
                                discouragedCbCount = discouragedCbCount + 1;
                            }
                            if(isDistant==true) {
                                distantCbCount = distantCbCount + 1;
                            }
                            if(isEnergized==true) {
                                energizedCbCount = energizedCbCount + 1;
                            }
                            if(isFatigued==true) {
                                fatiguedCbCount = fatiguedCbCount + 1;
                            }
                            if(isGloomy==true) {
                                gloomyCbCount = gloomyCbCount + 1;
                            }
                            if(isGrumpy==true) {
                                grumpyCbCount = grumpyCbCount + 1;
                            }
                            if(isGrouchy==true) {
                                grouchyCbCount = grouchyCbCount + 1;
                            }
                            if(isHappy==true) {
                                happyCbCount = happyCbCount + 1;
                            }
                            if(isHesitant==true) {
                                hesitantCbCount = hesitantCbCount + 1;
                            }
                            if(isImpatient==true) {
                                impatientCbCount = impatientCbCount + 1;
                            }
                            if(isInsecure==true) {
                                insecureCbCount = insecureCbCount + 1;
                            }
                        }

                    }
                     percentageAfraid = (int) ((afraidCbCount * 100) / childCount);
                     System.out.println("checkbox 1: " + afraidCbCount + " percentage: " + percentageAfraid);

                     percentageAggrevated = (int) ((aggrevatedCbCount * 100) / childCount);
                     System.out.println("checkbox 2: " + aggrevatedCbCount + " percentage: " + percentageAggrevated);

                     percentageAngry = (int) ((angryCbCount * 100) / childCount);
                     System.out.println("checkbox 3: " + angryCbCount + " percentage: " + percentageAngry);

                     percentageAnxious = (int) ((anxiousCbCount * 100) / childCount);
                     System.out.println("checkbox 4: " + anxiousCbCount + " percentage: " + percentageAnxious);

                     percentageAwkward = (int) ((awkwardCbCount * 100) / childCount);
                     System.out.println("checkbox 5: " + awkwardCbCount + " percentage: " + percentageAwkward);

                     percentageBrave = (int) ((braveCbCount * 100) / childCount);
                     System.out.println("checkbox 6: " + braveCbCount + " percentage: " + percentageBrave);

                     percentageCalm = (int) ((calmCbCount * 100) / childCount);
                     System.out.println("checkbox 7: " + calmCbCount + " percentage: " + percentageCalm);

                     percentageConfident = (int) ((confidentCbCount * 100) / childCount);
                     System.out.println("checkbox 8: " + confidentCbCount + " percentage: " + percentageConfident);

                     percentageContent = (int) ((contentCbCount * 100) / childCount);
                     System.out.println("checkbox 9: " + contentCbCount + " percentage: " + percentageContent);

                     percentageDepressed = (int) ((depressedCbCount * 100) / childCount);
                     System.out.println("checkbox 10: " + depressedCbCount + " percentage: " + percentageDepressed);

                     percentageDiscouraged = (int) ((discouragedCbCount * 100) / childCount);
                     System.out.println("checkbox 11: " + discouragedCbCount + " percentage: " + percentageDiscouraged);

                     percentageDistant = (int) ((distantCbCount * 100) / childCount);
                     System.out.println("checkbox 12: " + distantCbCount + " percentage: " + percentageDistant);

                     percentageEnergized = (int) ((energizedCbCount * 100) / childCount);
                     System.out.println("checkbox 13: " + energizedCbCount + " percentage: " + percentageEnergized);

                     percentageFatigued = (int) ((fatiguedCbCount * 100) / childCount);
                     System.out.println("checkbox 14: " + fatiguedCbCount + " percentage: " + percentageFatigued);

                     percentageGloomy = (int) ((gloomyCbCount * 100) / childCount);
                     System.out.println("checkbox 15: " + gloomyCbCount + " percentage: " + percentageGloomy);

                     percentageGrumpy = (int) ((grumpyCbCount * 100) / childCount);
                     System.out.println("checkbox 16: " + grumpyCbCount + " percentage: " + percentageGrumpy);

                     percentageGrouchy = (int) ((grouchyCbCount * 100) / childCount);
                     System.out.println("checkbox 17: " + grouchyCbCount + " percentage: " + percentageGrouchy);

                     percentageHappy = (int) ((happyCbCount * 100) / childCount);
                     System.out.println("checkbox 18: " + happyCbCount + " percentage: " + percentageHappy);

                     percentageHesitant = (int) ((hesitantCbCount * 100) / childCount);
                     System.out.println("checkbox 19: " + hesitantCbCount + " percentage: " + percentageHesitant);

                     percentageImpatient = (int) ((impatientCbCount * 100) / childCount);
                     System.out.println("checkbox 20: " + impatientCbCount + " percentage: " + percentageImpatient);

                     percentageInsecure = (int) ((insecureCbCount * 100) / childCount);
                     System.out.println("checkbox 21: " + insecureCbCount + " percentage: " + percentageInsecure);

                     System.out.println("-------------------------------------");

                    System.out.println("CHILD COUNT"+childCount);


                     moodString= "<i>Below show the % of people feeling one of the following moods today:</i> <br/><br/>"
                             +percentageAfraid +"% ~ feeling <i>afraid</i>. <br/>"
                             +percentageAggrevated+"% ~ feeling <i>aggrevated</i>. <br/> "
                             +percentageAngry+"% ~ feeling <i>angry.<br/>"
                             +percentageAnxious+"% ~ feeling <i>anxious.<br/>"
                             +percentageAwkward +"% ~ feeling <i>awkward. <br /> "
                             +percentageBrave+"% ~ feeling <i>brave. <br />"
                             +percentageCalm+"% ~ feeling <i>calm. <br />"
                             +percentageConfident+"% ~ feeling <i>confident</i>. <br />"
                             +percentageContent +"% ~ feeling <i>content</i>. <br /> "
                             +percentageDepressed+"% ~ feeling <i>depressed</i>. <br /> "
                             +percentageDiscouraged+"% ~ feeling <i>discouraged</i>. <br />"
                             +percentageDistant+"% ~ feeling <i>distant</i>. <br /> "
                             +percentageEnergized + "% ~ feeling <i>energized</i>. <br />"
                             +percentageFatigued+"% ~ feeling <i>fatigued</i>. <br />"
                             +percentageGloomy+"% ~ feeling <i>gloomy</i>. <br /> "
                             +percentageGrumpy+"% ~ feeling <i>grumpy</i>. <br /> "
                             +percentageGrouchy+"% ~ feeling <i>grouchy</i>. <br />"
                             +percentageHappy +"% ~ feeling <i>happy</i>. <br /> "
                             +percentageHesitant+"% ~ feeling <i>hesitant</i>. <br /> "
                             +percentageImpatient+"% ~ feeling <i>impatient</i>. <br/> "
                             +percentageInsecure+"% ~ feeling <i>insecure</i>.";
                 } catch (Exception e) {
                    System.out.println("error in capturing panic attacks "+e);
                     moodString="no results found, please try again";
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

    private void resetValues() {
        isAfraid=false;
        isAggrevated=false;
        isAngry=false;
         isAnxious=false;
         isAwkward=false;
          isBrave=false;
          isCalm=false;
          isConfident=false;
          isContent=false;
         isDepressed=false;
         isDiscouraged=false;
         isDistant=false;
         isEnergized=false;
         isFatigued=false;
         isGloomy=false;
         isGrumpy=false;
         isGrouchy=false;
         isHappy=false;
         isHesitant=false;
         isImpatient=false;
          isInsecure=false;
    }

    public void getPanicAttacks() {
       // todaysDate = "28/07/2018";

        /*Getting current date*/
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        System.out.println("cal.getTime: "+cal.getTime());
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        final int year = cal.get(Calendar.YEAR);

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


        final String todaysDate = d + "/" + m + "/" + year;
        System.out.println("todays date: "+todaysDate);

        //getting panic attacks for today
        final Query panicQuery = panicAttackDb.orderByChild("panicDate");
        panicQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean loc=false;
                boolean date = false;
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();

                        if(key.equals("location") && (value.contains(userCountryName) || value.contains(postelCode) || value.contains(countryCode))){
                            countryPanicCount= countryPanicCount+1;
                            loc=true;
                            System.out.println("country count is: "+ countryPanicCount);
                        }

                        if (key.equals("panicDate") && value.contains(todaysDate)) {
                            todaysPanicCount = todaysPanicCount + 1;
                            date=true;
                            System.out.println("in count...current count is: "+ todaysPanicCount);

                            //
                            //
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            try
                            {
                                Date tempDate = simpleDateFormat.parse(value);

                                System.out.println("date : "+simpleDateFormat.format(tempDate));

                                String temp = simpleDateFormat.format(tempDate);
                                //  System.out.println(temp.substring(temp.lastIndexOf("")))
                                String substring = temp.substring(temp.length() -5);
                                String substring1 = substring.substring(0,2);

                                System.out.println("temp: "+temp+ "sub string: "+substring+" sub string 1: "+substring1);
                                int v = Integer.parseInt(substring1);
                                //System.out.println("Double "+v);
                                //
                                if(v>06&& v <12){

                                    morning = true;
                                    morningCount = morningCount+1;
                                    System.out.println("morning");
                                }else if(v> 12 && v < 15){

                                    afternoon= true;
                                    afternoonCount =afternoonCount+1;
                                    System.out.println("afternoon");
                                }else if(v > 15 && v < 19){

                                    evening = true;
                                    eveningCount = eveningCount+1;
                                    System.out.println("evening");
                                }else{

                                    nightCount= nightCount+1;
                                    System.out.println("night");
                                    night=true;

                                }

                                panicCountThisYear= panicCountThisYear+1;
                            }
                            catch (ParseException ex)
                            {
                                System.out.println("Exception "+ex);
                            }

                            //
                        }else if(key.equals("panicDate") && !value.contains(todaysDate)){
                            //
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            try {
                                Date tempDate = simpleDateFormat.parse(value);
                                String t = tempDate.toString().substring(tempDate.toString().length() - 4);
                                int tempDateYear = Integer.parseInt(t);
                                System.out.println("tempDateYear: " + tempDateYear);
                                if (tempDateYear==year) {
                                    System.out.println("tempDateYear is 2018");

                                System.out.println("date : " + simpleDateFormat.format(tempDate));

                                String temp = simpleDateFormat.format(tempDate);
                                //  System.out.println(temp.substring(temp.lastIndexOf("")))
                                String substring = temp.substring(temp.length() - 5);
                                String substring1 = substring.substring(0, 2);

                                System.out.println("temp: " + temp + "sub string: " + substring + " sub string 1: " + substring1);
                                int v = Integer.parseInt(substring1);
                                //System.out.println("Double "+v);
                                //
                                if (v > 06 && v < 12) {

                                    morning = true;
                                    morningCount = morningCount + 1;
                                    System.out.println("morning");
                                } else if (v > 12 && v < 15) {

                                    afternoon = true;
                                    afternoonCount = afternoonCount + 1;
                                    System.out.println("afternoon");
                                } else if (v > 15 && v < 19) {

                                    evening = true;
                                    eveningCount = eveningCount + 1;
                                    System.out.println("evening");
                                } else {

                                    nightCount = nightCount + 1;
                                    System.out.println("night");
                                    night = true;

                                }
                            }else{

                                }
                                panicCountThisYear= panicCountThisYear+1;
                            }
                            catch (ParseException ex)
                            {
                                System.out.println("Exception "+ex);
                            }
                        }

                    }
                    if(loc==true && date==true){
                        todayCountryPanicCount= todayCountryPanicCount+1;
                    }

                    if(todaysPanicCount ==0){
                        System.out.println("in if statement");
                        panicText="<b><i>Wow! No panic attacks were recorded yet today</i>";
                    }else{
                        System.out.println("in else statement");
                        panicText="There is currently a total of <i>"+ todaysPanicCount +" </i> panic attacks recorded today around the world.";
                    }

                } catch (Exception e) {
                    System.out.println("error in capturing panic attacks "+e);
                    panicText="no results found, please try again";

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

    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))


        if (ActivityCompat.checkSelfPermission(StatsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StatsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StatsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                try {
                    addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0);
                    userCountryName = addresses.get(0).getCountryName();
                    countryCode= addresses.get(0).getCountryCode();
                    System.out.println("country code "+countryCode);
                    System.out.println("postel code "+postelCode);
                    postelCode= addresses.get(0).getPostalCode();
                    userAddress = "<b>address:</b> " + address+ " <b>country name</b> "+userCountryName;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                try {
                    addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0);
                    String countryName = addresses.get(0).getCountryName();
                    userAddress = "address: " + address+ " country name "+countryName;
                    userCountryName = addresses.get(0).getCountryName();
                    countryCode= addresses.get(0).getCountryCode();
                    System.out.println("country code "+countryCode);
                    System.out.println("postel code "+postelCode);
                    postelCode= addresses.get(0).getPostalCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                try {
                    addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0);
                    String countryName = addresses.get(0).getCountryName();
                    userAddress = "address: " + address+ " country name "+countryName;
                    userCountryName = addresses.get(0).getCountryName();
                    countryCode= addresses.get(0).getCountryCode();
                    System.out.println("country code "+countryCode);
                    System.out.println("postel code "+postelCode);
                    postelCode= addresses.get(0).getPostalCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(StatsActivity.this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
