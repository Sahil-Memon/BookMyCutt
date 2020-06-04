package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private TextView mDisplayDate,age;
    private DatePickerDialog.OnDateSetListener setListeners;
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN=1;
    private EditText fullNameUser,contact;
    private Button submit;
    private RadioGroup gender;
    private FirebaseFirestore db;
    String Gender;
    String date;
    int year2;
    private DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mFirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mFirebaseAuth.getCurrentUser();
        fullNameUser=findViewById(R.id.name);
        gender=findViewById(R.id.radioGroup);
        contact=findViewById(R.id.number);
        submit=findViewById(R.id.button);
        age=findViewById(R.id.age);
        // onBackPressed();
        gender.setOnCheckedChangeListener(this);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        age.setOnClickListener(new View.OnClickListener() {
            private DatePickerDialog.OnDateSetListener setListeners;

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        ProfileActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                year2=year;
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                date=day+"/"+month+"/"+year;
                year2=year;

                age.setText(date);
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FirebaseUser  user1=mFirebaseAuth.getCurrentUser();
                if(fullNameUser.length()==0){
                    fullNameUser.setError("Enter Name");
                }
                else if(age.length()==0){
                    age.setError("Enter Date OF Birth");
                }
                else if(contact.length()==0){
                    contact.setError("Enter Contactno");
                }
                else if(contact.length()>10 || contact.length()<10){
                    contact.setError("Enter correct phone no");
                }
                else if(year2>=2019){
                    age.setError("Please Select proper date of birth");
                    Toast.makeText(ProfileActivity.this, "Please Select proper date of birth", Toast.LENGTH_SHORT).show();


               }
                else {


                    final String fullName = fullNameUser.getText().toString().trim();

                    //String value= et.getText().toString();

                    final String contact2 = contact.getText().toString();
                    String uid = mFirebaseAuth.getUid();
                    String email = user.getEmail();


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", fullName);
                    hashMap.put("phone", contact2);
                    hashMap.put("DateOfBirth", date);
                    hashMap.put("gender",Gender );
                    hashMap.put("image", String.valueOf(user1.getPhotoUrl()));
                    hashMap.put("cover","");


                    //firebase database instance
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    // path to store user data named "Users"
                    DatabaseReference reference = database.getReference("Users");
                    //put data within hashmap in database
                    reference.child(uid).setValue(hashMap);


                    // show user email
                    Toast.makeText(ProfileActivity.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                    // go to profile activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }}
        });



    }



    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        switch (i) {
            case R.id.female:
                Gender="Female";
                break;
            case R.id.male:
                Gender="Male";
                break;

        }
    }
}
