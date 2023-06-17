package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

                if (email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(RegisterPage.this, "Please enter all the details", Toast.LENGTH_LONG).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration successful, create a separate Firestore database for the user
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("phone", phone);
                                userData.put("email",email);
                                userData.put("password",password);

                                db.collection("users").document(userId)
                                        .set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterPage.this, "Sign up successful", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterPage.this, HomePage.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterPage.this, "Sign up failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterPage.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }








   /*             String email = regisEmail.getText().toString();
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

    */

              //  Toast.makeText(RegisterPage.this,"intent successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}