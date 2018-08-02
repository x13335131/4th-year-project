package com.example.louis.prototype;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by louis on 25/06/2018.
 */

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.ViewHolder>{

    public List<ForumPost> post_list;
    String dateString;
    private Context context;
    DatabaseReference databaseUsername;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase database;
    DatabaseReference forumComments, forumPosts;


    public ForumRecyclerAdapter(List<ForumPost> post_list){
        this.post_list = post_list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item,parent, false);
        context = parent.getContext();
        database = FirebaseDatabase.getInstance();
        databaseUsername = FirebaseDatabase.getInstance().getReference("username");
        firebaseAuth = FirebaseAuth.getInstance();
        synchronized(post_list){
            System.out.println("in here");
            post_list.notify();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        String content_data = post_list.get(position).getPost();
        System.out.println("postList: "+post_list.toString());
        holder.setContextText(content_data);
        final String forumPostId = post_list.get(position).ForumPostId;
        System.out.println("Forum post id:"+forumPostId);
        final String forumUserId = post_list.get(position).getUserID();
        System.out.println("post_list_user id: "+forumUserId);
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        if(forumUserId.equals(currentUserId)){
            holder.post_delete_btn.setEnabled(true);
            holder.post_delete_btn.setVisibility(View.VISIBLE);
        }
        final Query userQuery = databaseUsername.orderByChild("userID").equalTo(forumUserId);


       userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String userName = dataSnapshot.child("username").getValue().toString();

                    holder.setUsernameText(userName);

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
      /*  System.out.println("TESTING");
        long millisecond = post_list.get(position).getTimestamp().getTime();
        System.out.println("long: "+millisecond);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        holder.setDateText(dateString);*/

        ////


        long millisecond = post_list.get(position).getTimestamp().getTime();

        System.out.println("long: "+millisecond);
        Date d = new Date(millisecond);
        System.out.println("date "+d.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth=mMonth+1;
        System.out.println("day: "+mDay+" month "+mMonth+" year "+mYear);
        String dateString = +mDay+"/"+mMonth+"/"+mYear;
        holder.setDateText(dateString);
    //    android.text.format.DateFormat df = new android.text.format.DateFormat();
    //   dateString= df.format("dd/MM/yyyy hh:mm:ss", millisecond).toString();
      //  String dateString = DateFormat.getInstance().format(new Date(millisecond)).toString();
        //android.text.format.DateFormat df = new android.text.format.DateFormat();
       // String ds = df.format("MM/dd/yyyy", new Date(millisecond)).toString();
       //DateFormat df = new SimpleDateFormat("HH:mm:ss", new Date(millisecond));
       /* Date d = new Date(millisecond);
        System.out.println("testing 1 ");
        String ds = d.toString();*/

      ///  String epochString = "1081157732";
      //  long epoch = Long.parseLong( epochString );
      /*  Date d = new Date(millisecond * 1000 );
        String ds = d.toString();
        System.out.println("testing 2");

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sfd.format(new Date(millisecond));
        String ds= sfd.toString();
        holder.setDateText(ds);*/
///////////
        //Get Comments
        //Get Likes Count
        forumComments = database.getReferenceFromUrl("https://mymentalhealthtracker.firebaseio.com/post/"+forumPostId +"/comments");
        System.out.println("FORUM POST ID "+forumPostId);
        forumComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("post snapshot key: "+dataSnapshot.getKey().toString());
                   if(dataSnapshot.exists()){
                       int count = (int) dataSnapshot.getChildrenCount();

                       holder.updateCommentsCount(count);
                   }else{
                       holder.updateCommentsCount(0);
                   }
             //   }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /////////
        holder.forumCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                System.out.println("FORUM POST ID "+forumPostId);
                commentIntent.putExtra("forum_post_id", forumPostId);
                context.startActivity(commentIntent);

            }
        });

        holder.post_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forumPosts = database.getReferenceFromUrl("https://mymentalhealthtracker.firebaseio.com/post/");

                final Task<Void> deleteUserForumPost = forumPosts.child(forumPostId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        post_list.remove(position);

                    }
                });

        }
    });
    }
//comments feature
    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView contentView;
        private TextView usernameView;
        private TextView dateView;
        private TextView commentCount;
        private Button forumCommentBtn;
        Button post_delete_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            forumCommentBtn= (Button) mView.findViewById(R.id.forum_commentBtn);
            post_delete_btn = (Button) mView.findViewById(R.id.post_delete_btn);
        }
        public void setContextText(String text){
            contentView = (TextView) mView.findViewById(R.id.post_content);
            contentView.setText(text);

        }
        public void setUsernameText(String text){
            usernameView = (TextView) mView.findViewById(R.id.post_user_name);
            usernameView.setText(text);
        }
        public void setDateText(String text){
            dateView = (TextView) mView.findViewById(R.id.post_date);
            dateView.setText(text);
        }
        public void updateCommentsCount(int count){

            commentCount = (TextView) mView.findViewById(R.id.forum_comment_count);
            commentCount.setText(count + " Comments");

        }
    }

}
