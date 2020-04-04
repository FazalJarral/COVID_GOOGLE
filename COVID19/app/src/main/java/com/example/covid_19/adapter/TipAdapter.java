package com.example.covid_19.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.R;
import com.example.covid_19.bean.Tip;

import java.util.List;
import java.util.Random;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.GroupViewHolder> {

    List<Tip> tipList;

    public TipAdapter(List<Tip> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Tip tip = tipList.get(position);


        holder.tip.setText(tip.getTitle());
        holder.desc.setText(tip.getDesc());
        if (tip.isExpanded()) {
            holder.expandable.setVisibility(View.VISIBLE);
        }
        else
            holder.expandable.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tip;
        TextView desc;
        LinearLayout expandable;
        View view;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tip = itemView.findViewById(R.id.parent_tip);
            desc = itemView.findViewById(R.id.description);
            expandable = itemView.findViewById(R.id.expandable);
            view = itemView.findViewById(R.id.view);
            Random r = new Random();
            int red=r.nextInt(255 - 0 + 1)+0;
            int green=r.nextInt(255 - 0 + 1)+0;
            int blue=r.nextInt(255 - 0 + 1)+0;

            GradientDrawable draw = new GradientDrawable();
            draw.setShape(GradientDrawable.RECTANGLE);
            draw.setColor(Color.rgb(red,green,blue));
            view.setBackground(draw);
            tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tip tip = tipList.get(getAdapterPosition());
                    tip.setExpanded(!tip.isExpanded());
                    notifyDataSetChanged();
                }
            });
        }

    }

}
