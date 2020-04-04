package com.example.covid_19;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.covid_19.bean.User;
import com.example.covid_19.fragment.MapFrag;
import com.example.covid_19.fragment.TipsFrag;
import com.example.covid_19.fragment.home;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity{
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //loadUserForFrag();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home:
                    updateFragment(new home());
                    return true;
                case R.id.map:
                    updateFragment(new MapFrag());
                    return true;

                case R.id.tips:
                    updateFragment(new TipsFrag());
                    return true;
            }
            return false;

            }
        });

    }

    @Override
    protected void onStart() {
      super.onStart();

        //First Time
        updateFragment(new home());
    }


    public void updateFragment(Fragment fragment){


        fragmentManager.beginTransaction()

                .replace(R.id.frame_layout ,fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }


}
