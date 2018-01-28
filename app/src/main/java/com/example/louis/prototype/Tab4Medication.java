package com.example.louis.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by louis on 01/11/2017.
 */

public class Tab4Medication extends Fragment {
    EditText editTextMed;
    EditText editTextMedDosage;
    Button buttonSave;

    DatabaseReference databaseMed;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4medication, container, false);
        editTextMed = (EditText) rootView.findViewById(R.id.editText4);
        editTextMedDosage = (EditText) rootView.findViewById(R.id.editText7);
        buttonSave = (Button) rootView.findViewById(R.id.button9);

        buttonSave.setOnClickListener(getButtonOnClickListener());
        databaseMed = FirebaseDatabase.getInstance().getReference("meds");
        return rootView;
    }

    //declaring OnClickListener as an object
    private View.OnClickListener getButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedication();
            }
        };
    }

    private void addMedication(){
        String med = editTextMed.getText().toString().trim();
        int medDosage = Integer.parseInt(editTextMedDosage.getText().toString());
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        // Date dateobj = new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String todaysDate = df.format(cal.getTime());

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID= currentFirebaseUser.getUid();
        // Toast.makeText(Tab1Notes.this.getActivity(), "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        // System.out.println(df.format(cal.getTime()));
        if(!TextUtils.isEmpty(med)){
            String id= databaseMed.push().getKey();
            Med myMed = new Med(id,med,medDosage, todaysDate, userID);
            databaseMed.child(id).setValue(myMed);
            Toast.makeText(Tab4Medication.this.getActivity() ,"medication added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(Tab4Medication.this.getActivity() ,"please enter todays medication", Toast.LENGTH_LONG).show();
        }
    }
}