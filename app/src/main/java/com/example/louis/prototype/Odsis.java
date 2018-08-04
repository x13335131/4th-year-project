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

public class Odsis extends AppCompatActivity {
    private SeekBar seekbar1,seekbar2,seekbar3,seekbar4,seekbar5;
    private int seek1Value;
    private int seek2Value;
    private int seek3Value;
    private int seek4Value;
    private int seek5Value;
    private int totalOdsisValue;
    private String todaysDate;
    DatabaseReference databaseOdsis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odsis);

        seekbar1 = (SeekBar)findViewById(R.id.answer1Odsis);
        seekbar2 = (SeekBar)findViewById(R.id.answer2Odsis);
        seekbar3 = (SeekBar)findViewById(R.id.answer3Odsis);
        seekbar4 = (SeekBar)findViewById(R.id.answer4Odsis);
        seekbar5 = (SeekBar)findViewById(R.id.answer5Odsis);
        Button submit = (Button)findViewById(R.id.submitOdsis); //to oasis and odsis
        databaseOdsis = FirebaseDatabase.getInstance().getReference("odsis");
        //set maximum value
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
                totalOdsisValue= seek1Value+seek2Value+seek3Value+seek4Value+seek5Value;
                String id= databaseOdsis.push().getKey();
                OdsisGS myOdsis = new OdsisGS(totalOdsisValue, todaysDate, currentuser);
                databaseOdsis.child(id).setValue(myOdsis);
                Toast.makeText(Odsis.this ,"odsis added", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
