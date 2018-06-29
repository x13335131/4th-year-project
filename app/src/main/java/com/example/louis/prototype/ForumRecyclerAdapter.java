package com.example.louis.prototype;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private String userid;
    public ForumRecyclerAdapter(List<ForumPost> post_list){
        this.post_list = post_list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item,parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String content_data = post_list.get(position).getPost();
        holder.setContextText(content_data);

        String userID = post_list.get(position).getUserID();
        firebaseAuth = FirebaseAuth.getInstance();

        userid = firebaseAuth.getCurrentUser().getUid();

        databaseUsername = FirebaseDatabase.getInstance().getReference("username");
        final Query userQuery = databaseUsername.orderByChild("userID").equalTo(userID);

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
       /* System.out.println("TESTING");
        long millisecond = post_list.get(position).getTimestamp().getTime();
        System.out.println("long: "+millisecond);*/
        //String dateString = DateFormat.format("MM/dd/yyyy",new Date(millisecond)).toString();

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
       /* Date d = new Date(millisecond * 1000 );
        String ds = d.toString();
        System.out.println("testing 2");*/

        /*SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sfd.format(new Date(millisecond));
        String ds= sfd.toString();
        holder.setDateText(ds);*/
    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView contentView;
        private TextView usernameView;
        private TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

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
    }

}
