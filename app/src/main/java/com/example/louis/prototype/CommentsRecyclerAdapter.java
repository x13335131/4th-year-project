package com.example.louis.prototype;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * Created by louis on 30/07/2018.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>{
    DatabaseReference databaseUsername;
    public List<Comments> commentsList;
    public Context context;
    private FirebaseAuth firebaseAuth;
    private String userid;

    public CommentsRecyclerAdapter(List<Comments> commentsList){
        this.commentsList = commentsList;
    }


    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
       context = parent.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsername = FirebaseDatabase.getInstance().getReference("username");
       return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        System.out.println("comment list "+commentsList);
        String commentMessage = commentsList.get(position).getComment();
       // String userName = commentsList.get(position).getUserID();
        System.out.println("comment message "+commentMessage);
        holder.setComment_message(commentMessage);
      //  holder.setComment_username(userName);
        final String userID = commentsList.get(position).getUserID();
        userid = firebaseAuth.getCurrentUser().getUid();

        final Query userQuery = databaseUsername.orderByChild("userID").equalTo(userID);


        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String userName = dataSnapshot.child("username").getValue().toString();

                    holder.setComment_username(userName);

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

    @Override
    public int getItemCount() {
        if(commentsList != null){
            return commentsList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private TextView comment_username;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_message(String message){

            comment_message = (TextView) mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }
        public void setComment_username(String username){

            comment_username = (TextView) mView.findViewById(R.id.comment_username);
            comment_username.setText(username);
        }

    }
}
