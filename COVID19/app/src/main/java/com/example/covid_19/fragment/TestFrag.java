package com.example.covid_19.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19.R;
import com.example.covid_19.bean.SymptomTest;
import com.example.covid_19.bean.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import me.toptas.kooldown.Kooldown;

import static android.view.View.GONE;

public class TestFrag extends Fragment implements View.OnClickListener, LocationListener {

    Location myLocation;
    Spinner spinner;
    String testCity;
    String FCM_API = "https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;


    RadioButton positiveCough;
    RadioButton negativeCough;
    RadioButton positiveFever;
    RadioButton negativeFever;
    RadioButton positiveNose;
    RadioButton negativeNose;
    RadioButton positiveTired;
    RadioButton negativeTired;
    RadioButton positiveCardiac;
    RadioButton negativeCardiac;

    RadioGroup coughGroup;
    RadioGroup feverGroup;
    RadioGroup noseGroup;
    RadioGroup tiredGroup;
    RadioGroup cardiacGroup;

    EditText fever;
    MaterialButton submit;
    MaterialButton support;
    TextView message;
    TextView prediction;
    int score, temp;
    ProgressBar progressBar;

    ImageView imageView;
    LinearLayout fullView;
    Kooldown chartView;
    protected LocationManager locationManager;
    User myUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_test, container, false);
        init(v);
        submit.setOnClickListener(this);
        support.setOnClickListener(this);
        initLocation();
        loadUserForFrag();
        setupSpinner();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Please Assign Permission", Toast.LENGTH_LONG).show();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                testCity = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), testCity, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;

    }

    private void setupSpinner() {
        Vector<String> str = new Vector<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("city.txt")));
            String line = in.readLine();
            while (line != null) {

                str.add(line);
                line = in.readLine();
            }
            Collections.sort(str);
            ArrayAdapter adapter = new ArrayAdapter(getContext(),
                    android.R.layout.simple_spinner_item, str);

            spinner.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void loadUserForFrag() {
        String firebaseUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = (DatabaseReference) FirebaseDatabase.getInstance().getReference("User");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().contentEquals(firebaseUserId)) {
                        Log.e("User Found", user.getName());
                        myUser = user;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void initLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.e("Last location" , locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) + "");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }
    }

    private String getCityName(Location myLocation) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getAddressLine(0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;

    }


    private void init(View v) {
        requestQueue = Volley.newRequestQueue(getContext());

        imageView = v.findViewById(R.id.image);
        spinner = v.findViewById(R.id.spinner);
        positiveCardiac = v.findViewById(R.id.positiveCardiac);
        positiveCough = v.findViewById(R.id.positiveCough);
        positiveFever = v.findViewById(R.id.positiveFever);
        positiveNose = v.findViewById(R.id.positiveNose);
        positiveTired = v.findViewById(R.id.positivetired);
        negativeCardiac = v.findViewById(R.id.negativeCardia);
        negativeCough = v.findViewById(R.id.negativeCough);
        negativeFever = v.findViewById(R.id.negativeFever);
        negativeNose = v.findViewById(R.id.negativeNose);
        negativeTired = v.findViewById(R.id.negativetired);
        fever = v.findViewById(R.id.fever);
        prediction = v.findViewById(R.id.prediction);
        message = v.findViewById(R.id.text_after);
        submit = v.findViewById(R.id.submit);
        support = v.findViewById(R.id.call_supprt);
        progressBar = v.findViewById(R.id.progress_circular);
        fullView = v.findViewById(R.id.full_layout);
        chartView = v.findViewById(R.id.kd);
        coughGroup = v.findViewById(R.id.radioGroupCough);
        cardiacGroup = v.findViewById(R.id.radioGroupCardiac);
        tiredGroup = v.findViewById(R.id.radioGroupTired);
        noseGroup = v.findViewById(R.id.radioGroupNose);
        feverGroup = v.findViewById(R.id.radioGroupFever);
        feverGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == negativeFever.getId()) {
                    fever.setVisibility(GONE);
                } else {
                    fever.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                score = 0;


                if (myLocation == null) {
                    Toast.makeText(getContext(), "Waiting For Server", Toast.LENGTH_SHORT).show();
                    myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    fullView.setVisibility(GONE);

                    //     Log.e("City", getCityName(myLocation));
                    myUser.setAddress(getCityName(myLocation));


                    FirebaseDatabase.getInstance().getReference().child("User")
                            .child(myUser.getId()).setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("Update Child", "onComplete: " + "succesfuly updated");
                            } else {
                                Log.e("Update Child", "onComplete: " + "failed" + task.getException().getMessage());

                            }
                        }
                    });


                    sendToDB();

                    progressBar.setVisibility(GONE);

                    message.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    message.setText("Be Safe. Your Test Has been submitted to authorities , they will contact you through your phone num. Please Do not resubmit your test again.Our Team is working closely with authorities.");
                    Toast.makeText(getContext(), "Your Response is being sent to authoraties", Toast.LENGTH_SHORT).show();
                    prepareNotification(testCity);
                }
                break;

            case R.id.call_supprt:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1166"));
                startActivity(intent);


        }

    }
    private void prepareNotification(String topic) {
        JSONObject notfication = new JSONObject();
        JSONObject notficationBody = new JSONObject();
        try {
            notficationBody.put("title", "A NEW PATIENT HAS SUBMITTED TEST");
            notficationBody.put("body","YOU HAVE GOT A NEW TEST , PLEASE CONNECT WITH USER");
            notfication.put("to" , "/topics/"+topic);
            notfication.put("data", notficationBody);
            notfication.put("notification" , notficationBody);

        }
        catch (Exception e){
            Log.e("Exception Notification", e.getMessage());
        }
        sendNotification(notfication);
    }

    private void sendNotification(JSONObject notfication) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST , FCM_API, notfication,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response Success" , response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response Error" , error.toString());

                    }

                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String contenttype = "application/json";
                HashMap<String , String> param = new HashMap();
                param.put("Authorization", "key=" + getString(R.string.server_key));
                param.put("Content-Type" , contenttype);
                return param;
            }
        };
        Log.e("Request" , request.getBody().toString());
        requestQueue.add(request);
    }

    private void sendToDB() {

        SymptomTest symptomTest = new SymptomTest();
        if (positiveCardiac.isChecked()) {
            symptomTest.setCardiac_pressure(true);
        }
        if (positiveNose.isChecked()) {
            symptomTest.setRunnyNose(true);
        }
        if (positiveCough.isChecked()) {
            symptomTest.setCough(true);
        }
        if (positiveFever.isChecked()) {
            symptomTest.setisFever(true);
        }
        if (positiveTired.isChecked()) {
            symptomTest.setTiredness(true);
        }

        symptomTest.setUser(myUser);
        if (!(TextUtils.isEmpty(fever.getText()))) {
            symptomTest.setTemperature(fever.getText().toString());
        }
        symptomTest.setCity(testCity);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Result");
        symptomTest.setId(databaseReference.push().getKey());
        databaseReference.child(testCity).child(symptomTest.getId()).setValue(symptomTest);


    }


    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        Log.e("Location", location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}