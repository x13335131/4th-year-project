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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by louis on 01/11/2017.
 */

public class Tab2Symptoms extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private SeekBar acneSeekbar, bloatingSeekbar, crampsSeekbar, dizzinessSeekbar, headachesSeekbar, insomniaSeekbar, spotsSeekbar, sweatingSeekbar;
    private int acneValue, bloatingValue, crampValue, dizzinessValue, headacheValue, insomniaValue, spotsValue, sweatingValue;
    private String todaysDate;
    private Button submitBtn;

    DatabaseReference databaseSymptoms;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2symptoms, container, false);

        acneSeekbar = (SeekBar) rootView.findViewById(R.id.acneSeekbar);
        bloatingSeekbar = (SeekBar) rootView.findViewById(R.id.bloatingSeekbar);
        crampsSeekbar = (SeekBar) rootView.findViewById(R.id.crampsSeekbar);
        dizzinessSeekbar = (SeekBar) rootView.findViewById(R.id.dizzinessSeekbar);
        spotsSeekbar = (SeekBar) rootView.findViewById(R.id.spotsSeekbar);
        headachesSeekbar = (SeekBar) rootView.findViewById(R.id.headacheSeekbar);
        insomniaSeekbar = (SeekBar) rootView.findViewById(R.id.insomniaSeekbar);
        sweatingSeekbar = (SeekBar) rootView.findViewById(R.id.sweatingSeekbar);
        submitBtn = (Button) rootView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(getButton1OnClickListener());
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
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                // Date dateobj = new Date();
                Calendar cal = Calendar.getInstance();
                todaysDate = df.format(cal.getTime());
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userID= currentFirebaseUser.getUid();

                final String subDate=todaysDate.substring(0,10);//date part only
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

                if( acneValue + bloatingValue + crampValue + dizzinessValue + spotsValue + headacheValue + insomniaValue + sweatingValue ==0){

                    Toast.makeText(Tab2Symptoms.this.getActivity(), "please enter a value", Toast.LENGTH_LONG).show();
                }else {
                final Query SymptomsQuery = databaseSymptoms.orderByChild("userID").equalTo(userID);

                SymptomsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        try{
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                getDate();
                                //getting key & value from db
                                String key = child.getKey().toString();
                                String value = child.getValue().toString();

                                System.out.println("test child value: "+value);
                                System.out.println("test subdate: "+subDate);
                                System.out.println("test todays date: "+todaysDate);
                                if (key.equals("symptomDate") && value.contains(subDate) && !value.equals(todaysDate)) {
                                    //  System.out.println("databasemoods parent "+databaseMoods.getParent());
                                    System.out.println("datasnapshot key "+dataSnapshot.getKey().toString());

                                    String dskey =dataSnapshot.getKey().toString();
                                    System.out.println("database symptoms key "+ databaseSymptoms.getKey()+ "value: "+dataSnapshot.getValue());
                                    //   Object o = dataSnapshot;
                                    System.out.println("database child "+databaseSymptoms.child(dskey));
                                    databaseSymptoms.child(dskey).removeValue();
                                    System.out.println("CHILD KEY "+child.toString());
                                    // break;
                                }else{
                                    System.out.println("nahhh");
                                }

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


                    String id = databaseSymptoms.push().getKey();
                    Symptom mySymptom = new Symptom(id, acneValue, bloatingValue, crampValue, dizzinessValue, spotsValue, headacheValue, insomniaValue, sweatingValue, todaysDate, userID);
                    databaseSymptoms.child(id).setValue(mySymptom);
                    Toast.makeText(Tab2Symptoms.this.getActivity(), "value added", Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    public void getDate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Date dateobj = new Date();
        Calendar cal = Calendar.getInstance();
        todaysDate = df.format(cal.getTime());
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID= currentFirebaseUser.getUid();
        System.out.println("yo todayssssss date "+todaysDate);

        final String subDate=todaysDate.substring(0,10);//date part only
        System.out.println("sub date "+subDate);
    }
}