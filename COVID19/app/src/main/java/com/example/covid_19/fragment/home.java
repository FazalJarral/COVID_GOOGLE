package com.example.covid_19.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.DataService;
import com.example.covid_19.MainActivity;
import com.example.covid_19.R;
import com.example.covid_19.adapter.dataAdapter;
import com.example.covid_19.bean.PatientData;
import com.example.covid_19.bean.ProvincialStat;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home extends Fragment implements View.OnClickListener {
    PieChart pieChart;
    MaterialButton buttonTest;
    ProgressBar progressBar;

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://coronavirus-19-api.herokuapp.com/countries/";
    RecyclerView recyclerView;
    dataAdapter adapter;
    ArrayList<PatientData> data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_home , container ,false);
        progressBar = v.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        initChart(v);
        initField(v);
        buttonTest = v.findViewById(R.id.test_call);
        data = new ArrayList<>();
        buttonTest.setOnClickListener(this);
        getDataFromServer();
        return v;

    }

    private void getDataFromServer() {

        DataService service = getRetrofitInstance().create(DataService.class);
        Call<PatientData> call = service.getAllPatients();
        call.enqueue(new Callback<PatientData>() {

            @Override
            public void onResponse(Call<PatientData> call, Response<PatientData> response) {
                if (response.isSuccessful()){
                    PatientData patientData = response.body();
                    data.add(patientData);
                    progressBar.setVisibility(View.GONE);
                    adapter = new dataAdapter(data);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PatientData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                Log.e("Failure" , t.getLocalizedMessage());
                Toast.makeText(getContext(), "Our Servers Are Having Some Trouble , Refresh Again", Toast.LENGTH_SHORT).show();
            }
        });

    }
//    private void generateDataList(List<PatientData> body) {
//
//        data.addAll(body);
//
//    }

    public static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    private void initField(View v) {
        recyclerView = v.findViewById(R.id.recyclerViewPatient);




    }

    private void initChart(View v) {
    pieChart = v.findViewById(R.id.pie_chart);

    ProvincialStat stat = new ProvincialStat();
    stat.setAJK(11);
    stat.setBalochistan(175);
    stat.setGilgit(190);
    stat.setKPK(343);
    stat.setPunjab(977);
    stat.setSindh(783);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Case");
               reference.child("id").setValue(stat);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProvincialStat stat = snapshot.getValue(ProvincialStat.class);
                Log.e("stat" , stat.toString());
                setChartData(stat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void setChartData(ProvincialStat stat) {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(stat.getPunjab() , "Punjab"));
        pieEntries.add(new PieEntry(stat.getSindh() , "Sindh"));
        pieEntries.add(new PieEntry(stat.getGilgit() , "GB"));
        pieEntries.add(new PieEntry(stat.getKPK() , "KPK"));
        pieEntries.add(new PieEntry(stat.getBalochistan() , "Balochistan"));
        pieEntries.add(new PieEntry(stat.getAJK() , "AJK"));
        pieChart.animateXY(5000 , 5000);
        PieDataSet dataSet = new PieDataSet(pieEntries , "Death Toll ");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        Description description = new Description();
        description.setText("A Pie Chart To Represent Death Toll");
        pieChart.setData(data);
        pieChart.setDescription(description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_call:
               ((MainActivity)getActivity()).updateFragment(new TestFrag());


        }
    }
}
