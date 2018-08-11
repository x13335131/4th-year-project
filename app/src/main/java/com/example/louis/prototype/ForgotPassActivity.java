package com.example.louis.prototype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassActivity extends AppCompatActivity {

    EditText email;
    Button resetPass;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        email = (EditText) findViewById(R.id.enterEmailPassEt);
        resetPass = (Button) findViewById(R.id.forgotPassBtn);
        firebaseAuth = FirebaseAuth.getInstance();

       resetPass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                String useremail = email.getText().toString().trim();

                if(useremail.equals("")){
                    Toast.makeText(ForgotPassActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassActivity.this, "password reset email sent!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(ForgotPassActivity.this, "Error in sending email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
           }
       });
    }
}
