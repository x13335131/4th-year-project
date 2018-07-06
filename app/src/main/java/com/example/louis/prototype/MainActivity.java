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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //private TextView secondsTv;
    DatabaseReference OdsisDb;
    TextView secondsTv;
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
    private Button diaryBtn, calBtn, chartBtn, socialBtn;
    private Button survey;
    private Button panicButton;
    private Thread thread;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    TextView locationTv;
    Geocoder geocoder;
    List<Address> addresses;
    String address;
    String location;
    double latti, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        mAuth = FirebaseAuth.getInstance();
        thread = new Thread() {

            @Override
            public void run() {

                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI

                            getSupportActionBar().setTitle("My Mental Health");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        secondsTv = (TextView) findViewById(R.id.seconds_tv);
        locationTv = (TextView) findViewById(R.id.text_location);
        diaryBtn = (Button) findViewById(R.id.diary_btn); //diary button
        calBtn = (Button) findViewById(R.id.calendar_btn); //calendar button
        chartBtn = (Button) findViewById(R.id.charts_btn); //chart button
        socialBtn = (Button) findViewById(R.id.social_btn); //social button

        // Stuff that updates the UI
        Switch s = (Switch) findViewById(R.id.panic_btn_switch);
        databasePanic = FirebaseDatabase.getInstance().getReference("panic");
        panicButton = (Button) findViewById(R.id.panic_btn);
        panicList = new ArrayList<>();

        survey = (Button) findViewById(R.id.surveyBtn); //to oasis and odsis
        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();

        OdsisDb = database.getReference("odsis");

        Query lastQuery = OdsisDb.orderByChild("user").equalTo(userID).limitToLast(1);//.orderByKey().limitToLast(1);
        lastQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    value = child.getValue().toString();
                    if (key.equals("todaysDate")) {
                        System.out.println("key " + key + " Value " + value);
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        String dateBeforeString = value;
                        String dateAfterString = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
                        System.out.println("Date Before String: " + dateBeforeString);
                        System.out.println("Date After String: " + dateAfterString);
                        try {
                            Date dateBefore = myFormat.parse(dateBeforeString);
                            Date dateAfter = myFormat.parse(dateAfterString);

                            long difference = dateAfter.getTime() - dateBefore.getTime();
                            daysBetween = (difference / (1000 * 60 * 60 * 24));

                            System.out.println("Number of Days between dates: " + daysBetween);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (daysBetween < 7.0) { //it has been less than 7 days since user took odsis survey then set button to invisible
                            System.out.println("survey set to invisible ");
                            survey.setVisibility(View.INVISIBLE);
                        } else { //it has been more than 7 days than they may take survey
                            System.out.println("Survay set to visible ");
                            survey.setVisibility(View.VISIBLE);
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

        panicButton.setOnClickListener(new View.OnClickListener() {


            boolean pressed = true;

            //Timer  myTimer = new Timer();
            @Override
            public void onClick(View v) {
//if clicked
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (pressed == true) {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        getLocation();
                    System.out.println("starting timer");
                    start(); //start timer
                    pressed = false;
                    secondsTv.setText("Timer Start");
                } else {
                    System.out.println("ending timer");
                    end();
                    pressed = true;
                }


            }

            private void getLocation() {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
                            locationTv.setText("address: " + address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Latitude: " + latitude + " longitude: " + longitude);
                            /*locationTv.setText("Your current location is"+ "\n" + "Lattitude = " + latitude
                                    + "\n" + "Longitude = " + longitude);*/

                    } else if (location1 != null) {
                        latti = location1.getLatitude();
                        longi = location1.getLongitude();
                        latitude = String.valueOf(latti);
                        longitude = String.valueOf(longi);

                        try {
                            addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0);
                            locationTv.setText("address: " + address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Latitude: " + latitude + " longitude: " + longitude);
                            /*
                            locationTv.setText("Your current location is"+ "\n" + "Lattitude = " + latitude
                                    + "\n" + "Longitude = " + longitude);*/


                    } else if (location2 != null) {
                        latti = location2.getLatitude();
                        longi = location2.getLongitude();
                        latitude = String.valueOf(latti);
                        longitude = String.valueOf(longi);

                        try {
                            addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0);
                            locationTv.setText("address: " + address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Latitude: " + latitude + " longitude: " + longitude);
                            /*
                            locationTv.setText("Your current location is"+ "\n" + "Lattitude = " + latitude
                                    + "\n" + "Longitude = " + longitude);*/

                    } else {

                        Toast.makeText(MainActivity.this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            protected void buildAlertMessageNoGps() {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


            //start timer
            public void start() {

                secondsTv.setVisibility(View.VISIBLE);
                locationTv.setVisibility(View.VISIBLE);
                thread = new Thread() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        try {
                            myTimer = new Timer();
                            TimerTask task = new TimerTask() {
                                public void run() {
                                    secondsPassed++;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("Seconds passed " + secondsPassed);
                                            secondsTv.setText("Seconds Passed: " + String.valueOf(secondsPassed));

                                        }
                                    });
                                }
                            };
                            myTimer.scheduleAtFixedRate(task, 1000, 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

            //end timer
            public void end() {
                addPanicToDiary();
                getUserData();
                //a = secondsTv.getText().toString();
                secondsPassed = 0;
                myTimer.cancel();
                secondsTv.setText("Timer Stopped" + secondsCaptured);
            }

            public void addPanicToDiary() {
                secondsCaptured = secondsPassed;
                location = address;
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                java.util.Calendar cal = java.util.Calendar.getInstance();
                String todaysDate = df.format(cal.getTime());

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                userID = currentFirebaseUser.getUid();

                String id = databasePanic.push().getKey();
                PanicButton panic = new PanicButton(secondsCaptured, todaysDate, longi, latti, location, userID);
                databasePanic.child(id).setValue(panic);
                Toast.makeText(MainActivity.this, "panic attack added", Toast.LENGTH_LONG).show();
            }

            public void getUserData() {
                // Read from the database
                //ordering output by length
                // final Query userQuery = databasePanic.orderByChild("length");
                final Query userQuery = databasePanic.orderByChild("userID").equalTo(userID);
                userQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // map.clear();
                        //Get the node from the datasnapshot
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey().toString();
                            String value = child.getValue().toString();
                            //map.put(key,value);
                            System.out.println("key " + key + " Value " + value);
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
        });


        diaryBtn.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View v) {
                                      Intent i1 = new Intent(getApplicationContext(), MyDiary.class);
                                      startActivity(i1);
                                  }
                              }
        );
        calBtn.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View v) {
                                      Intent i2 = new Intent(getApplicationContext(), CalendarActivity.class);
                                      startActivity(i2);
                                  }
                              }
        );
        chartBtn.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View v) {
                                      Intent i3 = new Intent(getApplicationContext(), Chart.class);
                                      startActivity(i3);
                                  }
                              }
        );
        socialBtn.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View v) {
                                      Intent i4 = new Intent(getApplicationContext(), Social.class);
                                      startActivity(i4);
                                  }
                              }
        );
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(getApplicationContext(), Oasis.class);
                startActivity(i5);
            }
        });
        s.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (panicButton.isShown()) {//if clicked and panic button is already showing, make it disappear
                    //myTimer = new Timer();
                    secondsPassed = 0;
                    // myTimer.cancel();

                    panicButton.setVisibility(View.GONE);
                    secondsTv.setVisibility(View.GONE);
                    locationTv.setVisibility(View.GONE);

                } else {
                    panicButton.setVisibility(View.VISIBLE);
                }

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
