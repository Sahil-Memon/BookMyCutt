package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Oregister extends AppCompatActivity {
    EditText emailET,passwordET;
    Button registerBtn;
    TextView have_acc;

    //progress to display while registering
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oregister);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        registerBtn = findViewById(R.id.registerBtn);
        have_acc =findViewById(R.id.have_acc);


        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating User...");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input email,password
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error and focus to email editext
                    emailET.setError("Invalid Email");
                    emailET.setFocusable(true);
                }
                else if(password.length()<6){
                    passwordET.setError("Password is too weak,password length should be atleast 6 character");
                    passwordET.setFocusable(true);
                }
                else {

                    registerUser(email,password);
                }
            }
        });

        //handle login textview click listner
        have_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Oregister.this,Ologin.class));
                finish();
            }
        });
    }

    private void registerUser(String email, String password) {

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start register activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // get user email and uid from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            // when user is registered store user info in firebase realtime database too
                            //using hashmap
                            HashMap<Object,String> hashMap = new HashMap<>();
                            //put info into hashmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("owner name",""); //will add later (eg edit profile)
                            hashMap.put("shop name","");
                            hashMap.put("sex","");
                            hashMap.put("shop adddress","");
                            hashMap.put("phone number","");
                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            // path to store user data named "Users"
                            DatabaseReference reference = database.getReference("Owners");
                            //put data within hashmap in database
                            reference.child(uid).setValue(hashMap);

                            //for shop info
                            HashMap<Object,String> hashMap1 = new HashMap<>();
                            //put info into hashmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("Number of employees",""); //will add later (eg edit profile)
                            hashMap.put("Working Days","");
                            hashMap.put("Timings of shop","");
                            //firebase database instance
                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                            // path to store user data named "Users"
                            DatabaseReference reference1 = database1.getReference("Details");
                            //put data within hashmap in database
                            reference1.child(uid).setValue(hashMap1);


                            Toast.makeText(Oregister.this, "Registered ...\n"+user, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Oregister.this,Oprofile.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Oregister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error dismiss dialog and show error
                progressDialog.dismiss();
                Toast.makeText(Oregister.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //on previous activity
        return super.onSupportNavigateUp();
    }


}
