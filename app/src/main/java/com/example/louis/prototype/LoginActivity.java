package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText email ;
    EditText password;
    Button loginBtn, regBtn;
    String e,p;
    TextView attemptCounterTv;
    List<String> errorList = new ArrayList<String>();
    int counter = 3;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailEt);
        email.requestFocus();
        password = (EditText) findViewById(R.id.passwordEt);
        attemptCounterTv = (TextView)findViewById(R.id.attemptsNumTv);
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
                        Toast.makeText(getApplicationContext(),
                                "Redirecting...", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);//not working
                        startActivity(intent1);
                        finish();
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
            email.setError("Please enter valid email address");
            valid = false;
        }
        if(p.isEmpty()){
            password.setError("please enter a valid password");
            valid = false;
        }
        return valid;
    }
    public void initialize(){
        e = email.getText().toString().trim();
        p = password.getText().toString().trim();

    }
}
