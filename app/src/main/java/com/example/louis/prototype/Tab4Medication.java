package com.example.louis.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by louis on 01/11/2017.
 */

public class Tab4Medication extends Fragment {
    EditText editTextMed;
    EditText editTextMedDosage;
    TextView addedMedsTv;
    Button buttonSave;
    FirebaseDatabase database;
    String userID;
    String med;
    String medDosageAmount;
    double medDosage;

    String formattedMedList;
    ArrayList<String> medicationList;
    String medDate;
    String subDate;
    String todaysDate;
    DatabaseReference databaseMed;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4medication, container, false);

        database = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //current user id~access specific info
        userID = currentFirebaseUser.getUid();
        System.out.println("sub date "+subDate);
        databaseMed = database.getReference("meds");
        editTextMed = (EditText) rootView.findViewById(R.id.enterMedEt);
        editTextMedDosage = (EditText) rootView.findViewById(R.id.enterNumMedEt);
        addedMedsTv = (TextView) rootView.findViewById(R.id.addedMedTv);
        buttonSave = (Button) rootView.findViewById(R.id.addBtn);

        medicationList = new ArrayList<String>();
        buttonSave.setOnClickListener(getButtonOnClickListener());
        databaseMed = FirebaseDatabase.getInstance().getReference("meds");

        displayAddedMeds();
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

    private void displayAddedMeds(){



        final Query medQuery = databaseMed.orderByChild("userID").equalTo(userID);

        medQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        // Date dateobj = new Date();
                        Calendar cal = Calendar.getInstance();
                        todaysDate = df.format(cal.getTime());

                        subDate=todaysDate.substring(0,10);//date part only
                        //getting key & value from db
                        String key = child.getKey().toString();
                        String value = child.getValue().toString();


                        System.out.println("******************LOOPINGGGGGG ***********************");
                        System.out.println("med key: "+key+" med value: "+value);
                        if (key.equals("medDate") && value.contains(subDate.toString())){ //&& value.contains(date)) {
                            medDate= child.getValue().toString();
                            System.out.println("med Date: "+medDate);

                        }
                        if (key.equals("medDosage")&& medDate!=null) {
                            medDosageAmount = child.getValue().toString();

                            System.out.println("med dosage: "+medDosageAmount);

                        }
                        if (key.equals("medName") && medDate!=null) {
                            med = child.getValue().toString();

                            System.out.println("med: "+med);

                        }
                        System.out.println("med date: "+medDate+" subdate "+subDate);
                        System.out.println("MED: "+med+" med dosage: "+medDosageAmount+" date: "+medDate);
                        if((med!=""&& med!=null)&& (medDosageAmount!=""&& medDosageAmount!=null) && medDate!=null) {
                            String dateTime=medDate.substring(10,16);//date part only
                            System.out.println("med in TAB: "+med+" med Date "+ medDate+" sub date:"+dateTime+" dosage: "+ medDosageAmount);
                            //locLength = loc+" Length: "+panicLength;
                            String medInfo = med+" Dosage: "+medDosageAmount +" Time: "+dateTime;
                            //System.out.println("locLength "+locLength);
                            medicationList.add(medInfo);
                            med="";
                            medDosageAmount="";
                        }
                    }
                    if(!medicationList.isEmpty()) {
                        formattedMedList = medicationList.toString()
                                .replace("[", "<br/>\u2022")  //remove the left bracket
                                .replace("]", "")  //remove the right bracket
                                .replace(",", "<br/>\u2022")  //remove commas
                                .trim();           //remove trailing spaces from partially initialized arrays

                        addedMedsTv.setText(Html.fromHtml("<b> medication already added: </b><br/>" + formattedMedList));
                        //medicationList.clear();
                    }

                } catch(Exception e){
                    System.out.println(e);
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
    private void addMedication(){
        String med = editTextMed.getText().toString().trim();
        String etMedDosage = editTextMedDosage.getText().toString();
        if(!etMedDosage.isEmpty()) {
            medDosage = Double.parseDouble(editTextMedDosage.getText().toString());
        }else{
            medDosage=0;
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Date dateobj = new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String todaysDate = df.format(cal.getTime());

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID= currentFirebaseUser.getUid();
        // Toast.makeText(Tab1Notes.this.getActivity(), "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        // System.out.println(df.format(cal.getTime()));
        if(!TextUtils.isEmpty(med) && medDosage!=0){
            String id= databaseMed.push().getKey();
            Med myMed = new Med(id,med,medDosage, todaysDate, userID);
            databaseMed.child(id).setValue(myMed);
            Toast.makeText(Tab4Medication.this.getActivity() ,"medication added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(Tab4Medication.this.getActivity() ,"please enter todays medication", Toast.LENGTH_LONG).show();
        }
    }
}