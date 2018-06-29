package com.example.louis.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {
    private EditText setupName;
    private Button setupBtn;
    private FirebaseAuth firebaseAuth;
    private String userid;
    DatabaseReference databaseUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_setup);
            Toolbar setupToolbar = (Toolbar) findViewById(R.id.setupToolbar);
            setSupportActionBar(setupToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Account Settings");

            firebaseAuth = FirebaseAuth.getInstance();

            userid = firebaseAuth.getCurrentUser().getUid();

            databaseUsername = FirebaseDatabase.getInstance().getReference("username");
            setupName = (EditText) findViewById(R.id.setup_name);
            setupBtn = (Button) findViewById(R.id.setup_btn);


            final Query userQuery = databaseUsername.orderByChild("userID").equalTo(userid);

            userQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             /*   if(dataSnapshot.exists()){
                    Toast.makeText(SetupActivity.this, "Data exists", Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(SetupActivity.this, "Data doesn't exist", Toast.LENGTH_LONG).show();
                }*/

               /* for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();*/
                    if (dataSnapshot.exists()) {
                        String value = dataSnapshot.child("username").getValue().toString();
                        setupName.setText(value);
                        Toast.makeText(SetupActivity.this, "Data exists", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(SetupActivity.this, "Data doesn't exist", Toast.LENGTH_LONG).show();
                    }
                  /*  if (value.equals(userid)) {
                        Toast.makeText(SetupActivity.this, "Data exists", Toast.LENGTH_LONG).show();
                    }*/

                }

                // }

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


            setupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user_name = setupName.getText().toString();

                    if (!TextUtils.isEmpty(user_name)) {

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String currentuser = currentFirebaseUser.getUid();

                        String id = databaseUsername.push().getKey();
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("username", user_name);
                        userMap.put("userID", currentuser);
                        databaseUsername.child(id).setValue(userMap);

                        Toast.makeText(SetupActivity.this, "username added", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainIntent);
                    }
                }
            });
        }

}
