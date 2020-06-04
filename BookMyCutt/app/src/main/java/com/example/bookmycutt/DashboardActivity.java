package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth FirebaseAuth;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        FirebaseAuth=FirebaseAuth.getInstance();
        Toolbar toolbar =findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);




        //bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //handle item clicks
            switch (menuItem.getItemId()){
                case R.id.home:
                    //home fragment transaction
                    HomeFragment fragment1 = new HomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.content,fragment1,"");
                    ft1.commit();
                    return true;
                case R.id.users:
                    //Users fragment transaction
                    //home fragment transaction
                    UsersFragment fragment2 = new UsersFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.content,fragment2,"");
                    ft2.commit();
                    return true;
                case R.id.profile:
                    //profile fragment transaction
                    //home fragment transaction
                    ProfileFragment fragment3 = new ProfileFragment();
                    FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                    ft3.replace(R.id.content,fragment3,"");
                    ft3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }

    private void checkuserstatus() {
        FirebaseUser user=FirebaseAuth.getCurrentUser();
        if (user!=null){
            //display.setText(user.getEmail());
            String uid=FirebaseAuth.getCurrentUser().getUid();

            //  reference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("name");

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue()==null) {
                        // The child doesn't exist
                        Toast.makeText(DashboardActivity.this, "Please Complete Profile", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DashboardActivity.this,ProfileActivity.class));
                    }
                    else{

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            FirebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }
}

