package com.nikitha.android.sunshineapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.utilities.SunshineDateUtils;
import com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils.getStringForWeatherCondition;

class AdaptorRecyclerView extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_TODAY=0;
    private static final int VIEW_TYPE_FUTURE_DAY=1;

    ArrayList<TaskEntry> listItems=new ArrayList<>();
    static ListItemClickListener mClickListener;
    Context context;
    String unitsValue1;
    public AdaptorRecyclerView(Context context,ArrayList<TaskEntry> listItems, ListItemClickListener mClickListener) {
        this.listItems=listItems;
        this.mClickListener=mClickListener;
        this.context=context;
    }




    interface ListItemClickListener{
        void onListItemClick(int position);
    }
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View v;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.v=itemView;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            mClickListener.onListItemClick(pos);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        CustomViewHolder customViewHolder;
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forecast_today, parent, false);
                    customViewHolder=new CustomViewHolder(v1);

                // to change % of height this layout occupies on screen.
                ViewGroup.LayoutParams layoutParams = v1.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight() * 0.5);
                v1.setLayoutParams(layoutParams);

                break;
            default: v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
                     customViewHolder=new CustomViewHolder(v); break;
        }

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View currentView = holder.itemView;
        TaskEntry currentItemAtPos = listItems.get(position);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean units = sharedPreferences.getBoolean(context.getString(R.string.units_key), context.getResources().getBoolean(R.bool.pref_show_default));
        String unitsValue=celOrFar(units);

        TextView tempmax=(TextView) currentView.findViewById(R.id.Maxtemp);
        String tempValue = SunshineWeatherUtils.formatTemperature(context, Double.valueOf(currentItemAtPos.getTemp_max()));
        tempmax.setText(tempValue);

        TextView tempmin=(TextView) currentView.findViewById(R.id.Mintemp);
        String tempValuemin = SunshineWeatherUtils.formatTemperature(context, Double.valueOf(currentItemAtPos.getTemp_min()));
        tempmin.setText(tempValuemin);

        String weatherid=currentItemAtPos.getWeatherid();

        TextView weather=(TextView) currentView.findViewById(R.id.description);
        String desc = getStringForWeatherCondition(context, Integer.valueOf(weatherid));
        weather.setText(desc);

        ImageView image=(ImageView) currentView.findViewById(R.id.weatherImage) ;
        image.setImageResource(SunshineWeatherUtils.getIconResourceForWeatherCondition(Integer.valueOf(weatherid)));

        String dateString = currentItemAtPos.getDate_txt();
        TextView date=(TextView) currentView.findViewById(R.id.dayNtime);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date date1 = null;
        try {
            date1 = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateinMilli = date1.getTime();

        String datevalue = SunshineDateUtils.getFriendlyDateString(context,dateinMilli,true );
        date.setText(datevalue);

        String temp_max = currentItemAtPos.getTemp_max();
        String temp_min = currentItemAtPos.getTemp_min();
        String temp_kf = currentItemAtPos.getTemp_kf();
        String feels_like = currentItemAtPos.getFeels_like();
        String grnd_level = currentItemAtPos.getGrnd_level();
        String sea_level = currentItemAtPos.getSea_level();
        String description = currentItemAtPos.getDescription();
        String humidity = currentItemAtPos.getHumidity();
        String pressure = currentItemAtPos.getPressure();
        String windSpeed = currentItemAtPos.getWindSpeed();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:return VIEW_TYPE_TODAY;
            default:return VIEW_TYPE_FUTURE_DAY;
        }
        //return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setData(List<TaskEntry> data) {
        listItems.clear();
        listItems.addAll(data);
        notifyDataSetChanged();
    }
    public String celOrFar(boolean units){
        String unitsValue1;
        if(units){
            unitsValue1=context.getString(R.string.metric);
        }
        else{
            unitsValue1=context.getString(R.string.Imperial);
        }
        return unitsValue1;
    }
}

