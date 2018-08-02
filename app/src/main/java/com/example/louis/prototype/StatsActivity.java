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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

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

    float percentageAfraid;
    float percentageAggrevated;
    float percentageAngry;
    float percentageAnxious;
    float percentageAwkward;
    float  percentageBrave;
    float  percentageCalm;
    float  percentageConfident;
    float  percentageContent;
    float percentageDepressed;
    float percentageDiscouraged;
    float percentageDistant;
    float percentageEnergized;
    float percentageFatigued;
    float percentageGloomy;
    float percentageGrumpy;
    float percentageGrouchy;
    float percentageHappy;
    float percentageHesitant;
    float percentageImpatient;
    float  percentageInsecure;

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
int childCount;
String moodString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
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
       thread = new Thread() {

            @Override
            public void run() {

                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            System.out.println("displaying text..");
                            displayStatsTv.setText(Html.fromHtml("<h2>Panic Attacks</h2> "+panicText+" <br /> The total number of panic attacks in <i>"+userCountryName+"</i> is: "+countryPanicCount
                            + "<br /> The total number of panic attacks today in <i>"+userCountryName+"</i> is "+todayCountryPanicCount+"<br /><h2>Moods</h2>"+moodString));

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
                childCount= childCount+1;
                System.out.println("CHILDDD"+childCount);
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
                     percentageAfraid = (float) ((afraidCbCount * 100) / childCount);
                     System.out.println("checkbox 1: " + afraidCbCount + " percentage: " + percentageAfraid);
                     percentageAggrevated = (float) ((aggrevatedCbCount * 100) / childCount);
                     System.out.println("checkbox 2: " + aggrevatedCbCount + " percentage: " + percentageAggrevated);
                     percentageAngry = (float) ((angryCbCount * 100) / childCount);
                     System.out.println("checkbox 3: " + angryCbCount + " percentage: " + percentageAngry);
                     percentageAnxious = (float) ((anxiousCbCount * 100) / childCount);
                     System.out.println("checkbox 4: " + anxiousCbCount + " percentage: " + percentageAnxious);
                     percentageAwkward = (float) ((awkwardCbCount * 100) / childCount);
                     System.out.println("checkbox 5: " + awkwardCbCount + " percentage: " + percentageAwkward);
                     percentageBrave = (float) ((braveCbCount * 100) / childCount);
                     System.out.println("checkbox 6: " + braveCbCount + " percentage: " + percentageBrave);
                     percentageCalm = (float) ((calmCbCount * 100) / childCount);
                     System.out.println("checkbox 7: " + calmCbCount + " percentage: " + percentageCalm);
                     percentageConfident = (float) ((confidentCbCount * 100) / childCount);
                     System.out.println("checkbox 8: " + confidentCbCount + " percentage: " + percentageConfident);
                     percentageContent = (float) ((contentCbCount * 100) / childCount);
                     System.out.println("checkbox 9: " + contentCbCount + " percentage: " + percentageContent);
                     percentageDepressed = (float) ((depressedCbCount * 100) / childCount);
                     System.out.println("checkbox 10: " + depressedCbCount + " percentage: " + percentageDepressed);
                     percentageDiscouraged = (float) ((discouragedCbCount * 100) / childCount);
                     System.out.println("checkbox 11: " + discouragedCbCount + " percentage: " + percentageDiscouraged);
                     percentageDistant = (float) ((distantCbCount * 100) / childCount);
                     System.out.println("checkbox 12: " + distantCbCount + " percentage: " + percentageDistant);
                     percentageEnergized = (float) ((energizedCbCount * 100) / childCount);
                     System.out.println("checkbox 13: " + energizedCbCount + " percentage: " + percentageEnergized);
                     percentageFatigued = (float) ((fatiguedCbCount * 100) / childCount);
                     System.out.println("checkbox 14: " + fatiguedCbCount + " percentage: " + percentageFatigued);
                     percentageGloomy = (float) ((gloomyCbCount * 100) / childCount);
                     System.out.println("checkbox 15: " + gloomyCbCount + " percentage: " + percentageGloomy);
                     percentageGrumpy = (float) ((grumpyCbCount * 100) / childCount);
                     System.out.println("checkbox 16: " + grumpyCbCount + " percentage: " + percentageGrumpy);
                     percentageGrouchy = (float) ((grouchyCbCount * 100) / childCount);
                     System.out.println("checkbox 17: " + grouchyCbCount + " percentage: " + percentageGrouchy);
                     percentageHappy = (float) ((happyCbCount * 100) / childCount);
                     System.out.println("checkbox 18: " + happyCbCount + " percentage: " + percentageHappy);
                     percentageHesitant = (float) ((hesitantCbCount * 100) / childCount);
                     System.out.println("checkbox 19: " + hesitantCbCount + " percentage: " + percentageHesitant);
                     percentageImpatient = (float) ((impatientCbCount * 100) / childCount);
                     System.out.println("checkbox 20: " + impatientCbCount + " percentage: " + percentageImpatient);
                     percentageInsecure = (float) ((insecureCbCount * 100) / childCount);
                     System.out.println("checkbox 21: " + insecureCbCount + " percentage: " + percentageInsecure);
                     System.out.println("-------------------------------------");

                     moodString= "Afraid:"+percentageAfraid +"<br/>Aggrevated: "+ percentageAggrevated+"<br/> Angry: "+percentageAngry+"<br/> Anxious: "+percentageAnxious+"<br /> Awkward: "+percentageAwkward
                             +"<br /> Brave: "+percentageBrave+"<br /> Calm: "+percentageCalm+"<br /> Confident: "+percentageConfident+"<br /> Content: "+percentageContent +
                            "<br /> Depressed: "+percentageDepressed+"<br /> Discouraged: "+percentageDiscouraged+"<br /> Distant: "+percentageDistant+"<br /> Energized: "+percentageEnergized +
                             "<br /> Fatigued: "+percentageFatigued+"<br /> Gloomy: "+percentageGloomy+"<br /> Grumpy: "+percentageGrumpy+"<br /> Grouchy"+percentageGrouchy+"<br /> Happy "+percentageHappy +
                             "<br /> Hesitant: "+percentageHesitant+"<br /> Impatient: "+percentageImpatient+"<br/> Insecure: "+percentageInsecure;
                 } catch (Exception e) {
                    System.out.println("error in capturing panic attacks "+e);
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
}
