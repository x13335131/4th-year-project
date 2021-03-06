package com.example.louis.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Oasis extends AppCompatActivity {

    private SeekBar seekbar1,seekbar2,seekbar3,seekbar4,seekbar5;
    private int seek1Value;
    private int seek2Value;
    private int seek3Value;
    private int seek4Value;
    private int seek5Value;
    private int totalOasisValue;
    private String todaysDate;
    DatabaseReference databaseOasis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oasis);

        seekbar1 = (SeekBar)findViewById(R.id.answer1);
        seekbar2 = (SeekBar)findViewById(R.id.answer2);
        seekbar3 = (SeekBar)findViewById(R.id.answer3);
        seekbar4 = (SeekBar)findViewById(R.id.answer4);
        seekbar5 = (SeekBar)findViewById(R.id.answer5);
        Button submit = (Button)findViewById(R.id.submit); //to oasis and odsis
        databaseOasis = FirebaseDatabase.getInstance().getReference("oasis");
        seekbar1.setMax(5);
        seekbar2.setMax(5);
        seekbar3.setMax(5);
        seekbar4.setMax(5);
        seekbar5.setMax(5);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                // Date dateobj = new Date();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                todaysDate = df.format(cal.getTime());
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentuser = currentFirebaseUser.getUid();
                // Toast.makeText(Tab1Notes.this.getActivity(), "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                // System.out.println(df.format(cal.getTime()));
                seek1Value=seekbar1.getProgress();
                seek2Value=seekbar2.getProgress();
                seek3Value=seekbar3.getProgress();
                seek4Value=seekbar4.getProgress();
                seek5Value=seekbar5.getProgress();
                totalOasisValue= seek1Value+seek2Value+seek3Value+seek4Value+seek5Value;
                String id= databaseOasis.push().getKey();
                OasisGS myOasis = new OasisGS(totalOasisValue, todaysDate, currentuser);
                databaseOasis.child(id).setValue(myOasis);
                Toast.makeText(Oasis.this ,"oasis added", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Odsis.class);
                startActivity(i);
            }
        });
    }
}
