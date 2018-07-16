package com.example.louis.prototype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

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
        barGraph.setTitle("moods barchart");

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
        //over last week
        final Calendar calendar_weekAgo = Calendar.getInstance(); // this would default to now
        calendar_weekAgo.add(Calendar.DAY_OF_MONTH, -7);
        //over last month
        final Calendar calendar_monthAgo = Calendar.getInstance();
        calendar_monthAgo.add(Calendar.DAY_OF_MONTH, -30);

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

                                System.out.println("past week: value: "+value);
                            } if (calendar_Test.getTime().after(calendar_monthAgo.getTime()) && calendar_Test.getTime().before(calendar_Today.getTime())) {
                                betweenDates = true;

                                System.out.println("past Month: value: "+value);
                            } else {
                                System.out.println("nope not between these two dates");
                            }
                            long a = getDifferenceDays(calendar_Test.getTime(), calendar_Today.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("woops error occurred with dates");
                        }
                    }

                    //remove series
                    barGraph.removeAllSeries();
                    if (key.equals("afraidCb") && value.equals("true")) {
                        //   System.out.println("key "+key+" value "+value+" this value is true");
                        afraidCbCount = afraidCbCount + 1;
                    }
                    if (key.equals("aggrevatedCb") && value.equals("true")) {
                        //    System.out.println("key "+key+" value "+value+" this value is true");
                        aggrevatedCbCount = aggrevatedCbCount + 1;
                    }
                    if (key.equals("angryCb") && value.equals("true")) {
                        //    System.out.println("key "+key+" value "+value+" this value is true");
                        angryCbCount = angryCbCount + 1;
                    }
                    if (key.equals("anxiousCb") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        anxiousCbCount = anxiousCbCount + 1;
                    }
                    if (key.equals("awkwardCb") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        awkwardCbCount = awkwardCbCount + 1;
                    }
                    if (key.equals("braveCb") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        braveCbCount = braveCbCount + 1;
                    }
                    if (key.equals("calmCb") && value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        calmCbCount = calmCbCount + 1;
                    }
                    if(key.equals("confidentCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        confidentCbCount = confidentCbCount + 1;
                    }
                    if(key.equals("contentCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        contentCbCount = contentCbCount + 1;
                    }
                    if(key.equals("depressedCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        depressedCbCount = depressedCbCount + 1;
                    }
                    if(key.equals("discouragedCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        discouragedCbCount = discouragedCbCount + 1;
                    }
                    if(key.equals("distantCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        distantCbCount = distantCbCount + 1;
                    }
                    if(key.equals("energizedCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        energizedCbCount = energizedCbCount + 1;
                    }
                    if(key.equals("fatiguedCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        fatiguedCbCount = fatiguedCbCount + 1;
                    }
                    if(key.equals("gloomyCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        gloomyCbCount = gloomyCbCount + 1;
                    }
                    if(key.equals("grumpyCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        grumpyCbCount = grumpyCbCount + 1;
                    }
                    if(key.equals("grouchyCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        grouchyCbCount = grouchyCbCount + 1;
                    }
                    if(key.equals("happyCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        happyCbCount = happyCbCount + 1;
                    }
                    if(key.equals("hesitantCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        hesitantCbCount = hesitantCbCount + 1;
                    }
                    if(key.equals("impatientCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        impatientCbCount = impatientCbCount + 1;
                    }
                    if(key.equals("insecureCb")&& value.equals("true")) {
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        insecureCbCount = insecureCbCount + 1;
                    }

                }
                percentage = (float) ((afraidCbCount * 100) / totalScore);

                System.out.println("checkbox 1: " + afraidCbCount + " percentage: " + percentage);
                percentage = (float) ((aggrevatedCbCount * 100) / totalScore);
                System.out.println("checkbox 2: " + aggrevatedCbCount + " percentage: " + percentage);
                percentage = (float) ((angryCbCount * 100) / totalScore);
                System.out.println("checkbox 3: " + angryCbCount + " percentage: " + percentage);
                percentage = (float) ((anxiousCbCount * 100) / totalScore);
                System.out.println("checkbox 4: " + anxiousCbCount + " percentage: " + percentage);
                percentage = (float) ((awkwardCbCount * 100) / totalScore);
                System.out.println("checkbox 5: " + awkwardCbCount + " percentage: " + percentage);
                percentage = (float) ((braveCbCount * 100) / totalScore);
                System.out.println("checkbox 6: " + braveCbCount + " percentage: " + percentage);
                percentage = (float) ((calmCbCount * 100) / totalScore);
                System.out.println("checkbox 7: " + calmCbCount + " percentage: " + percentage);
                percentage = (float) ((confidentCbCount * 100) / totalScore);
                System.out.println("checkbox 8: " + confidentCbCount + " percentage: " + percentage);
                percentage = (float) ((contentCbCount * 100) / totalScore);
                System.out.println("checkbox 9: " + contentCbCount + " percentage: " + percentage);
                percentage = (float) ((depressedCbCount * 100) / totalScore);
                System.out.println("checkbox 10: " + depressedCbCount + " percentage: " + percentage);
                percentage = (float) ((discouragedCbCount * 100) / totalScore);
                System.out.println("checkbox 11: " + discouragedCbCount + " percentage: " + percentage);
                percentage = (float) ((distantCbCount * 100) / totalScore);
                System.out.println("checkbox 12: " + distantCbCount + " percentage: " + percentage);
                percentage = (float) ((energizedCbCount * 100) / totalScore);
                System.out.println("checkbox 13: " + energizedCbCount + " percentage: " + percentage);
                percentage = (float) ((fatiguedCbCount * 100) / totalScore);
                System.out.println("checkbox 14: " + fatiguedCbCount + " percentage: " + percentage);
                percentage = (float) ((gloomyCbCount * 100) / totalScore);
                System.out.println("checkbox 15: " + gloomyCbCount + " percentage: " + percentage);
                percentage = (float) ((grumpyCbCount * 100) / totalScore);
                System.out.println("checkbox 16: " + grumpyCbCount + " percentage: " + percentage);
                percentage = (float) ((grouchyCbCount * 100) / totalScore);
                System.out.println("checkbox 17: " + grouchyCbCount + " percentage: " + percentage);
                percentage = (float) ((happyCbCount * 100) / totalScore);
                System.out.println("checkbox 18: " + happyCbCount + " percentage: " + percentage);
                percentage = (float) ((hesitantCbCount * 100) / totalScore);
                System.out.println("checkbox 19: " + hesitantCbCount + " percentage: " + percentage);
                percentage = (float) ((impatientCbCount * 100) / totalScore);
                System.out.println("checkbox 20: " + impatientCbCount + " percentage: " + percentage);
                percentage = (float) ((insecureCbCount * 100) / totalScore);
                System.out.println("checkbox 21: " + insecureCbCount + " percentage: " + percentage);
                System.out.println("-------------------------------------");

                final Vector<DataPoint> pointVector3 = new Vector<>();

                pointVector3.add(new DataPoint(0, 0));
                pointVector3.add(new DataPoint(0, afraidCbCount));
                pointVector3.add(new DataPoint(1, aggrevatedCbCount));
                pointVector3.add(new DataPoint(2, angryCbCount));
                pointVector3.add(new DataPoint(3, anxiousCbCount));
                pointVector3.add(new DataPoint(4, awkwardCbCount));
                pointVector3.add(new DataPoint(5, braveCbCount));
                pointVector3.add(new DataPoint(6, calmCbCount));
                pointVector3.add(new DataPoint(7, confidentCbCount));
                pointVector3.add(new DataPoint(8, contentCbCount));
                pointVector3.add(new DataPoint(9, depressedCbCount));
                pointVector3.add(new DataPoint(10, discouragedCbCount));
                pointVector3.add(new DataPoint(11, distantCbCount));
                pointVector3.add(new DataPoint(12, energizedCbCount));
                pointVector3.add(new DataPoint(13, fatiguedCbCount));
                pointVector3.add(new DataPoint(14, gloomyCbCount));
                pointVector3.add(new DataPoint(15, grumpyCbCount));
                pointVector3.add(new DataPoint(16, grouchyCbCount));
                pointVector3.add(new DataPoint(17, happyCbCount));
                pointVector3.add(new DataPoint(18, hesitantCbCount));
                pointVector3.add(new DataPoint(19, impatientCbCount));
                pointVector3.add(new DataPoint(20, insecureCbCount));


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
                barGraph.getViewport().setMaxX(20);
                barGraph.getViewport().setMinY(0.0);
                barSeries.setSpacing(15);
                // draw values on top

                barSeries.setDrawValuesOnTop(true);
                barSeries.setValuesOnTopColor(Color.RED);

                //
                barSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Double datapoint = dataPoint.getX();
                        String dp = datapoint.toString();
                        String dpName="";
                        switch(dp) {
                            case "0.0" :
                                // Statements
                                dpName="afraid";
                                break; // optional

                            case "1.0" :
                                // Statements
                                dpName="aggrevated";
                                break; // optional
                            case "2.0" :
                                // Statements
                                dpName="angry";
                                break; // optional
                            case "3.0" :
                                // Statements
                                dpName="anxious";
                                break; // optional
                            case "4.0" :
                                // Statements
                                dpName="awkward";
                                break; // optional
                            case "5.0" :
                                // Statements
                                dpName="brave";
                                break; // optional
                            case "6.0" :
                                // Statements
                                dpName="calm";
                                break; // optional
                            case "7.0" :
                                // Statements
                                dpName="confident";
                                break; // optional
                            case "8.0" :
                                // Statements
                                dpName="content";
                                break; // optional
                            case "9.0" :
                                // Statements
                                dpName="depressed";
                                break; // optional
                            case "10.0" :
                                // Statements
                                dpName="discouraged";
                                break; // optional
                            case "11.0" :
                                // Statements
                                dpName="distant";
                                break; // optional
                            case "12.0" :
                                // Statements
                                dpName="energized";
                                break; // optional
                            case "13.0" :
                                // Statements
                                dpName="fatigued";
                                break; // optional
                            case "14.0" :
                                // Statements
                                dpName="gloomy";
                                break; // optional
                            case "15.0" :
                                // Statements
                                dpName="grumpy";
                                break; // optional
                            case "16.0" :
                                // Statements
                                dpName="grouchy";
                                break; // optional
                            case "17.0" :
                                // Statements
                                dpName="happy";
                                break; // optional
                            case "18.0" :
                                // Statements
                                dpName="hesitant";
                                break; // optional
                            case "19.0" :
                                // Statements
                                dpName="impatient";
                                break; // optional
                            case "20.0" :
                                // Statements
                                dpName="insecure";
                                break; // optional

                            // You can have any number of case statements.
                            default : // Optional
                                // Statements
                        }
                        Toast.makeText(Chart.this, "Mood: "+dpName, Toast.LENGTH_SHORT).show();
                    }
                });

                GridLabelRenderer gridLabel = barGraph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle("Moods");
                // enable scaling and scrolling
              //  barGraph.getViewport().setScalable(true);
             //   barGraph.getViewport().setScalableY(false);

             //   barGraph.getViewport().setScrollable(true); // enables horizontal scrolling
             //   barGraph.getViewport().setScrollableY(false); // enables vertical scrolling
                barGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling

               // barGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
                //
                //use static labels for horizontal and vertical labels
               // StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(barGraph);
              //  staticLabelsFormatter.setHorizontalLabels(new String[] {" ","moods", " "});
               // staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
              //  barGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
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
