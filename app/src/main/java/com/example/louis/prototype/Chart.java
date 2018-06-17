package com.example.louis.prototype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Chart extends AppCompatActivity {

    //LineGraphSeries<DataPoint> series;
    LineChart lineChart;
    private static final String TAG = "Chart";
    String userID;
    DatabaseReference OasisDb;
    DatabaseReference OdsisDb;
    DatabaseReference MoodsDb;
    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> series2;
    BarGraphSeries<DataPoint> barseries;
    int x=1;
    int k=1;
    int y;
    double totalScore=5;
    float percentage;
    /*int checkBox1;
    int checkBox2;
    int checkBox3;
    int checkBox4;
    int checkBox5;*/
    int checkBox1Count=0;
    int checkBox2Count=0;
    int checkBox3Count=0;
    int checkBox4Count=0;
    int checkBox5Count=0;
    int checkBox6Count=0;
    int checkBox7Count=0;
    GraphView graph;
    GraphView bargraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         OasisDb= database.getReference("oasis");
        OdsisDb= database.getReference("odsis");
        MoodsDb= database.getReference("moods");
        graph = (GraphView) findViewById(R.id.graph);
        bargraph = (GraphView) findViewById(R.id.bargraph);
        graph.setTitle("Oasis and Odsis");
        bargraph.setTitle("moods bar graph");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID= currentFirebaseUser.getUid();

        System.out.println("plotting line graph...");
        getLineGraphData();
        System.out.println("plotting bar chart...");
        getBarChartData();
       // System.out.println("getting plots...");
        //getPlots();
       // plotBarGraph();
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
       // LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

                                   @Override
                                   public void onClick(View v) {
                                       Intent i2 = new Intent(getApplicationContext(), MainActivity.class);


                                       startActivity(i2);
                                   }
                               }
        );
    }

    //LINE GRAPH
    public void getLineGraphData() {
        final Vector<DataPoint> pointVector = new Vector<>();
        final Vector<DataPoint> pointVector2 = new Vector<>();
        System.out.println("------Query 1: OASIS-------");
        final Query userQuery = OasisDb.orderByChild("user").equalTo(userID);
        final Query userQuery2 = OdsisDb.orderByChild("user").equalTo(userID);

        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();

                    if (key.equals("totalOasisValue")) {
                      //  System.out.println("key " + key + " Value " + value);

                        y = Integer.parseInt(value);
                        pointVector.add(new DataPoint(x, y));
                        x = x + 1;
                    }

                }
                DataPoint[] dataPoints = new DataPoint[pointVector.size()];
                int x = pointVector.size();
                for (int i = 0; i < x; i++) {
                    //  System.out.println("vector size "+x);
                    // System.out.println("point vector get(i) "+pointVector.get(i));
                    dataPoints[i] = pointVector.get(i);
                    //  System.out.println("datapoints[i] "+dataPoints[i]);

                }
                series = new LineGraphSeries<>(dataPoints);
                series.setColor(Color.RED);
                graph.addSeries(series);

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
        //second query
        userQuery2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();


                    if (key.equals("totalOdsisValue")) {
                      //  System.out.println("key " + key + " Value " + value);

                        y = Integer.parseInt(value);
                        pointVector2.add(new DataPoint(k, y));
                        k = k + 1;
                    }

                }
                DataPoint[] dataPoints = new DataPoint[pointVector2.size()];
                int x = pointVector2.size();
                for (int i = 0; i < x; i++) {
                    //  System.out.println("vector size "+x);
                    // System.out.println("point vector get(i) "+pointVector.get(i));
                    dataPoints[i] = pointVector2.get(i);
                    //  System.out.println("datapoints[i] "+dataPoints[i]);

                }
                series2 = new LineGraphSeries<>(dataPoints);
                series2.setColor(Color.BLUE);
                graph.addSeries(series2);

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
        //query 3
    public void getBarChartData(){

        final Calendar calendar_Today = Calendar.getInstance(); // this would default to now
        calendar_Today.add(Calendar.DAY_OF_MONTH, +1);
        final Calendar calendar_weekAgo = Calendar.getInstance(); // this would default to now
        calendar_weekAgo.add(Calendar.DAY_OF_MONTH, -7);
      //  System.out.println("Date = " + calendar_weekAgo.getTime());
      //  System.out.println("between todays date " + calendar_Today.getTime() + " and a week ago = " + calendar_weekAgo.getTime());
        final Query userQuery3 = MoodsDb.orderByChild("userID").equalTo(userID);
        System.out.println("------Query 3-------");

        userQuery3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    boolean betweenDates=false;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                   // System.out.println("key "+key+" Value "+value);
                    if(key.equals("symptomDate")){
                       // System.out.println("key "+key+" Value "+value);
                        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        Date startDate;
                        try {
                            startDate = df.parse(value);
                       //     System.out.println("Date, with the default formatting: " + startDate);

                            // Once converted to a Date object, you can convert
                            // back to a String using any desired format.
                            String startDateString1 = df.format(startDate);
                         //   System.out.println("Date in format MM/dd/yyyy: " + startDateString1);
                            Calendar calendar_Test= Calendar.getInstance();
                            calendar_Test.setTime(startDate);
                        //    System.out.println("calendarTest: "+calendar_Test.getTime()+" today "+calendar_Today.getTime());
                            if(calendar_Test.getTime().after(calendar_weekAgo.getTime())&&calendar_Test.getTime().before(calendar_Today.getTime())){
                               // System.out.println("key "+key+" Value "+value+" date: "+calendar_Test.getTime());
                                betweenDates=true;
                            }else{
                               // System.out.println("nope not between these two dates");
                            }
                          long a= getDifferenceDays(calendar_Test.getTime(), calendar_Today.getTime());
                         // System.out.println("a= "+a);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("woops error occurred with dates");
                        }
                    }
                  /*  if((key.equals("checkBox1")||key.equals("checkBox2")||key.equals("checkBox3")||key.equals("checkBox4")||key.equals("checkBox5"))&&value.equals("true")){
                        System.out.println("key "+key+" value "+value+" this value is true");
                    }*/
                    bargraph.removeAllSeries();
                  if(key.equals("checkBox1")&&value.equals("true")){
                     //   System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox1Count=checkBox1Count+1;
                    }
                   if(key.equals("checkBox2")&&value.equals("true")){
                    //    System.out.println("key "+key+" value "+value+" this value is true");
                       checkBox2Count=checkBox2Count+1;
                    }
                    if(key.equals("checkBox3")&&value.equals("true")){
                    //    System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox3Count=checkBox3Count+1;
                    }
                   if(key.equals("checkBox4")&&value.equals("true")){
                   //     System.out.println("key "+key+" value "+value+" this value is true");
                       checkBox4Count=checkBox4Count+1;
                    }
                    if(key.equals("checkBox5")&&value.equals("true")){
                   //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox5Count=checkBox5Count+1;
                    }
                    if(key.equals("checkBox6")&&value.equals("true")){
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox6Count=checkBox6Count+1;
                    }
                    if(key.equals("checkBox7")&&value.equals("true")){
                        //     System.out.println("key "+key+" value "+value+" this value is true");
                        checkBox7Count=checkBox7Count+1;
                    }
                 /*   System.out.println("checkbox 1: "+checkBox1Count);
                System.out.println("checkbox 2: "+checkBox2Count);
                System.out.println("checkbox 3: "+checkBox3Count);
                System.out.println("checkbox 4: "+checkBox4Count);
                System.out.println("checkbox 5: "+checkBox5Count);*/


                }
                percentage = (float) ((checkBox1Count*100)/totalScore);

                System.out.println("checkbox 1: "+checkBox1Count+" percentage: "+percentage);
                percentage = (float) ((checkBox2Count*100)/totalScore);
                System.out.println("checkbox 2: "+checkBox2Count+" percentage: "+percentage);
                percentage = (float) ((checkBox3Count*100)/totalScore);
                System.out.println("checkbox 3: "+checkBox3Count+" percentage: "+percentage);
                percentage = (float) ((checkBox4Count*100)/totalScore);
                System.out.println("checkbox 4: "+checkBox4Count+" percentage: "+percentage);
                percentage = (float) ((checkBox5Count*100)/totalScore);
                System.out.println("checkbox 5: "+checkBox5Count+" percentage: "+percentage);
                percentage = (float) ((checkBox6Count*100)/totalScore);
                System.out.println("checkbox 6: "+checkBox6Count+" percentage: "+percentage);
                percentage = (float) ((checkBox7Count*100)/totalScore);
                System.out.println("checkbox 7: "+checkBox7Count+" percentage: "+percentage);
                System.out.println("-------------------------------------");
               // System.out.println("inside loop count");
             //   plotBarGraph(checkBox1Count, checkBox2Count, checkBox3Count, checkBox4Count, checkBox5Count);

                final Vector<DataPoint> pointVector3 = new Vector<>();
                pointVector3.add(new DataPoint(0, checkBox1Count));
                pointVector3.add(new DataPoint(1, checkBox2Count));
                pointVector3.add(new DataPoint(2, checkBox3Count));
                pointVector3.add(new DataPoint(3, checkBox4Count));
                pointVector3.add(new DataPoint(4, checkBox5Count));
                pointVector3.add(new DataPoint(5, checkBox6Count));
                pointVector3.add(new DataPoint(6, checkBox7Count));
                //bar graph



                DataPoint[] dataPoints = new DataPoint[pointVector3.size()];
                int x= pointVector3.size();
                for(int i=0; i<x; i++){
                      System.out.println("vector size "+x);
                     System.out.println("point vector get(i) "+pointVector3.get(i));
                    dataPoints[i]=pointVector3.get(i);
                      System.out.println("datapoints[i] "+dataPoints[i]);

                }
                barseries = new BarGraphSeries<>(dataPoints);
                bargraph.addSeries(barseries);

// styling
                barseries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                    }
                });
                bargraph.getViewport().setMaxX(10);
                barseries.setSpacing(50);
// draw values on top
                barseries.setDrawValuesOnTop(true);
                barseries.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);
                //end
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
   /* public void getPlots(){
        System.out.println("box 1 count: "+checkBox1Count);
    }*/
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
  /*  public static long getIntbetweenDays(Date d1) {
        d1.date.compareTo(startDate) >= 0; object.date.compareTo(endDate) <= 0;
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }*/
}
