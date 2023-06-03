package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {
    EditText regisEmail,regisPassword ,regisPhone;
    Button regisButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        regisEmail=findViewById(R.id.regisEmail);
        regisPassword=findViewById(R.id.regisPassword);
        regisPhone=findViewById(R.id.regisPhone);
        regisButton=findViewById(R.id.regisButton);

        auth = FirebaseAuth.getInstance();

        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = regisEmail.getText().toString();
                String password = regisPassword.getText().toString();
                String phone = regisPhone.getText().toString();

                if (regisEmail.getText().toString().isEmpty() || regisPassword.getText().toString().isEmpty() || regisPhone.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterPage.this,"Please enter all the details",Toast.LENGTH_LONG).show();

                }
                else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterPage.this, "sign up successful", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(RegisterPage.this, HomePage.class));
                            } else {
                                Toast.makeText(RegisterPage.this, "sign up failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

              //  Toast.makeText(RegisterPage.this,"intent successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}