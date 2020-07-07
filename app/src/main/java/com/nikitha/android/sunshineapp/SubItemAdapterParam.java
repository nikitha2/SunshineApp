package com.nikitha.android.sunshineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nikitha.android.sunshineapp.data.gridlistItem;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SubItemAdapterParam  extends ArrayAdapter<gridlistItem> {
    List<gridlistItem> listItem=new ArrayList<>();


    public SubItemAdapterParam(@NonNull Context context, int resource, @NonNull List<gridlistItem> objects) {
        super(context, 0, objects);
        listItem= objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        gridlistItem user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.details_layout3_listitem, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.Pressure);
        TextView value = (TextView) convertView.findViewById(R.id.pressumeVal);
        // Populate the data into the template view using the data object
        title.setText(user.getTitle());
        value.setText(user.getValue());
        // Return the completed view to render on screen
        return convertView;
    }


}
