package com.example.covid_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.covid_admin.bean.SymptomTest;
import com.example.covid_admin.bean.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ResultAdapter.OnCallClickListener {
    RecyclerView recyclerView;
    ResultAdapter adapter;
    ArrayList<SymptomTest> data;
    ArrayList<User> userData;
    String city;
    SymptomTest test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        data = new ArrayList<>();
        userData = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            city = getIntent().getExtras().getString("city");
            Log.e("City", city);
        }
        adapter = new ResultAdapter(data, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveData();
        subscribe(city);

        Log.e("data", data.size() + " " + userData.size());

    }

    private void subscribe(final String city) {
        FirebaseMessaging.getInstance().subscribeToTopic(city)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Subscribed to " +city, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void retrieveData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Result").child(city);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("Snapshot", snapshot.getKey());

                    test = snapshot.getValue(SymptomTest.class);
                    data.add(test);
                    Log.e("map", test.getCity());

                }

                if (!(recyclerView.isComputingLayout())) {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onCallButtonClick(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this ,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE} , 1);
            return;
        }
        startActivity(intent);
    }
}
