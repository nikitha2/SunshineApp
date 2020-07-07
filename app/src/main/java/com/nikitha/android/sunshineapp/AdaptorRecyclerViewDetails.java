package com.nikitha.android.sunshineapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.nikitha.android.sunshineapp.data.gridlistItem;
import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.utilities.SunshineDateUtils;
import com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.LinearLayout.HORIZONTAL;
import static com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils.getStringForWeatherCondition;

public class AdaptorRecyclerViewDetails extends RecyclerView.Adapter {
    private static final int CURRENT=0;
    private static final int FUTURE_TIMES=1;
    private static final int PARAMETERS=2;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    private final ArrayList<TaskEntry> listItems;
    TaskEntry weatherAt0;
    private final Context context;

    public AdaptorRecyclerViewDetails(Context context, ArrayList<TaskEntry> listItems) {
        this.listItems=listItems;
        this.context=context;
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
        switch (viewType) {
            case CURRENT:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_layout1, parent, false);
                customViewHolder=new CustomViewHolder(v1);

                // to change % of height this layout occupies on screen.
                ViewGroup.LayoutParams layoutParams = v1.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight() * 0.5);
                v1.setLayoutParams(layoutParams);

                break;
            case FUTURE_TIMES:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_layout2, parent, false);
                customViewHolder=new CustomViewHolder(v2);

                // to change % of height this layout occupies on screen.
                ViewGroup.LayoutParams layoutParams2 = v2.getLayoutParams();
                layoutParams2.height = (int) (parent.getHeight() * 0.2);
                v2.setLayoutParams(layoutParams2);

                break;
            default:  View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_layout3, parent, false);
                customViewHolder=new CustomViewHolder(v3);
                break;
        }

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder viewHolder1 = (CustomViewHolder)holder;
        View currentView = viewHolder1.itemView;
        TaskEntry currentItemAtPos = listItems.get(position);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        switch (holder.getItemViewType()) {
            case CURRENT:
                weatherAt0=listItems.get(0);
                TextView tempmax=(TextView) currentView.findViewById(R.id.city);
                tempmax.setText(sharedPreferences.getString(context.getString(R.string.location_key), context.getString(R.string.Albany)));

                TextView desc=(TextView) currentView.findViewById(R.id.desc);
                desc.setText(getStringForWeatherCondition(context, Integer.valueOf(currentItemAtPos.getWeatherid())));

                TextView temp=(TextView) currentView.findViewById(R.id.temp1);
                String tempval = SunshineWeatherUtils.formatTemperature(context, Double.valueOf(currentItemAtPos.getTemp()));
                temp.setText(tempval);

                TextView tempmax1=(TextView) currentView.findViewById(R.id.max);
                String tempValue = SunshineWeatherUtils.formatTemperature(context, Double.valueOf(currentItemAtPos.getTemp_max()));
                tempmax1.setText(tempValue);

                TextView tempmin=(TextView) currentView.findViewById(R.id.min);
                String tempValuemin = SunshineWeatherUtils.formatTemperature(context, Double.valueOf(currentItemAtPos.getTemp_min()));
                tempmin.setText(tempValuemin);

                String dateString = currentItemAtPos.getDate_txt();
                TextView day=(TextView) currentView.findViewById(R.id.day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                Date date1 = null;
                try {
                    date1 = sdf.parse(dateString);
                    System.out.println(sdf.format(date1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long dateinMilli = date1.getTime();

                String datevalue = SunshineDateUtils.getFriendlyDateString(context,dateinMilli,true );
                day.setText(datevalue);
                break;

            case FUTURE_TIMES:

                listItems.remove(0);

                LinearLayoutManager layoutManagerRecyclerView = new LinearLayoutManager(currentView.getContext(),LinearLayoutManager.HORIZONTAL,false);
                layoutManagerRecyclerView.setInitialPrefetchItemCount(listItems.size());

                SubItemAdapter subItemAdapter = new SubItemAdapter(listItems);

                RecyclerView subRecyclerView = (RecyclerView )currentView.findViewById(R.id.weatherPerTime);
                    subRecyclerView.setLayoutManager(layoutManagerRecyclerView);
                    subRecyclerView.setAdapter(subItemAdapter);
                    subRecyclerView.setRecycledViewPool(viewPool);
                break;

            default:
                LinearLayoutManager layoutManagerRecyclerView1 = new LinearLayoutManager(currentView.getContext(),LinearLayoutManager.HORIZONTAL,false);
                List<gridlistItem> data=new ArrayList<>();
                data.add(new gridlistItem("Feels like",weatherAt0.getFeels_like()));
                data.add(new gridlistItem("Pressure",weatherAt0.getPressure()));
                data.add(new gridlistItem("Sea level",weatherAt0.getSea_level()));
                data.add(new gridlistItem("Ground level",weatherAt0.getGrnd_level()));
                data.add(new gridlistItem("Wind Speed",weatherAt0.getWindSpeed()));
                data.add(new gridlistItem("Humidity",weatherAt0.getHumidity()));
                SubItemAdapterParam subItemAdapterParam = new SubItemAdapterParam(context,0,data);
                GridView gridViewParam = (GridView)currentView.findViewById(R.id.grid);
                //subRecyclerViewParam.setLayoutManager(layoutManagerRecyclerView1);
                gridViewParam.setAdapter(subItemAdapterParam);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:return CURRENT;
            case 1:return FUTURE_TIMES;
            default:return PARAMETERS;
        }
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
}
