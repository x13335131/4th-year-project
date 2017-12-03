package com.example.louis.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.louis.prototype.R.id.checkBox;

/**
 * Created by louis on 01/11/2017.
 */

public class Tab3Moods extends Fragment {
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;
    private CheckBox checkBox7;
    private CheckBox checkBox8;
    private CheckBox checkBox9;
    private CheckBox checkBox10;
    private CheckBox checkBox11;
    private CheckBox checkBox12;
    private CheckBox checkBox13;
    private CheckBox checkBox14;
    private CheckBox checkBox15;
    private CheckBox checkBox16;
    private CheckBox checkBox17;
    private CheckBox checkBox18;
    private CheckBox checkBox19;
    private CheckBox checkBox20;
    private CheckBox checkBox21;
    private boolean checkbox1Value;
    private boolean checkbox2Value;
    private boolean checkbox3Value;
    private boolean checkbox4Value;
    private boolean checkbox5Value;
    private Button buttonSave3;
    DatabaseReference databaseMoods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3moods, container, false);
        buttonSave3 = (Button) rootView.findViewById(R.id.button5);
        buttonSave3.setOnClickListener(getButton2OnClickListener());
        databaseMoods = FirebaseDatabase.getInstance().getReference("moods");
        checkBox1 = (CheckBox) rootView.findViewById(R.id.checkBox2);
        checkBox2= (CheckBox) rootView.findViewById(R.id.checkBox3);
        checkBox3= (CheckBox) rootView.findViewById(checkBox);
        checkBox4= (CheckBox) rootView.findViewById(R.id.checkBox4);
        checkBox5= (CheckBox) rootView.findViewById(R.id.checkBox5);
        checkBox6= (CheckBox) rootView.findViewById(R.id.checkBox6);
        checkBox7= (CheckBox) rootView.findViewById(R.id.checkBox7);
        checkBox8= (CheckBox) rootView.findViewById(R.id.checkBox11);
        checkBox9= (CheckBox) rootView.findViewById(R.id.checkBox9);
        checkBox10= (CheckBox) rootView.findViewById(R.id.checkBox10);
        checkBox11 = (CheckBox) rootView.findViewById(R.id.checkBox8);
        checkBox12= (CheckBox) rootView.findViewById(R.id.checkBox16);
        checkBox13= (CheckBox) rootView.findViewById(R.id.checkBox14);
        checkBox14= (CheckBox) rootView.findViewById(R.id.checkBox17);
        checkBox15= (CheckBox) rootView.findViewById(R.id.checkBox13);
        checkBox16= (CheckBox) rootView.findViewById(R.id.checkBox18);
        checkBox17= (CheckBox) rootView.findViewById(R.id.checkBox19);
        checkBox18= (CheckBox) rootView.findViewById(R.id.checkBox20);
        checkBox19= (CheckBox) rootView.findViewById(R.id.checkBox21);
        checkBox20= (CheckBox) rootView.findViewById(R.id.checkBox22);
        checkBox21= (CheckBox) rootView.findViewById(R.id.checkBox24);
        checkbox1Value = false;
        checkbox2Value = false;
        checkbox3Value = false;
        checkbox4Value = false;
        checkbox5Value = false;

        return rootView;
    }
    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        System.out.println("box clicked");

    }
    //declaring OnClickListener as an object
    private View.OnClickListener getButton2OnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                // Date dateobj = new Date();
                Calendar cal = Calendar.getInstance();
                String todaysDate = df.format(cal.getTime());
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userID= currentFirebaseUser.getUid();

                if(checkBox1.isChecked()){
                    checkbox1Value=true;
                }
                if(checkBox2.isChecked()){
                    checkbox2Value=true;
                }
                if(checkBox3.isChecked()){
                    checkbox3Value=true;
                }
                if(checkBox4.isChecked()){
                    checkbox4Value=true;
                }
                if(checkBox5.isChecked()){
                    checkbox5Value=true;
                }


                String id= databaseMoods.push().getKey();
                Mood myMood = new Mood(id, checkbox1Value, checkbox2Value, checkbox3Value, checkbox4Value, checkbox5Value, todaysDate, userID);
                databaseMoods.child(id).setValue(myMood);
                Toast.makeText(Tab3Moods.this.getActivity() ,"moods added", Toast.LENGTH_LONG).show();
            }
        };
    }
}