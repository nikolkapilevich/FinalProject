package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  {

    EditText userEmail,password;
    TextView signupText;
    Button loginButton;
    FirebaseAuth auth;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText=(TextView)findViewById(R.id.signupText);

        serviceIntent = new Intent(getApplicationContext(),MyService.class);
        startService(new Intent(getApplicationContext(),MyService.class));


        auth = FirebaseAuth.getInstance();


        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this,"intent successfully",Toast.LENGTH_LONG).show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = userEmail.getText().toString();
                String pass = password.getText().toString();

                if (!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()){
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(user,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this,"Login successfully",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, HomePage.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(MainActivity.this,"enter all the details",Toast.LENGTH_LONG).show();
                    }

                } else if (user.isEmpty()){
                    Toast.makeText(MainActivity.this,"enter all the details",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"enter valid email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}