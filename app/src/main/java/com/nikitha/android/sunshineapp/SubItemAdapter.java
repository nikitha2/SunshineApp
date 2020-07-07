package com.nikitha.android.sunshineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubItemAdapter extends RecyclerView.Adapter {
    ArrayList<TaskEntry> listItems;

    public SubItemAdapter(ArrayList<TaskEntry> listItems) {
        this.listItems=listItems;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder  {
        View v;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.v=itemView;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        CustomViewHolder customViewHolder = null;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_layout2_listitem1, parent, false);
        customViewHolder=new CustomViewHolder(v);

        return  customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View currentView = holder.itemView;
        TaskEntry currentItemAtPos = listItems.get(position);

        TextView time=(TextView) currentView.findViewById(R.id.time);
        time.setText(currentItemAtPos.getTime());

        ImageView image1=(ImageView) currentView.findViewById(R.id.image1);
        image1.setImageResource(SunshineWeatherUtils.getIconResourceForWeatherCondition(Integer.valueOf(currentItemAtPos.getWeatherid())));

        TextView temp2=(TextView) currentView.findViewById(R.id.temp2);
        temp2.setText(SunshineWeatherUtils.formatTemperature(currentView.getContext(), Double.valueOf(currentItemAtPos.getTemp())));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
