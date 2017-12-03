package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email ;
    EditText password;
    Button b1,b2;

    TextView tx1;
    int counter = 3;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        b1 = (Button)findViewById(R.id.button12);
        email = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText9);

        //b2 = (Button)findViewById(R.id.button15);
        tx1 = (TextView)findViewById(R.id.textView32);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e = email.getText().toString().trim();
                final String p = password.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),
                                        "Redirecting...", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);//not working
                                startActivity(intent1);
                            }
                        else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            tx1.setVisibility(View.VISIBLE);
                            //tx1.setBackgroundColor(Color.RED);
                            counter--;
                            tx1.setText(Integer.toString(counter));

                            if (counter == 0) {
                                b1.setEnabled(false);
                            }
                        }
                    }
                });
            }
        });

       /* b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        final Button signupBtn = (Button) findViewById(R.id.button19);
        signupBtn.setOnClickListener(new View.OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      Intent i3 = new Intent(getApplicationContext(), SignUp.class);
                                      startActivity(i3);
                                  }
                              }
        );
    }
}
