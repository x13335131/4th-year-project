package com.example.louis.prototype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import com.jjoe64.graphview.LegendRenderer;
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
import java.util.ArrayList;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Chart extends AppCompatActivity {

    String userID;
    FirebaseDatabase database;
    DatabaseReference OasisDb, OdsisDb, MoodsDb, SymptomsDb;
    LineGraphSeries<DataPoint> series, series2;
    BarGraphSeries<DataPoint> barSeries;
    GraphView lineGraph, barGraph;
    int x = 1;
    int k = 1;
    int y;
    double totalScore = 5;
    double symtpomTotalScore = 10;
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
//symptoms
    int acneCount=0;
    int bloatingCount=0;
    int crampsCount=0;
    int dizzinessCount=0;
    int headacheCount=0;
    int insomniaCount=0;
    int spotsCount=0;
    int sweatingCount=0;
    Boolean acneSelected=false;
    Boolean bloatingSelected=false;
    Boolean crampsSelected=false;
    Boolean dizzinessSelected=false;
    Boolean spotsSelected=false;
    Boolean headacheSelected=false;
    Boolean insomniaSelected=false;
    Boolean sweatingSelected=false;
    //pie stuff
    private static String TAG = "Chart";
    private float[] yData;
    private String[] xData;
    TextView legendTvACNE, legendTvBLOATING, legendTvCRAMPS, legendTvDIZZINESS, legendTvSPOTS, legendTvHEADACHE, legendTvINSOMNIA, legendTvSWEATING;
    PieChart pieChart;
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
        SymptomsDb = database.getReference("symptoms");

        //assigning variables to views
        lineGraph = (GraphView) findViewById(R.id.graph);
        barGraph = (GraphView) findViewById(R.id.bargraph);

        //setting titles of graphs
        lineGraph.setTitle("Oasis and Odsis");
        barGraph.setTitle("moods barchart");
//Pie chart

        pieChart = (PieChart)findViewById(R.id.piechart);
        Description desc = new Description();
        desc.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        desc.setText("My Symptoms");
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);

        ///////////////////////////////
        //pie end
        //plotting graph data
        System.out.println("plotting line lineGraph...");
        getLineGraphData();
        System.out.println("plotting bar chart...");
        getBarChartData();
        System.out.println("plotting pie chart...");
        //getValsFromDb();
        addDataSet();
        //legend
        legendTvACNE = (TextView) findViewById(R.id.legendTv);
        String acneGRAY ="<font color=\"ltgray\"> \u2022 ACNE </font>";
        String bloatingBLUE ="<font color=\"blue\"> \u2022 BLOATING </font>";
        String crampRED ="<font color=\"red\"> \u2022 CRAMP </font>";
        String dizzinessGREEN ="<font color=\"green\"> \u2022 DIZZINESS </font>";
        String spotsCYAN ="<font color=\"cyan\"> \u2022 SPOTS </font>";
        String headacheMAGENTA ="<font color=\"magenta\"> \u2022 HEADACHE </font>";
        String insomniaDKGRAY ="<font color=\"dkgray\"> \u2022 INSOMNIA </font>";
        String sweatingYELLOW ="<font color=\"yellow\"> \u2022 SWEATING </font>";
        legendTvACNE.setTextSize(8);
       // legendTvACNE.setTextColor(Color.GRAY);
        legendTvACNE.setText(Html.fromHtml("<html> "+acneGRAY+" "+bloatingBLUE+" "+crampRED+" "+dizzinessGREEN+" "+spotsCYAN+" "+headacheMAGENTA+" "+insomniaDKGRAY+" "+sweatingYELLOW+"</html>"));
       /* legendTvBLOATING.setTextSize(12);
        legendTvBLOATING.setTextColor(Color.BLUE);
        legendTvBLOATING.setText("BLOATING");
        legendTvCRAMPS.setTextSize(12);
        legendTvCRAMPS.setTextColor(Color.RED);
        legendTvCRAMPS.setText("CRAMPS");
        legendTvDIZZINESS.setTextSize(12);
        legendTvDIZZINESS.setTextColor(Color.GREEN);
        legendTvDIZZINESS.setText("DIZZINESS");
        legendTvSPOTS.setTextSize(12);
        legendTvSPOTS.setTextColor(Color.CYAN);
        legendTvSPOTS.setText("SPOTS");
        legendTvHEADACHE.setTextSize(12);
        legendTvHEADACHE.setTextColor(Color.MAGENTA);
        legendTvHEADACHE.setText("HEADACHE");
        legendTvINSOMNIA.setTextSize(12);
        legendTvINSOMNIA.setTextColor(Color.DKGRAY);
        legendTvINSOMNIA.setText("INSOMNIA");
        legendTvSWEATING.setTextSize(12);
        legendTvSWEATING.setTextColor(Color.YELLOW);
        legendTvSWEATING.setText("SWEATING");
*/

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

    private void addDataSet() {

        final Calendar calendar_Today = Calendar.getInstance(); // this would default to now
        calendar_Today.add(Calendar.DAY_OF_MONTH, +1);
        //over last week
        final Calendar calendar_weekAgo = Calendar.getInstance(); // this would default to now
        calendar_weekAgo.add(Calendar.DAY_OF_MONTH, -7);
        //over last month
        final Calendar calendar_monthAgo = Calendar.getInstance();
        calendar_monthAgo.add(Calendar.DAY_OF_MONTH, -30);

        final Query symptomQuery = SymptomsDb.orderByChild("userID").equalTo(userID);
        symptomQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean betweenDates = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                    if (key.equals("symptomDate")) {
                        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        Date startDate;
                        try {
                            startDate = df.parse(value);
                            String startDateString1 = df.format(startDate);
                            Calendar calendar_Test = Calendar.getInstance();
                            calendar_Test.setTime(startDate);
                            if (calendar_Test.getTime().after(calendar_weekAgo.getTime()) && calendar_Test.getTime().before(calendar_Today.getTime())) {
                                betweenDates = true;
                                System.out.println("past week: value: " + value);
                            }
                            if (calendar_Test.getTime().after(calendar_monthAgo.getTime()) && calendar_Test.getTime().before(calendar_Today.getTime())) {
                                betweenDates = true;
                                System.out.println("past Month: value: " + value);
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
                    if (key.equals("value1") && !value.equals("0")) {
                        acneCount = acneCount + 1;
                        acneSelected = true;
                    }
                    if (key.equals("value2") && !value.equals("0")) {
                        bloatingCount = bloatingCount + 1;
                        bloatingSelected = true;
                    }
                    if (key.equals("value3") && !value.equals("0")) {
                        crampsCount = crampsCount + 1;
                        crampsSelected = true;
                    }
                    if (key.equals("value4") && !value.equals("0")) {
                        dizzinessCount = dizzinessCount + 1;
                        dizzinessSelected = true;
                    }
                    if (key.equals("value5") && !value.equals("0")) {
                        spotsCount = spotsCount + 1;
                        spotsSelected = true;
                    }
                    if (key.equals("value6") && !value.equals("0")) {
                        headacheCount = headacheCount + 1;
                        headacheSelected = true;
                    }
                    if (key.equals("value7") && !value.equals("0")) {
                        insomniaCount = insomniaCount + 1;
                        insomniaSelected = true;
                    }
                    if (key.equals("value8") && !value.equals("0")) {
                        sweatingCount = sweatingCount + 1;
                        sweatingSelected = true;
                    }

                    float acnePercentValue = (float) ((acneCount * 100) / symtpomTotalScore);
                    System.out.println("val 1: " + acneCount + " percentage: " + acnePercentValue);
                    float bloatingPercentValue = (float) ((bloatingCount * 100) / symtpomTotalScore);
                    System.out.println("val 2: " + bloatingCount + " percentage: " + bloatingPercentValue);
                    float crampPercentValue = (float) ((crampsCount * 100) / symtpomTotalScore);
                    System.out.println("val 3: " + crampsCount + " percentage: " + crampPercentValue);
                    float dizzinessPercentValue = (float) ((dizzinessCount * 100) / symtpomTotalScore);
                    System.out.println("val 4: " + dizzinessCount + " percentage: " + dizzinessPercentValue);
                    float headachePercentValue = (float) ((headacheCount * 100) / symtpomTotalScore);
                    System.out.println("val 5: " + headacheCount + " percentage: " + headachePercentValue);
                    float insomniaPercentValue = (float) ((insomniaCount * 100) / symtpomTotalScore);
                    System.out.println("val 6: " + insomniaCount + " percentage: " + insomniaPercentValue);
                    float spotsPercentValue = (float) ((spotsCount * 100) / symtpomTotalScore);
                    System.out.println("val 7: " + spotsCount + " percentage: " + spotsPercentValue);
                    float sweatingPercentValue = (float) ((sweatingCount * 100) / symtpomTotalScore);
                    System.out.println("val 8: " + sweatingCount + " percentage: " + sweatingPercentValue);
                    System.out.println("-------------------------------------");

                    yData = new float[] {acnePercentValue, bloatingPercentValue, crampPercentValue, dizzinessPercentValue,
                            spotsPercentValue, headachePercentValue, insomniaPercentValue, sweatingPercentValue};
                    xData = new String[]{"Acne", "Bloating", "Cramps", "Dizziness", "Spots", "Headache", "Insomnia", "Sweating"};

                    ArrayList<PieEntry> yEntrys = new ArrayList<>();
                    ArrayList<String> xEntrys = new ArrayList<>();

                    for(int i =0; i < yData.length; i++){
                        yEntrys.add(new PieEntry(yData[i] , i ));
                    }
                    System.out.println("xData length: "+ xData.length);
                    for(int i = 0; i < xData.length; i++){
                        xEntrys.add(xData[i]);
                    }

                    //create data set
                    PieDataSet pieDataSet = new PieDataSet(yEntrys, "Symptoms");
                    pieDataSet.setSliceSpace(2);
                    pieDataSet.setValueTextSize(12);

                    //add colors to dataset
                    ArrayList<Integer> colors = new ArrayList<>();
                    colors.add(Color.LTGRAY);
                    colors.add(Color.BLUE);
                    colors.add(Color.RED);
                    colors.add(Color.GREEN);
                    colors.add(Color.CYAN);
                    colors.add(Color.MAGENTA);
                    colors.add(Color.DKGRAY);
                    colors.add(Color.YELLOW);

                    pieDataSet.setColors(colors);

                    //add legend to chart
                    Legend legend = pieChart.getLegend();
                    legend.setForm(Legend.LegendForm.CIRCLE);
                    legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                    //create pie data object
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.invalidate();

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
                //    series.setTitle("overall anxiety severity and impairment scale");
                lineGraph.addSeries(series);//add to graph

                series.setTitle("OASIS");//Overall depression severity and impairment scale
                //   GridLabelRenderer gridLabel = lineGraph.getGridLabelRenderer();
                //    gridLabel.setHorizontalAxisTitle("Weeks");
                //   lineGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
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


        System.out.println("------ODSIS QUERY-------");

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
                        y = Integer.parseInt(value);
                        pointVector2.add(new DataPoint(k, y));
                        k = k + 1;
                    }
                }

                //data point array
                DataPoint[] dataPoints = new DataPoint[pointVector2.size()];
                int x = pointVector2.size();
                System.out.println("point vector size: "+x);

                //if i is less than pointVector2 size
                for (int i = 0; i < x; i++) {
                    dataPoints[i] = pointVector2.get(i);
                }
                series2 = new LineGraphSeries<>(dataPoints);
                series2.setColor(Color.BLUE); //blue line for depression
                lineGraph.addSeries(series2);
                // lineGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling

                // legend
                lineGraph.getViewport().setMaxX(x+1);
                lineGraph.getViewport().setMinX(1);
                lineGraph.getViewport().setMaxY(25);
                lineGraph.getViewport().setMinY(0);
                lineGraph.getViewport().setXAxisBoundsManual(true);
                lineGraph.getViewport().setYAxisBoundsManual(true);
                series2.setTitle("ODSIS");//Overall depression severity and impairment scale
                GridLabelRenderer gridLabel = lineGraph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle("Week");
                gridLabel.setVerticalAxisTitle("Severity");
                //     lineGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling

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
        System.out.println("------MOOD QUERY-------");

        moodQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean betweenDates = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                    if (key.equals("symptomDate")) {
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
                        afraidCbCount = afraidCbCount + 1;
                    }
                    if (key.equals("aggrevatedCb") && value.equals("true")) {
                        aggrevatedCbCount = aggrevatedCbCount + 1;
                    }
                    if (key.equals("angryCb") && value.equals("true")) {
                        angryCbCount = angryCbCount + 1;
                    }
                    if (key.equals("anxiousCb") && value.equals("true")) {
                        anxiousCbCount = anxiousCbCount + 1;
                    }
                    if (key.equals("awkwardCb") && value.equals("true")) {
                        awkwardCbCount = awkwardCbCount + 1;
                    }
                    if (key.equals("braveCb") && value.equals("true")) {
                        braveCbCount = braveCbCount + 1;
                    }
                    if (key.equals("calmCb") && value.equals("true")) {
                        calmCbCount = calmCbCount + 1;
                    }
                    if(key.equals("confidentCb")&& value.equals("true")) {
                        confidentCbCount = confidentCbCount + 1;
                    }
                    if(key.equals("contentCb")&& value.equals("true")) {
                        contentCbCount = contentCbCount + 1;
                    }
                    if(key.equals("depressedCb")&& value.equals("true")) {
                        depressedCbCount = depressedCbCount + 1;
                    }
                    if(key.equals("discouragedCb")&& value.equals("true")) {
                        discouragedCbCount = discouragedCbCount + 1;
                    }
                    if(key.equals("distantCb")&& value.equals("true")) {
                        distantCbCount = distantCbCount + 1;
                    }
                    if(key.equals("energizedCb")&& value.equals("true")) {
                        energizedCbCount = energizedCbCount + 1;
                    }
                    if(key.equals("fatiguedCb")&& value.equals("true")) {
                        fatiguedCbCount = fatiguedCbCount + 1;
                    }
                    if(key.equals("gloomyCb")&& value.equals("true")) {
                        gloomyCbCount = gloomyCbCount + 1;
                    }
                    if(key.equals("grumpyCb")&& value.equals("true")) {
                        grumpyCbCount = grumpyCbCount + 1;
                    }
                    if(key.equals("grouchyCb")&& value.equals("true")) {
                        grouchyCbCount = grouchyCbCount + 1;
                    }
                    if(key.equals("happyCb")&& value.equals("true")) {
                        happyCbCount = happyCbCount + 1;
                    }
                    if(key.equals("hesitantCb")&& value.equals("true")) {
                        hesitantCbCount = hesitantCbCount + 1;
                    }
                    if(key.equals("impatientCb")&& value.equals("true")) {
                        impatientCbCount = impatientCbCount + 1;
                    }
                    if(key.equals("insecureCb")&& value.equals("true")) {
                        insecureCbCount = insecureCbCount + 1;
                    }
                }
                float percentageAfraid = (float) ((afraidCbCount * 100) / totalScore);
                System.out.println("checkbox 1: " + afraidCbCount + " percentage: " + percentageAfraid);
                float percentageAggrevated = (float) ((aggrevatedCbCount * 100) / totalScore);
                System.out.println("checkbox 2: " + aggrevatedCbCount + " percentage: " + percentageAggrevated);
                float percentageAngry = (float) ((angryCbCount * 100) / totalScore);
                System.out.println("checkbox 3: " + angryCbCount + " percentage: " + percentageAngry);
                float percentageAnxious = (float) ((anxiousCbCount * 100) / totalScore);
                System.out.println("checkbox 4: " + anxiousCbCount + " percentage: " + percentageAnxious);
                float percentageAwkward = (float) ((awkwardCbCount * 100) / totalScore);
                System.out.println("checkbox 5: " + awkwardCbCount + " percentage: " + percentageAwkward);
                float  percentageBrave = (float) ((braveCbCount * 100) / totalScore);
                System.out.println("checkbox 6: " + braveCbCount + " percentage: " + percentageBrave);
                float  percentageCalm = (float) ((calmCbCount * 100) / totalScore);
                System.out.println("checkbox 7: " + calmCbCount + " percentage: " + percentageCalm);
                float  percentageConfident = (float) ((confidentCbCount * 100) / totalScore);
                System.out.println("checkbox 8: " + confidentCbCount + " percentage: " + percentageConfident);
                float  percentageContent = (float) ((contentCbCount * 100) / totalScore);
                System.out.println("checkbox 9: " + contentCbCount + " percentage: " + percentageContent);
                float percentageDepressed = (float) ((depressedCbCount * 100) / totalScore);
                System.out.println("checkbox 10: " + depressedCbCount + " percentage: " + percentageDepressed);
                float percentageDiscouraged = (float) ((discouragedCbCount * 100) / totalScore);
                System.out.println("checkbox 11: " + discouragedCbCount + " percentage: " + percentageDiscouraged);
                float percentageDistant = (float) ((distantCbCount * 100) / totalScore);
                System.out.println("checkbox 12: " + distantCbCount + " percentage: " + percentageDistant);
                float percentageEnergized = (float) ((energizedCbCount * 100) / totalScore);
                System.out.println("checkbox 13: " + energizedCbCount + " percentage: " + percentageEnergized);
                float percentageFatigued = (float) ((fatiguedCbCount * 100) / totalScore);
                System.out.println("checkbox 14: " + fatiguedCbCount + " percentage: " + percentageFatigued);
                float percentageGloomy = (float) ((gloomyCbCount * 100) / totalScore);
                System.out.println("checkbox 15: " + gloomyCbCount + " percentage: " + percentageGloomy);
                float percentageGrumpy = (float) ((grumpyCbCount * 100) / totalScore);
                System.out.println("checkbox 16: " + grumpyCbCount + " percentage: " + percentageGrumpy);
                float percentageGrouchy = (float) ((grouchyCbCount * 100) / totalScore);
                System.out.println("checkbox 17: " + grouchyCbCount + " percentage: " + percentageGrouchy);
                float percentageHappy = (float) ((happyCbCount * 100) / totalScore);
                System.out.println("checkbox 18: " + happyCbCount + " percentage: " + percentageHappy);
                float percentageHesitant = (float) ((hesitantCbCount * 100) / totalScore);
                System.out.println("checkbox 19: " + hesitantCbCount + " percentage: " + percentageHesitant);
                float percentageImpatient = (float) ((impatientCbCount * 100) / totalScore);
                System.out.println("checkbox 20: " + impatientCbCount + " percentage: " + percentageImpatient);
                float  percentageInsecure = (float) ((insecureCbCount * 100) / totalScore);
                System.out.println("checkbox 21: " + insecureCbCount + " percentage: " + percentageInsecure);
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
                barGraph.getViewport().setMaxX(21);
                barGraph.getViewport().setMinY(0.0);
                barSeries.setSpacing(15);
                barSeries.setDrawValuesOnTop(true);
                barSeries.setValuesOnTopColor(Color.RED);
                barSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Double datapoint = dataPoint.getX();
                        String dp = datapoint.toString();
                        String dpName="";
                        switch(dp) {
                            case "0.0" :
                                dpName="afraid";
                                break;
                            case "1.0" :
                                dpName="aggrevated";
                                break;
                            case "2.0" :
                                dpName="angry";
                                break;
                            case "3.0" :
                                dpName="anxious";
                                break;
                            case "4.0" :
                                dpName="awkward";
                                break;
                            case "5.0" :
                                dpName="brave";
                                break;
                            case "6.0" :
                                dpName="calm";
                                break;
                            case "7.0" :
                                dpName="confident";
                                break;
                            case "8.0" :
                                dpName="content";
                                break;
                            case "9.0" :
                                dpName="depressed";
                                break;
                            case "10.0" :
                                dpName="discouraged";
                                break;
                            case "11.0" :
                                dpName="distant";
                                break;
                            case "12.0" :
                                dpName="energized";
                                break;
                            case "13.0" :
                                dpName="fatigued";
                                break;
                            case "14.0" :
                                dpName="gloomy";
                                break;
                            case "15.0" :
                                dpName="grumpy";
                                break;
                            case "16.0" :
                                dpName="grouchy";
                                break;
                            case "17.0" :
                                dpName="happy";
                                break;
                            case "18.0" :
                                dpName="hesitant";
                                break;
                            case "19.0" :
                                dpName="impatient";
                                break;
                            case "20.0" :
                                dpName="insecure";
                                break;
                            default :
                        }
                        Toast.makeText(Chart.this, "Mood: "+dpName, Toast.LENGTH_SHORT).show();
                    }
                });

                GridLabelRenderer gridLabel = barGraph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle("Moods");
                gridLabel.setVerticalAxisTitle("Percentage");
                barGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
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