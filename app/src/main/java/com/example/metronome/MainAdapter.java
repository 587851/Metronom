package com.example.metronome;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    String[] metronomeStrings;
    ItemClickListener itemClickListener;
    int selectedPosition;

    public MainAdapter(String [] metronomeStrings, ItemClickListener itemClickListener, int selectedPosition){
        this.metronomeStrings = metronomeStrings;
        this.itemClickListener = itemClickListener;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        //set text on radiobuttons
        holder.radioButton.setText(metronomeStrings[position]);
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    selectedPosition = holder.getAdapterPosition();
                    itemClickListener.onClick(holder.radioButton.getText().toString(), selectedPosition);
                }

            }
        });
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public int getItemCount() {
        return metronomeStrings.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button_metronome_sounds);
        }
    }
}
