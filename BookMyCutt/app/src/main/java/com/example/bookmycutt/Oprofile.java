package com.example.bookmycutt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Oprofile extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN=1;
    private EditText fullNameUser,contact,shopname,sex,address;
    private FirebaseFirestore db;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oprofile);

        mFirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mFirebaseAuth.getCurrentUser();
        fullNameUser=findViewById(R.id.name);
        shopname=findViewById(R.id.shopname);
        contact=findViewById(R.id.number);
        sex=findViewById(R.id.sex);
        address=findViewById(R.id.address);
        submit=findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FirebaseUser  user1=mFirebaseAuth.getCurrentUser();
                if(fullNameUser.length()==0){
                    fullNameUser.setError("Enter Name");
                }
                else if(shopname.length()==0){
                    shopname.setError("Enter Shop Name");
                }
                else if(contact.length()==0){
                    contact.setError("Enter Contactno");
                }
                else if(contact.length()>10 || contact.length()<10){
                    contact.setError("Enter correct phone no");
                }
                else if(sex.length()==0){
                    sex.setError("Enter the detail");
                }
                else if(address.length()==0){
                    address.setError("Enter the Adddress");
                }
                else {


                    final String fullName = fullNameUser.getText().toString().trim();
                    final String Shopname = shopname.getText().toString().trim();
                    final String Sex = sex.getText().toString().trim();
                    final String Address = address.getText().toString().trim();


                    //String value= et.getText().toString();

                    final String contact2 = contact.getText().toString();
                    String uid = mFirebaseAuth.getUid();
                    String email = user.getEmail();


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("owner name", fullName);
                    hashMap.put("shop name", Shopname);
                    hashMap.put("sex",Sex );
                    hashMap.put("shop address",Address );
                    hashMap.put("phone number", contact2);
                    hashMap.put("image", String.valueOf(user1.getPhotoUrl()));


                    //firebase database instance
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    // path to store user data named "Users"
                    DatabaseReference reference = database.getReference("Owners");
                    //put data within hashmap in database
                    reference.child(uid).setValue(hashMap);


                    // show user email
                    Toast.makeText(Oprofile.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                    // go to profile activity
                    startActivity(new Intent(Oprofile.this, Overification.class));
                    finish();
                }}
        });
    }
}
