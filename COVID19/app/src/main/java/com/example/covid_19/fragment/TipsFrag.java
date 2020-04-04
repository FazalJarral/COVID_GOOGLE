package com.example.covid_19.fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.AlarmReceiver;
import com.example.covid_19.R;
import com.example.covid_19.adapter.TipAdapter;
import com.example.covid_19.bean.Tip;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class TipsFrag extends Fragment {
    RecyclerView recyclerView;
    TipAdapter adapter;
    ToggleButton alarmToggle;
    long def_interval = 15 * 60 * 1000;
    long selected_interval;
    long repeatInterval;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    NumberPicker numberPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tip, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        alarmToggle = v.findViewById(R.id.togle);

        mNotificationManager = (NotificationManager) getActivity().
                getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        Intent notifyIntent = new Intent(getContext(), AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        def_interval = 15 * 60 * 1000;

        final long triggerTime = SystemClock.elapsedRealtime()
                + repeatInterval;
        boolean alarmUp = (PendingIntent.getBroadcast(getContext(), NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);

//If the Toggle is turned on, set the repeating alarm with a 15 minute interval

        alarmToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (alarmManager != null) {
                                alarmManager.setInexactRepeating
                                        (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                                triggerTime, AlarmManager.INTERVAL_HALF_HOUR, notifyPendingIntent);
                            }
                        } else {
                            //Cancel notification if the alarm is turned off
                            mNotificationManager.cancelAll();
                            if (alarmManager != null) {
                                alarmManager.cancel(notifyPendingIntent);
                            }

                        }
                    }
                });




        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Tip> tips = new ArrayList<>();

        Tip tip = new Tip("Wash hands frequently ", "Regularly and thoroughly clean your hands with an alcohol-based hand rub or wash them with soap and water. Why? Washing your hands with soap and water or using alcohol-based hand rub kills viruses that may be on your hands.");
        Tip tip2 = new Tip("Maintain social distance ", "Maintain at least 1 metre (3 feet) distance between yourself and anyone who is coughing or sneezing. Why? When someone coughs or sneezes they spray small liquid droplets from their nose or mouth which may contain virus. If you are too close, you can breathe in the droplets, including the COVID-19 virus if the person coughing has the disease.");
        Tip tip3 = new Tip("Avoid touching face", "Why? Hands touch many surfaces and can pick up viruses. Once contaminated, hands can transfer the virus to your eyes, nose or mouth. From there, the virus can enter your body and can make you sick.");
        Tip tip4 = new Tip("Practice respiratory hygiene ", "Make sure you, and the people around you, follow good respiratory hygiene. This means covering your mouth and nose with your bent elbow or tissue when you cough or sneeze. Then dispose of the used tissue immediately.");

        tips.add(tip);
        tips.add(tip2);
        tips.add(tip3);
        tips.add(tip4);
        adapter = new TipAdapter(tips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Wash Your Hands",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Regularly wash your hands");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
