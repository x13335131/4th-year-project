package com.example.louis.prototype;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private Toolbar commentToolbar;
    private EditText comment_field;
    private Button comment_post_btn;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    DatabaseReference forumComments;
    private String forum_post_id;
    private String currentUserId;
    private RecyclerView comment_list;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comments> commentsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentToolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setTitle("Comments");

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        forum_post_id = getIntent().getStringExtra("forum_post_id");
        System.out.println("forum post id: "+forum_post_id);
        comment_field = (EditText) findViewById(R.id.comment_field);
        comment_post_btn=(Button) findViewById(R.id.comment_post_btn);
        comment_list = (RecyclerView) findViewById(R.id.comment_list);
        //RecyclerView firebase
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentsRecyclerAdapter);

        forumComments = database.getReferenceFromUrl("https://mymentalhealthtracker.firebaseio.com/post/"+forum_post_id+"/comments");

      forumComments.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                  String commentId = postSnapshot.getKey();
                  Comments comments = postSnapshot.getValue(Comments.class);
                  commentsList.add(comments);
                  System.out.println("commentlist size:" + commentsList.size() + " user id " + comments.getUserID() + " timestamp " + comments.getTimestamp());
                  // ForumPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                  //     ForumPost fp = postSnapshot.getValue(ForumPost.class).withId(forumPostId);
                  ////     post_list.add(fp);
                  //     forumRecyclerAdapter.notifyDataSetChanged();
              }

          }
          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
        comment_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment_message = comment_field.getText().toString();
                System.out.println("comment message: "+comment_message);
                //String dateString = todayDate.toString();

                if(!comment_message.isEmpty()){
                    Date todayDate = Calendar.getInstance().getTime();
                    String id = forumComments.push().getKey();
                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("comment", comment_message);
                    commentsMap.put("userID", currentUserId);
                    commentsMap.put("timestamp", todayDate);
                    forumComments.child(id).setValue(commentsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this, "Error posting comment: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CommentsActivity.this, "comment added", Toast.LENGTH_SHORT).show();
                               // comment_field.setText("");
                            }
                        }
                    });

                }
            }
        });
    }
}
