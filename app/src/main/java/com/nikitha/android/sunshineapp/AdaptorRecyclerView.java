package com.nikitha.android.sunshineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class AdaptorRecyclerView extends RecyclerView.Adapter {
    ArrayList<ListItems> listItems=new ArrayList<>();
    static ListItemClickListener mClickListener;
    public AdaptorRecyclerView(ArrayList<ListItems> listItems, ListItemClickListener mClickListener) {
        this.listItems=listItems;
        this.mClickListener=mClickListener;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        CustomViewHolder customViewHolder=new CustomViewHolder(v);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View currentView = holder.itemView;
        ListItems currentItemAtPos = listItems.get(position);

        TextView temp=(TextView) currentView.findViewById(R.id.temp);
        TextView weather=(TextView) currentView.findViewById(R.id.description);

        temp.setText(currentItemAtPos.temp);
        weather.setText(currentItemAtPos.description);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setData(ArrayList<ListItems> data) {
        listItems.addAll(data);
        notifyDataSetChanged();
    }
}

