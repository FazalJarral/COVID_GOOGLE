package com.example.covid_19.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.R;
import com.example.covid_19.bean.PatientData;

import java.util.ArrayList;


public class dataAdapter extends RecyclerView.Adapter<dataAdapter.ViewHolder> {
    ArrayList<PatientData> data;

    public dataAdapter(ArrayList<PatientData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_patient, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PatientData patientData = data.get(position);
        holder.todays_cases.setText(patientData.getTodayCases());
        holder.deathtoll.setText(patientData.getDeaths());
        holder.recovered.setText(patientData.getRecovered());
        holder.total_case.setText(patientData.getCases());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView total_case;
        TextView recovered;
        TextView todays_cases;
        TextView deathtoll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            total_case = itemView.findViewById(R.id.total_cases);
            recovered = itemView.findViewById(R.id.recoverd);
            todays_cases = itemView.findViewById(R.id.today_cases);
            deathtoll = itemView.findViewById(R.id.death_toll);

        }
    }
}
