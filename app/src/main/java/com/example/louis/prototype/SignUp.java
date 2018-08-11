package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity{
    private EditText et_email, et_password, et_cpassword;
    private String email, password, cpassword;
    Button regBtn;
    List<String> errorList = new ArrayList<String>();
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle(getString(R.string.app_reg));
        mAuth = FirebaseAuth.getInstance();
        et_email = (EditText) findViewById(R.id.emailEt);
        et_email.requestFocus();
        et_password = (EditText) findViewById(R.id.passEt);
        et_cpassword = (EditText) findViewById(R.id.confirmPassEt);
        regBtn = (Button) findViewById(R.id.registerBtn);
        regBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    register();//call when button is clicked to validate the input
                }
            });
    }
    public void register(){
        initialize(); //initialize the input to string variables
        if(!validate()){
            if(!errorList.isEmpty()){
                Toast.makeText(this,"Signup has failed"+errorList.toString(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Signup has failed", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendEmailVerification();
                        user.reload();
                        if(user.isEmailVerified()==true){
                            Toast.makeText(SignUp.this, "user email verified", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignUp.this, "email not verified", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"You Are Already Registered", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void sendEmailVerification() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, "Check email for verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    public boolean validate(){
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean valid = true;
        errorList.clear();
        if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError(Html.fromHtml("<font color='red'>"+getString(R.string.val_email)+"</font>"));
            valid = false;
        }
        if(password.isEmpty()){
            et_password.setError(Html.fromHtml("<font color='red'>"+getString(R.string.val_pass)+"</font>"));
            valid = false;
        }
        if (password.length() < 8) {
            errorList.add(""+getString(R.string.val_passNum));
            valid = false;
        }
        if (!UpperCasePatten.matcher(password).find()) {
            errorList.add("one uppercase character");
            valid=false;
        }
        if (!lowerCasePatten.matcher(password).find()) {
            errorList.add("one lowercase character");
            valid=false;
        }
        if (!digitCasePatten.matcher(password).find()) {
            errorList.add("one digit character");
            valid=false;
        }
        if(!password.matches(cpassword)){
            et_password.setError(Html.fromHtml("<font color='red'>passwords do not match</font>"));
            valid = false;
        }
        if(cpassword.isEmpty()|| !cpassword.matches(password)){
            et_cpassword.setError(Html.fromHtml("<font color='red'>passwords do not match</font>"));
            valid = false;
        }
        return valid;
    }
    public void initialize(){
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        cpassword = et_cpassword.getText().toString().trim();
    }
}
