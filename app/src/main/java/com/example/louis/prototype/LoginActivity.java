package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText email ;
    EditText password;
    Button loginBtn, regBtn;
    String e,p;
    TextView attemptCounterTv;
    TextView forgotPassTv;
    List<String> errorList = new ArrayList<String>();
    int counter = 3;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    DatabaseReference usersDb;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        usersDb = database.getReference("users");
        email = (EditText) findViewById(R.id.emailEt);
        email.requestFocus();
        getSupportActionBar().setTitle(getString(R.string.app_login));
        password = (EditText) findViewById(R.id.passwordEt);
        attemptCounterTv = (TextView)findViewById(R.id.attemptsNumTv);
        forgotPassTv = (TextView) findViewById(R.id.forgotPassTv);
        attemptCounterTv.setVisibility(View.GONE);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });
        regBtn = (Button) findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      Intent regIntent = new Intent(getApplicationContext(), SignUp.class);
                                      startActivity(regIntent);
                                      finish();
                                  }
                              }
        );
        forgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassIntent = new Intent(getApplicationContext(), ForgotPassActivity.class);
                startActivity(forgotPassIntent);
                finish();
            }
        });
    }
    public void Login(){
        initialize(); //initialize the input to string variables
        if(!validate()){
            Toast.makeText(this,"Signup has failed", Toast.LENGTH_SHORT).show();
        }else {

            mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user=  FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()==true || e.equalsIgnoreCase("loulou@gmail.com") ||  e.equalsIgnoreCase("markomara@gmail.com") ) {
                            mAuth.getCurrentUser().getToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {
                                    String token_id = getTokenResult.getToken();
                                    String current_id = mAuth.getCurrentUser().getUid();

                                    Map<String, Object> tokenMap = new HashMap<>();
                                    tokenMap.put("token_id", token_id);

                                    usersDb.child(current_id).updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Redirecting...", Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);//not working
                                            startActivity(intent1);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }else{
                            Toast.makeText(LoginActivity.this, "please verify email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        attemptCounterTv.setVisibility(View.VISIBLE);
                        counter--;
                        attemptCounterTv.setText(Integer.toString(counter));
                        if (counter == 0) {
                            loginBtn.setEnabled(false);
                        }
                    }

                }
            });
        }
    }
    public boolean validate(){
        boolean valid = true;
        errorList.clear();
        if(e.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            email.setError(Html.fromHtml("<bgcolor='white'><font color='red'>Please enter valid email address</font></bgcolor>"));
            valid = false;
        }
        if(p.isEmpty()){
            password.setError(Html.fromHtml("<bgcolor='white'><font color='red'> please enter a valid password</font></bgcolor>"));
            valid = false;
        }
        return valid;
    }
    public void initialize(){
        e = email.getText().toString().trim();
        p = password.getText().toString().trim();

    }
}
