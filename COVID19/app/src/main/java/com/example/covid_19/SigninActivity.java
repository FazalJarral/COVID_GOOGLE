package com.example.covid_19;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19.bean.User;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private static final String TAG = "SignIn";
    EditText phone_num;
    EditText name;
    MaterialButton sendOtp;
    String mVerificationId;
    private FirebaseAuth mAuth;
    Location myLocation;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        phone_num = findViewById(R.id.phone_num);
        name = findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        sendOtp = findViewById(R.id.btn_send_otp);
        sendOtp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("user", user.getDisplayName() + " " + user.getUid());
            updateUI(user);
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(name.getText())) {
            name.setFocusable(true);
            name.setError("Enter Your Name");
            return;
        } else if (TextUtils.isEmpty(phone_num.getText())) {
            phone_num.setFocusable(true);
            phone_num.setError("Enter Your Phone Number");
            return;
        } else {
            String phoneNumber = phone_num.getText().toString();
            if (phoneNumber.startsWith("0"))
                phoneNumber = phoneNumber.substring(1);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+92" + phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Log.e("Erro ", e.getLocalizedMessage());
                            Toast.makeText(SigninActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            mVerificationId = s;
                            mResendToken = forceResendingToken;
                        }
                    });        // OnVerificationStateChangedCallbacks

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User myuser = new User();
                            myuser.setName(name.getText().toString());
                            myuser.setPhoneNum(phone_num.getText().toString());
                            addToRealtimeDb(myuser, firebaseUser);

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void addToRealtimeDb(User myuser, FirebaseUser firebaseUser) {
        String key = firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        myuser.setId(key);
        myRef.child(key).setValue(myuser);
        Log.e("User", myuser.getName() + " Is added");
        updateUI(firebaseUser);

    }


    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
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
