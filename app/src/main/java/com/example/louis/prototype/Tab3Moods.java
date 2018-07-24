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

public class Tab3Moods extends Fragment {
    private CheckBox afraidCb;
    private CheckBox aggrevatedCb;
    private CheckBox angryCb;
    private CheckBox anxiousCb;
    private CheckBox awkwardCb;
    private CheckBox braveCb;
    private CheckBox calmCb;
    private CheckBox confindentCb;
    private CheckBox contentCb;
    private CheckBox depressedCb;
    private CheckBox discouragedCb;
    private CheckBox distantCb;
    private CheckBox energizedCb;
    private CheckBox fatiguedCb;
    private CheckBox gloomyCb;
    private CheckBox grumpyCb;
    private CheckBox grouchyCb;
    private CheckBox happyCb;
    private CheckBox hesitantCb;
    private CheckBox impatientCb;
    private CheckBox insecureCb;
    private boolean afraidCbValue;
    private boolean aggrevatedCbValue;
    private boolean angryCbValue;
    private boolean anxiousCbValue;
    private boolean awkwardCbValue;
    private boolean braveCbValue;
    private boolean calmCbValue;
    private boolean confidentCbValue;
    private boolean contentCbValue;
    private boolean depressedCbValue;
    private boolean discouragedCbValue;
    private boolean distantCbValue;
    private boolean energizedCbValue;
    private boolean fatiguedCbValue;
    private boolean gloomyCbValue;
    private boolean grumpyCbValue;
    private boolean grouchyCbValue;
    private boolean happyCbValue;
    private boolean hesitantCbValue;
    private boolean impatientCbValue;
    private boolean insecureCbValue;

