package com.example.louis.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by louis on 01/11/2017.
 */

public class Tab2Symptoms extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private SeekBar acneSeekbar;
    private SeekBar bloatingSeekbar;
    private SeekBar crampsSeekbar;
    private SeekBar dizzinessSeekbar;
    private SeekBar spotsSeekbar;
    private SeekBar headachesSeekbar;
    private SeekBar insomniaSeekbar;
    private SeekBar sweatingSeekbar;
    private Button buttonSave1;
    int acneValue;
    int bloatingValue;
    int crampValue;
    int dizzinessValue;
    int spotsValue;
    int headacheValue;
    int insomniaValue;
    int sweatingValue;


    DatabaseReference databaseSymptoms;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2symptoms, container, false);

        acneSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar2); // make seekbar object
        bloatingSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar3);
        crampsSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar9);
        dizzinessSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar10);
        spotsSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar11);
        headachesSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar12);
        insomniaSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar13);
        sweatingSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar5);
        buttonSave1 = (Button) rootView.findViewById(R.id.button3);
        buttonSave1.setOnClickListener(getButton1OnClickListener());
        databaseSymptoms = FirebaseDatabase.getInstance().getReference("symptoms");
        acneSeekbar.setMax(10);
        bloatingSeekbar.setMax(10);
        crampsSeekbar.setMax(10);
        dizzinessSeekbar.setMax(10);
        spotsSeekbar.setMax(10);
        headachesSeekbar.setMax(10);
        insomniaSeekbar.setMax(10);
        sweatingSeekbar.setMax(10);

        acneSeekbar.setOnSeekBarChangeListener(this);
        bloatingSeekbar.setOnSeekBarChangeListener(this);
        crampsSeekbar.setOnSeekBarChangeListener(this);
        dizzinessSeekbar.setOnSeekBarChangeListener(this);
        spotsSeekbar.setOnSeekBarChangeListener(this);
        headachesSeekbar.setOnSeekBarChangeListener(this);
        insomniaSeekbar.setOnSeekBarChangeListener(this);
        sweatingSeekbar.setOnSeekBarChangeListener(this);

        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        System.out.println(seekBar.toString()+" "+ progress);


           // Toast.makeText(Tab2Symptoms.this.getActivity() ,"you should enter a value", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    //declaring OnClickListener as an object
    private View.OnClickListener getButton1OnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                // Date dateobj = new Date();
                Calendar cal = Calendar.getInstance();
                String todaysDate = df.format(cal.getTime());
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userID= currentFirebaseUser.getUid();
                // Toast.makeText(Tab1Notes.this.getActivity(), "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                // System.out.println(df.format(cal.getTime()));
                acneValue= acneSeekbar.getProgress();
                bloatingValue=bloatingSeekbar.getProgress();
                crampValue=crampsSeekbar.getProgress();
                dizzinessValue=dizzinessSeekbar.getProgress();
                spotsValue=spotsSeekbar.getProgress();
                headacheValue=headachesSeekbar.getProgress();
                insomniaValue = insomniaSeekbar.getProgress();
                sweatingValue=sweatingSeekbar.getProgress();


                String id= databaseSymptoms.push().getKey();
                Symptom mySymptom = new Symptom(id,acneValue, bloatingValue, crampValue, dizzinessValue, spotsValue, headacheValue, insomniaValue, sweatingValue, todaysDate, userID);
                databaseSymptoms.child(id).setValue(mySymptom);
                Toast.makeText(Tab2Symptoms.this.getActivity() ,"value added", Toast.LENGTH_LONG).show();
            }
        };
    }
}