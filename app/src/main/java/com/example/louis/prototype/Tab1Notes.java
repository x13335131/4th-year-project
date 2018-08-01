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
import java.util.Calendar;

/**
 * Created by louis on 01/11/2017.
 */

public class Tab1Notes extends Fragment {
    EditText editTextNote;
    Button buttonSave;
    String note;
    DatabaseReference databaseNotes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1notes, container, false);
        editTextNote = (EditText) rootView.findViewById(R.id.typeHereEt);
        buttonSave = (Button) rootView.findViewById(R.id.saveNoteBtn);

        buttonSave.setOnClickListener(getButtonOnClickListener());
        databaseNotes = FirebaseDatabase.getInstance().getReference("notes");
        return rootView;
    }
    //declaring OnClickListener as an object
    private View.OnClickListener getButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                note = editTextNote.getText().toString().trim();
                if(note.isEmpty()){
                    Toast.makeText(Tab1Notes.this.getActivity(), "Please enter text into text area.", Toast.LENGTH_LONG).show();
                }else {
                    addNote();
                }
            }
        };
    }
    private void addNote(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
       // Date dateobj = new Date();
        Calendar cal = Calendar.getInstance();
        String todaysDate = df.format(cal.getTime());

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID= currentFirebaseUser.getUid();
        // Toast.makeText(Tab1Notes.this.getActivity(), "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
       // System.out.println(df.format(cal.getTime()));
        if(!TextUtils.isEmpty(note)){
            String id= databaseNotes.push().getKey();
            Note myNote = new Note(id,note, todaysDate, userID);
            databaseNotes.child(id).setValue(myNote);
            Toast.makeText(Tab1Notes.this.getActivity() ,"note added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(Tab1Notes.this.getActivity() ,"you should enter a note", Toast.LENGTH_LONG).show();
        }
    }
}
