package com.javasampleapproach.authenticationapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateNewUser extends AppCompatActivity {

    EditText username,password;
    Button btnAddUser;

    Button btnBack;     //Back to the login page

    String enterUsername, enterPassword;


    private FirebaseAuth myAuth;


    /*FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);


        username = (EditText)findViewById(R.id.new_username);
        password = (EditText)findViewById(R.id.new_password);


        btnAddUser = (Button)findViewById(R.id.add);
        btnBack = (Button)findViewById((R.id.Back));

        myAuth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CreateNewUser.this,MainActivity.class);
                startActivity(intent);
            }
        });


        btnAddUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enterUsername= username.getText().toString().trim();
                enterPassword = password.getText().toString().trim();
               /* details= new UserDetails(enterUsername,enterPassword);

                myRef.setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(CreateNew2.this, "Records Saved successfully to database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(CreateNew2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

                if(!AppPermissions.hasPermissions(CreateNew2.this,permissions))
                {
                    ActivityCompat.requestPermissions(CreateNew2.this,permissions,REQ_CODE);
                }*/
                myAuth.createUserWithEmailAndPassword(enterUsername,enterPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(CreateNewUser.this, "User has been created", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(CreateNewUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });


    }
    public void backtoMainLogin()                                          //Heading back to the login page
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}