    String todaysDate;
    private Button saveBtn;
    DatabaseReference databaseMoods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3moods, container, false);
        saveBtn = (Button) rootView.findViewById(R.id.submitBtn);
        saveBtn.setOnClickListener(getButton2OnClickListener());
        databaseMoods = FirebaseDatabase.getInstance().getReference("moods");
        afraidCb = (CheckBox) rootView.findViewById(R.id.afraidCb);
        aggrevatedCb = (CheckBox) rootView.findViewById(R.id.aggrevatedCb);
        angryCb = (CheckBox) rootView.findViewById(R.id.angryCb);
        anxiousCb = (CheckBox) rootView.findViewById(R.id.anxiousCb);
        awkwardCb = (CheckBox) rootView.findViewById(R.id.awkwardCb);
        braveCb = (CheckBox) rootView.findViewById(R.id.braveCb);
        calmCb = (CheckBox) rootView.findViewById(R.id.calmCb);
        confindentCb = (CheckBox) rootView.findViewById(R.id.confidentCb);
        contentCb = (CheckBox) rootView.findViewById(R.id.contentCb);
        depressedCb = (CheckBox) rootView.findViewById(R.id.depressedCb);
        discouragedCb = (CheckBox) rootView.findViewById(R.id.discouragedCb);
        distantCb = (CheckBox) rootView.findViewById(R.id.distantCb);
        energizedCb = (CheckBox) rootView.findViewById(R.id.energizedCb);
        fatiguedCb = (CheckBox) rootView.findViewById(R.id.fatiguedCb);
        gloomyCb = (CheckBox) rootView.findViewById(R.id.gloomyCb);
        grumpyCb = (CheckBox) rootView.findViewById(R.id.grumpyCb);
        grouchyCb = (CheckBox) rootView.findViewById(R.id.grouchyCb);
        happyCb = (CheckBox) rootView.findViewById(R.id.happyCb);
        hesitantCb = (CheckBox) rootView.findViewById(R.id.hesitantCb);
        impatientCb = (CheckBox) rootView.findViewById(R.id.impatientCb);
        insecureCb = (CheckBox) rootView.findViewById(R.id.insecureCb);
        afraidCbValue = false;
        aggrevatedCbValue = false;
        angryCbValue = false;
        anxiousCbValue = false;
        awkwardCbValue = false;
        braveCbValue = false;
        calmCbValue = false;
        confidentCbValue = false;
        contentCbValue=false;;
        depressedCbValue= false;
       discouragedCbValue= false;
        distantCbValue= false;
        energizedCbValue= false;
       fatiguedCbValue= false;
       gloomyCbValue= false;
        grumpyCbValue= false;
       grouchyCbValue= false;
        happyCbValue= false;
        hesitantCbValue= false;
        impatientCbValue= false;
       insecureCbValue= false;
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
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                // Date dateobj = new Date();
                Calendar cal = Calendar.getInstance();
                todaysDate = df.format(cal.getTime());
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userID = currentFirebaseUser.getUid();
                System.out.println("yo todayssssss date " + todaysDate);

                final String subDate = todaysDate.substring(0, 10);//date part only
                System.out.println("sub date " + subDate);
                if (afraidCb.isChecked()) {
                    afraidCbValue = true;
                }
                if (aggrevatedCb.isChecked()) {
                    aggrevatedCbValue = true;
                }
                if (angryCb.isChecked()) {
                    angryCbValue = true;
                }
                if (anxiousCb.isChecked()) {
                    anxiousCbValue = true;
                }
                if (awkwardCb.isChecked()) {
                    awkwardCbValue = true;
                }
                if (braveCb.isChecked()) {
                    braveCbValue = true;
                }
                if (calmCb.isChecked()) {
                    calmCbValue = true;
                }
                if (confindentCb.isChecked()) {
                    confidentCbValue = true;
                }
                if (contentCb.isChecked()) {
                    contentCbValue = true;
                }
                if (depressedCb.isChecked()) {
                    depressedCbValue = true;
                }
                if (discouragedCb.isChecked()) {
                    discouragedCbValue = true;
                }
                if (distantCb.isChecked()) {
                    distantCbValue = true;
                }
                if (energizedCb.isChecked()) {
                    energizedCbValue = true;
                }
                if (fatiguedCb.isChecked()) {
                    fatiguedCbValue = true;
                }
                if (gloomyCb.isChecked()) {
                    gloomyCbValue = true;
                }
                if (grumpyCb.isChecked()) {
                    grumpyCbValue = true;
                }
                if (grouchyCb.isChecked()) {
                    grouchyCbValue = true;
                }
                if (happyCb.isChecked()) {
                    happyCbValue = true;
                }
                if (hesitantCb.isChecked()) {
                    hesitantCbValue = true;
                }
                if (impatientCb.isChecked()) {
                    impatientCbValue = true;
                }
                if (insecureCb.isChecked()) {
                    insecureCbValue = true;
                }

                if (afraidCbValue== false &&
                    aggrevatedCbValue== false &&
                    angryCbValue== false  &&
                    anxiousCbValue== false &&
                    awkwardCbValue== false &&
                    braveCbValue== false &&
                    calmCbValue== false &&
                    confidentCbValue== false &&
                    contentCbValue== false &&
                    depressedCbValue== false &&
                    discouragedCbValue== false &&
                    distantCbValue== false &&
                    energizedCbValue== false &&
                    fatiguedCbValue == false &&
                    gloomyCbValue == false &&
                    grumpyCbValue == false &&
                    grouchyCbValue == false &&
                    happyCbValue== false &&
                    hesitantCbValue== false&&
                    impatientCbValue== false&&
                    insecureCbValue == false) {

                    Toast.makeText(Tab3Moods.this.getActivity(), "please enter a value", Toast.LENGTH_LONG).show();
                } else {

                    final Query panicQuery = databaseMoods.orderByChild("userID").equalTo(userID);

                    panicQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            try {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    getDate();
                                    //getting key & value from db
                                    String key = child.getKey().toString();
                                    String value = child.getValue().toString();

                                    System.out.println("test child value: " + value);
                                    System.out.println("test subdate: " + subDate);
                                    System.out.println("test todays date: " + todaysDate);
                                    if (key.equals("symptomDate") && value.contains(subDate) && !value.equals(todaysDate)) {
                                        //  System.out.println("databasemoods parent "+databaseMoods.getParent());
                                        System.out.println("datasnapshot key " + dataSnapshot.getKey().toString());

                                        String dskey = dataSnapshot.getKey().toString();
                                        System.out.println("database moods key " + databaseMoods.getKey() + "value: " + dataSnapshot.getValue());
                                        //   Object o = dataSnapshot;
                                        System.out.println("database child " + databaseMoods.child(dskey));
                                        databaseMoods.child(dskey).removeValue();
                                        System.out.println("CHILD KEY " + child.toString());
                                        // break;
                                    } else {
                                        System.out.println("nahhh");
                                    }

                                }

                            } catch (Exception e) {
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
                    String id = databaseMoods.push().getKey();
                    Mood myMood = new Mood(id, afraidCbValue, aggrevatedCbValue, angryCbValue, anxiousCbValue, awkwardCbValue, braveCbValue, calmCbValue,
                            confidentCbValue, contentCbValue, depressedCbValue, discouragedCbValue,
                            distantCbValue, energizedCbValue, fatiguedCbValue, gloomyCbValue, grumpyCbValue, grouchyCbValue, happyCbValue, hesitantCbValue,
                            impatientCbValue, insecureCbValue, todaysDate, userID);
                    databaseMoods.child(id).setValue(myMood);
                    Toast.makeText(Tab3Moods.this.getActivity(), "moods added", Toast.LENGTH_LONG).show();
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