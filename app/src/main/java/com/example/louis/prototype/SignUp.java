package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity{
    private EditText et_email, et_password, et_cpassword;
    private String email, password, cpassword;
    Button regBtn;
    List<String> errorList = new ArrayList<String>();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
            Toast.makeText(this,"Signup has failed", Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Intent setupIntent = new Intent(SignUp.this, SetupActivity.class);
                        startActivity(setupIntent);
                        finish();
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
    public boolean validate(){
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean valid = true;
        errorList.clear();
        if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please enter valid email address");
            valid = false;
        }
        if(password.isEmpty()){
            et_password.setError("please enter a valid password");
            valid = false;
        }
        if (password.length() < 8) {
            errorList.add("8 characters");
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
            et_password.setError("passwords do not match");
            valid = false;
        }
        if(cpassword.isEmpty()|| !cpassword.matches(password)){
            et_cpassword.setError("passwords do not match");
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
