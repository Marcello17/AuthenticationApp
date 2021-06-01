package com.javasampleapproach.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText username,password;
    Button btnCreateNew;
    Button btnSignIn;



    String enterUsername, enterPassword;
    UserDetails details;

    private FirebaseAuth myAuth;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    List<String> UserList;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username     = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        btnSignIn = (Button) findViewById(R.id.signIn);
        btnCreateNew = (Button)findViewById(R.id.create);

        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, CreateNewUser.class);
                startActivity(intent);
            }
        });





        myAuth = FirebaseAuth.getInstance();





        //Sign in section when the sign in button is clicked
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                enterUsername= username.getText().toString().trim();
                enterPassword = password.getText().toString().trim();
                myAuth.signInWithEmailAndPassword(enterUsername,enterPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "User" + myAuth.getCurrentUser().getEmail() +
                                            " Login success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,UserProfile.class);
                                    startActivity(intent);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }



}