package com.example.louis.prototype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Chart extends AppCompatActivity {

    private static final String TAG = "Chart";
    String userID;
    DatabaseReference OasisDb;
    DatabaseReference OdsisDb;
    DatabaseReference MoodsDb;
    LineGraphSeries<DataPoint> series, series2;
    BarGraphSeries<DataPoint> barSeries;
    int x = 1;
    int k = 1;
    int y;
    double totalScore = 5;
    float percentage;
    int checkBox1Count = 0;
    int checkBox2Count = 0;
    int checkBox3Count = 0;
    int checkBox4Count = 0;
    int checkBox5Count = 0;
    int checkBox6Count = 0;
    int checkBox7Count = 0;
    GraphView lineGraph, barGraph;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //setting back btn on actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //current user id~access specific info
        userID = currentFirebaseUser.getUid();

        //referencing databases being used in graphs
        OasisDb = database.getReference("oasis");
        OdsisDb = database.getReference("odsis");
        MoodsDb = database.getReference("moods");

        //assigning variables to views
        lineGraph = (GraphView) findViewById(R.id.graph);
        barGraph = (GraphView) findViewById(R.id.bargraph);

        //setting titles of graphs
        lineGraph.setTitle("Oasis and Odsis");
        barGraph.setTitle("moods bar lineGraph");

        //plotting graph data
        System.out.println("plotting line lineGraph...");
        getLineGraphData();
        System.out.println("plotting bar chart...");
        getBarChartData();

        //floating home btn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                       startActivity(intentMain);
                                   }
                               }
        );
    }

    //*Methods*

    //back to previous activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Line graph data
    public void getLineGraphData() {
        //declaring and assigning vectors
        final Vector<DataPoint> pointVector = new Vector<>();
        final Vector<DataPoint> pointVector2 = new Vector<>();

        final Query oasisQuery = OasisDb.orderByChild("user").equalTo(userID); //oasis(anxiety) query
        final Query odsisQuery = OdsisDb.orderByChild("user").equalTo(userID); //odsis(depression) query

        System.out.println("------Query 1: OASIS-------");

        //to do when new oasis is added
        oasisQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //getting key & value from db
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();

                    //if key = totalOasisValue do the following:
                    if (key.equals("totalOasisValue")) {
                        //  System.out.println("key " + key + " Value " + value);

                        y = Integer.parseInt(value); //y=value
                        pointVector.add(new DataPoint(x, y)); //add datapoint (1, value)
                        x = x + 1; //x+1=2 (2,value) etc..
                    }
                }

                //datapoint array
                DataPoint[] dataPoints = new DataPoint[pointVector.size()];
                int x = pointVector.size();

                //if i is less than pointVector size do:
                for (int i = 0; i < x; i++) {
                    dataPoints[i] = pointVector.get(i);//get each
                }
                series = new LineGraphSeries<>(dataPoints); //plot
                series.setColor(Color.RED); //red for anxiety line
                lineGraph.addSeries(series);//add to graph

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


        System.out.println("------Query 2: ODSIS-------");

        //to do when odsis is added
        odsisQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) { //for each child
                    //get key & value
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();

                    //if key = totalOdsisValue do :
                    if (key.equals("totalOdsisValue")) {
                        //  System.out.println("key " + key + " Value " + value);
                        y = Integer.parseInt(value);
                        pointVector2.add(new DataPoint(k, y));
                        k = k + 1;
                    }

                }

                //data point array
                DataPoint[] dataPoints = new DataPoint[pointVector2.size()];
                int x = pointVector2.size();

                //if i is less than pointVector2 size
                for (int i = 0; i < x; i++) {
                    dataPoints[i] = pointVector2.get(i);
                }
                series2 = new LineGraphSeries<>(dataPoints);
                series2.setColor(Color.BLUE); //blue line for depression
                lineGraph.addSeries(series2);

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

    //BarChart Data
    public void getBarChartData() {

        final Calendar calendar_Today = Calendar.getInstance(); // this would default to now
        calendar_Today.add(Calendar.DAY_OF_MONTH, +1);
        final Calendar calendar_weekAgo = Calendar.getInstance(); // this would default to now
        calendar_weekAgo.add(Calendar.DAY_OF_MONTH, -7);

        final Query moodQuery = MoodsDb.orderByChild("userID").equalTo(userID);
        System.out.println("------Query 3-------");

        moodQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean betweenDates = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                    // System.out.println("key "+key+" Value "+value);
                    if (key.equals("symptomDate")) {
                        // System.out.println("key "+key+" Value "+value);
                        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        Date startDate;
                        try {
                            startDate = df.parse(value);
                            String startDateString1 = df.format(startDate);
                            Calendar calendar_Test = Calendar.getInstance();
                            calendar_Test.setTime(startDate);
                            if (calendar_Test.getTime().after(calendar_weekAgo.getTime()) && calendar_Test.getTime().before(calendar_Today.getTime())) {
                                betweenDates = true;
                            } else {
                                // System.out.println("nope not between these two dates");
                            }
                            long a = getDifferenceDays(calendar_Test.getTime(), calendar_Today.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("woops error occurred with dates");
                        }
                    }

                    //remove series
                    barGraph.removeAllSeries();
                    if (key.equals("checkBox1") && value.equals("true")) {
                        //   System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox1Count = checkBox1Count + 1;
                    }
                    if (key.equals("checkBox2") && value.equals("true")) {
                        //    System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox2Count = checkBox2Count + 1;
                    }
                    if (key.equals("checkBox3") && value.equals("true")) {
                        //    System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox3Count = checkBox3Count + 1;
                    }
                    if (key.equals("checkBox4") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox4Count = checkBox4Count + 1;
                    }
                    if (key.equals("checkBox5") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox5Count = checkBox5Count + 1;
                    }
                    if (key.equals("checkBox6") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox6Count = checkBox6Count + 1;
                    }
                    if (key.equals("checkBox7") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox7Count = checkBox7Count + 1;
                    }
                }
                percentage = (float) ((checkBox1Count * 100) / totalScore);

                System.out.println("checkbox 1: " + checkBox1Count + " percentage: " + percentage);
                percentage = (float) ((checkBox2Count * 100) / totalScore);
                System.out.println("checkbox 2: " + checkBox2Count + " percentage: " + percentage);
                percentage = (float) ((checkBox3Count * 100) / totalScore);
                System.out.println("checkbox 3: " + checkBox3Count + " percentage: " + percentage);
                percentage = (float) ((checkBox4Count * 100) / totalScore);
                System.out.println("checkbox 4: " + checkBox4Count + " percentage: " + percentage);
                percentage = (float) ((checkBox5Count * 100) / totalScore);
                System.out.println("checkbox 5: " + checkBox5Count + " percentage: " + percentage);
                percentage = (float) ((checkBox6Count * 100) / totalScore);
                System.out.println("checkbox 6: " + checkBox6Count + " percentage: " + percentage);
                percentage = (float) ((checkBox7Count * 100) / totalScore);
                System.out.println("checkbox 7: " + checkBox7Count + " percentage: " + percentage);
                System.out.println("-------------------------------------");

                final Vector<DataPoint> pointVector3 = new Vector<>();
                pointVector3.add(new DataPoint(0, checkBox1Count));
                pointVector3.add(new DataPoint(1, checkBox2Count));
                pointVector3.add(new DataPoint(2, checkBox3Count));
                pointVector3.add(new DataPoint(3, checkBox4Count));
                pointVector3.add(new DataPoint(4, checkBox5Count));
                pointVector3.add(new DataPoint(5, checkBox6Count));
                pointVector3.add(new DataPoint(6, checkBox7Count));

                DataPoint[] dataPoints = new DataPoint[pointVector3.size()];
                int x = pointVector3.size();
                for (int i = 0; i < x; i++) {
                    System.out.println("vector size " + x);
                    System.out.println("point vector get(i) " + pointVector3.get(i));
                    dataPoints[i] = pointVector3.get(i);
                    System.out.println("datapoints[i] " + dataPoints[i]);
                }

                barSeries = new BarGraphSeries<>(dataPoints);
                barGraph.addSeries(barSeries);

                // styling
                barSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
                    }
                });
                barGraph.getViewport().setMaxX(10);
                barSeries.setSpacing(50);
                // draw values on top
                barSeries.setDrawValuesOnTop(true);
                barSeries.setValuesOnTopColor(Color.RED);
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

    //compare days
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
