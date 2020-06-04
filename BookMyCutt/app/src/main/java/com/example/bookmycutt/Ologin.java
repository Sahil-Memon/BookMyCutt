package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Ologin extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    EditText emailET,passwordET;
    Button loginbtn;
    TextView not_acc,recover;

    private FirebaseAuth mAuth;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ologin);

        emailET = findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        loginbtn=findViewById(R.id.loginBtn);
        not_acc=findViewById(R.id.not_acc);
        recover=findViewById(R.id.recover);
        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String email= emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //invalid email pattern set error
                    emailET.setError("Invalid Email");
                    emailET.setFocusable(true);
                }
                else{
                    //valid email pattern
                    loginUser(email,password);

                }
            }
        });
        //not have an account
        not_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ologin.this,Oregister.class));
                finish();
            }
        });
        //recover password
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverdialog();
            }
        });

        progress = new ProgressDialog(this);
    }

    private void recoverdialog() {
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //set liner layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in dialog
        final EditText emailET = new EditText(this);
        emailET.setHint("Email");
        emailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //seting minimum size width of editext
        emailET.setMinEms(16);

        linearLayout.addView(emailET);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailET.getText().toString().trim();
                beginrecovery(email);
            }
        });
        //buttons cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });
        //show Dialog
        builder.create().show();
    }

    private void beginrecovery(String email) {
        //show progress dialog
        progress.setMessage("Sending Email ...");
        progress.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(Ologin.this, "Email sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Ologin.this, "Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                //show proper error message
                Toast.makeText(Ologin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        //show progress dialog
        progress.setMessage("Logging in ...");
        progress.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //dismiss the progress
                            progress.dismiss();
                            // Sign in success, ProfileActivity UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //user is logged in so start activity
                            startActivity(new Intent(Ologin.this, Odashboard.class));
                            finish();
                        } else {
                            //dismiss the progress
                            progress.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Ologin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //dismiss the progress
                progress.dismiss();
                //error,get and show error message
                Toast.makeText(Ologin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //on previous activity
        return super.onSupportNavigateUp();
    }

}
