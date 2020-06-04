package com.example.pallazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button scan, submit;
    RadioButton radioButton,radioButton1;
    RadioGroup rgroup,rgroup1;
    public static TextView result, number, flat, name,text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgroup=findViewById(R.id.radioGroup);
        rgroup1=findViewById(R.id.radioGroup1);
        text=findViewById(R.id.other);
        scan = findViewById(R.id.scan);
        submit = findViewById(R.id.submit);
        number = findViewById(R.id.number);
        flat = findViewById(R.id.flatno);
        name = findViewById(R.id.name);
        int radioId=rgroup.getCheckedRadioButtonId();
        int radioId1=rgroup1.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        radioButton1=findViewById(radioId1);
//        Toast.makeText(MainActivity.this,""+radioButton.getText().toString(),Toast.LENGTH_SHORT).show();
        result = findViewById(R.id.result);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                final String strDate= formatter.format(date);
//                System.out.println(strDate);
                Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
                String text1 = mySpinner.getSelectedItem().toString();

                if(text1.equals("Other")){
                    text1=text.getText().toString().trim();
                }
                if (name.length() == 0) {
                    name.setError("Please Enter Name");
                } else if (number.length() == 0) {
                    number.setError("Please Enter Contact Number");
                } else if(number.length()>10 || number.length()<10) {
                    number.setError("Enter correct phone no");
                } else if (flat.length() == 0) {
                    flat.setError("Please Enter Flat Number");
                } else {
                    DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                    final String dateString = dateFormat.format(new Date()).toString();
                    final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Entries");
                    final String finalText = text1;
                    mref.child(flat.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String in=dataSnapshot.child("In").getValue(String.class);
                                String date=dataSnapshot.child("Date").getValue(String.class);

                                final HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("Status",radioButton1.getText().toString().trim());
                                hashMap.put("Name",name.getText().toString().trim());
                                hashMap.put("Contact",number.getText().toString().trim());
                                hashMap.put("FlatNo",flat.getText().toString().trim());
                                hashMap.put("Moving",radioButton.getText().toString().trim());
                                hashMap.put("Out Time and Date",dateString+" "+strDate);
                                hashMap.put("In Time and Date",in+" "+date);
//                                hashMap.put("Date",strDate);
                                hashMap.put("Purpose", finalText);
                                mref.child(in.replace(".","-")+"-"+dateString.replace(".","-")+" Date:"+date.replace("/","-")+"-"+strDate.replace('/','-')).setValue(hashMap);
                                mref.child(flat.getText().toString().trim()).removeValue();
                                startActivity(new Intent(MainActivity.this,ResponseActivity.class));
                            }
                            else {
                                final HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("Status",radioButton1.getText().toString().trim());
                                hashMap.put("Name",name.getText().toString().trim());
                                hashMap.put("Contact",number.getText().toString().trim());
                                hashMap.put("FlatNo",flat.getText().toString().trim());
                                hashMap.put("In Time and Date",dateString+" "+strDate);
                                hashMap.put("Date",strDate);
                                hashMap.put("Purpose", finalText);
                                hashMap.put("In",dateString);

                                hashMap.put("Moving",radioButton.getText().toString().trim());
                                mref.child(flat.getText().toString().trim()).setValue(hashMap);
                                startActivity(new Intent(MainActivity.this,ResponseActivity.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void checkButton(View v){
        int radioId=rgroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
    }

    public void checkButton1(View v){
        int radioId1=rgroup1.getCheckedRadioButtonId();
        radioButton1=findViewById(radioId1);
    }
}
