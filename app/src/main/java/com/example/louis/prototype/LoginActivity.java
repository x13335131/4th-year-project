package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username ;
    EditText password;
    Button b1,b2;

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button)findViewById(R.id.button12);
        username = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText9);

        //b2 = (Button)findViewById(R.id.button15);
        tx1 = (TextView)findViewById(R.id.textView32);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") &&
                        password.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i1);
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

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
