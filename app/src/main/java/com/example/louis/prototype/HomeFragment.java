package com.example.louis.prototype;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView post_list_view;
    private List<ForumPost> post_list;
    private ForumRecyclerAdapter forumRecyclerAdapter;
    DatabaseReference databasePost;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        post_list = new ArrayList<>();
        post_list_view = (RecyclerView) view.findViewById(R.id.post_list_view);
        forumRecyclerAdapter = new ForumRecyclerAdapter(post_list);
        post_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        post_list_view.setAdapter(forumRecyclerAdapter);
        databasePost = FirebaseDatabase.getInstance().getReference("post");

        Query firstQuery = databasePost.orderByChild("timestamp");//.limitToLast(3);
        firstQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    String forumPostId = postSnapshot.getKey();
                    // ForumPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                    ForumPost fp = postSnapshot.getValue(ForumPost.class).withId(forumPostId);
                    //ForumPost fp = postSnapshot.getValue(ForumPost.class);
                    post_list.add(fp);
                    forumRecyclerAdapter.notifyDataSetChanged();
                }
                Collections.reverse(post_list); //in decending order
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        databasePost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();

                    ForumPost forumPost = child.getValue(ForumPost.class);
                    post_list.add(forumPost);
                    //OasisGS myOasis = new OasisGS(totalOasisValue, todaysDate, currentuser);
                   // databaseOasis.child(id).setValue(myOasis);

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
        });*/
        // Inflate the layout for this fragment
        return view;
    }

    public void loadMorePosts(){
        Query nextQuery = databasePost.orderByChild("timestamp").limitToLast(3);
        nextQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){


                    String forumPostId = postSnapshot.getKey();
                   // ForumPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                    ForumPost fp = postSnapshot.getValue(ForumPost.class).withId(forumPostId);
                    post_list.add(fp);
                    forumRecyclerAdapter.notifyDataSetChanged();
                }
                Collections.reverse(post_list); //in decending order
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
