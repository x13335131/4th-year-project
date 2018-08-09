package com.example.louis.prototype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;

public class ForumActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar mainToolbar;

    private FirebaseAuth mAuth;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView mBottomNav;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_forum);
            mainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
            setSupportActionBar(mainToolbar);

            getSupportActionBar().setTitle(getString(R.string.forum_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

            //fragments
            homeFragment = new HomeFragment();
            notificationFragment = new NotificationFragment();
           // accountFragment = new AccountFragment();


            replaceFragment(homeFragment);

            mAuth = FirebaseAuth.getInstance();

            ///////////


            addPostBtn = (FloatingActionButton) findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newPostIntent = new Intent(ForumActivity.this, NewPostActivity.class);
                    startActivity(newPostIntent);

                }
            });
            mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // handle desired action here
                    // One possibility of action is to replace the contents above the nav bar
                    // return true if you want the item to be displayed as the selected item
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            replaceFragment(homeFragment);
                            return true;
                        case R.id.menu_notifications:
                            replaceFragment(notificationFragment);
                            return true;
                       /* case R.id.menu_account:
                            replaceFragment(accountFragment);
                            return true;*/

                        default:
                            return false;
                    }
                }
            });
        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.more_info_btn:
                Intent infoIntent = new Intent(ForumActivity.this, InformationActivity.class);
                startActivity(infoIntent);
                return true;

            case R.id.action_logout_btn:
                logOut();
                return true;

            case R.id.action_settings_btn:
                Intent settingsIntent = new Intent(ForumActivity.this, SetupActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(ForumActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }
}
