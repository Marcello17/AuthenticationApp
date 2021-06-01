package com.javasampleapproach.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity {

    EditText username,password;
    Button btnCreateNew;
    Button btnSignIn;

    //Biometric variables
    private TextView authStatusTv;
    private Button authBtn;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    String enterUsername, enterPassword;


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


        //---------------------------------Bio metric section-----------------------------------------------------------------------------//
        authStatusTv = findViewById(R.id.authStatus);
        authBtn = findViewById(R.id.bioBtn);

        //init bio metric
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                //Error authentication, stop tasks which would require auth
                authStatusTv.setText("Authentication error" + errString);
                Toast.makeText(MainActivity.this, "Authentication error:"+errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Successful authentication
                authStatusTv.setText("Authentication succeeded...!");
                Toast.makeText(MainActivity.this, "Authentication succeeded...!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //Failed authentication
                authStatusTv.setText("Authentication failed..!");
                Toast.makeText(MainActivity.this, "Authentication failed..!", Toast.LENGTH_SHORT).show();
            }
        });

        //Set up title, desciption on the auth dialog pop up
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using finger print authentication")
                .setNegativeButtonText("User App Password")
                .build();

        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            biometricPrompt.authenticate(promptInfo);
            }
        });
//------------------------------------------------End of biometric code----------------------------------------------------------------------//
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

/*Sources used:
https://stackoverflow.com/questions/35335892/android-m-fingerprint-scanner-on-android-emulator/45181265

https://www.youtube.com/watch?v=yPcxZWSszh8
 */