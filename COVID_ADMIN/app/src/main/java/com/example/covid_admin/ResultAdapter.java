package com.example.covid_admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_admin.bean.SymptomTest;
import com.example.covid_admin.bean.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    ArrayList<SymptomTest> data;
    OnCallClickListener mCallClick;
    Context context;
    String phone;



    public ResultAdapter(ArrayList<SymptomTest> data , OnCallClickListener mCallClick) {
        this.data = data;
        this.mCallClick = mCallClick;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_patient, parent, false);
        ViewHolder viewHolder = new ViewHolder(v , mCallClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final SymptomTest result = data.get(position);
       // final User mUser = userData.get(position);
        holder.runnynose.setText(String.valueOf(result.isRunnyNose()));
        if (result.getTemperature() != null)
            holder.fever.setText(String.valueOf(result.isFever() + " " + result.getTemperature()));
        else holder.fever.setText(String.valueOf(result.isFever()));
        holder.cough.setText(String.valueOf(result.isCough()));
        holder.cardiac.setText(String.valueOf(result.isCardiac_pressure()));
        holder.tiredness.setText(String.valueOf(result.isTiredness()));
      holder.name.setText(result.getUser().getName());
       holder.location.setText(result.getUser().getAddress());
        phone = result.getUser().getPhoneNum();

    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fever;
        TextView name;
        TextView cardiac;
        TextView cough;
        TextView tiredness;
        TextView runnynose;
        TextView location;
        Button callPatient;
        OnCallClickListener callClickListener;
        public ViewHolder(@NonNull View itemView , OnCallClickListener callClickListener)  {
            super(itemView);
            this.callClickListener = callClickListener;
            fever = itemView.findViewById(R.id.fever);
            name = itemView.findViewById(R.id.patient_name);
            cardiac = itemView.findViewById(R.id.cardiacpressure);
            cough = itemView.findViewById(R.id.drycough);
            tiredness = itemView.findViewById(R.id.tiredness);
            runnynose = itemView.findViewById(R.id.runnyNose);
            location = itemView.findViewById(R.id.location);
            callPatient = itemView.findViewById(R.id.btn_call);
             callPatient.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            callClickListener.onCallButtonClick(phone);
        }
    }
    public interface OnCallClickListener{
        void onCallButtonClick(String phone);
    }

}
