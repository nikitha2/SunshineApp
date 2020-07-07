package com.nikitha.android.sunshineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ArrayAdapterSunshine extends ArrayAdapter<ListItems> {
    ListItems currentword;
    ArrayList weatherData=new ArrayList<ListItems>();

    public ArrayAdapterSunshine(@NonNull Context context, @NonNull ArrayList<ListItems> objects) {
        super(context, 0, objects);
        weatherData=objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        final String[] parts = (parent.toString()).split("app:id/",2);
        currentword = getItem(position);

        TextView temp=(TextView) listItemView.findViewById(R.id.Maxtemp);
        TextView weather=(TextView) listItemView.findViewById(R.id.description);

        temp.setText(currentword.temp);
        weather.setText(currentword.description);

        return listItemView;
    }

    public void setData(ArrayList<ListItems> data) {
        weatherData.addAll(data);
        notifyDataSetChanged();
    }
}
