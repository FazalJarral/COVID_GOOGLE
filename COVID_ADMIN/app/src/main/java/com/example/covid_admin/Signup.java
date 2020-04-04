package com.example.covid_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    SharedPreferences sharedPref;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spinner = findViewById(R.id.spinner);
        btnNext = findViewById(R.id.btn_next);
        setupSpinner();
        sharedPref = getSharedPreferences("details", Context.MODE_PRIVATE);
        if (sharedPref.contains("City")) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("city" , sharedPref.getString("City" , null));
            startActivity(intent);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("City", parent.getItemAtPosition(position).toString());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnNext.setOnClickListener(this);
    }

    private void setupSpinner() {
        Vector<String> str = new Vector<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(getAssets().open("city.txt")));
            String line = in.readLine();
            while (line != null) {

                str.add(line);
                line = in.readLine();
            }
            Collections.sort(str);
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item, str);

            spinner.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            FirebaseMessaging.getInstance().subscribeToTopic("Abbottabad")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Signup.this, "Subscribed to " +spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
