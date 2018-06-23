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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.lang.Object;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar newPostToolbar;

    private FirebaseAuth firebaseAuth;

    private String userid;
    private EditText newPostText;
    private Button newPostBtn;

    DatabaseReference databasePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        firebaseAuth = FirebaseAuth.getInstance();

        userid = firebaseAuth.getCurrentUser().getUid();

        databasePost = FirebaseDatabase.getInstance().getReference("post");
        newPostToolbar = (Toolbar) findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);

        getSupportActionBar().setTitle("Add new post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostText = (EditText) findViewById(R.id.new_post_text);
        newPostBtn = (Button) findViewById(R.id.post_btn);


        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postText = newPostText.getText().toString();

                if(!TextUtils.isEmpty(postText)){
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String currentuser = currentFirebaseUser.getUid();

                    String id= databasePost.push().getKey();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("post", postText);
                    userMap.put("userID", currentuser);
                    userMap.put("timestamp", ServerValue.TIMESTAMP);
                    databasePost.child(id).setValue(userMap);

                    Toast.makeText(NewPostActivity.this ,"post added", Toast.LENGTH_LONG).show();
                    Intent forumIntent = new Intent(getApplicationContext(), QandA.class);
                    startActivity(forumIntent);
                }

            }
        });
    }

}
