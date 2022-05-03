package com.example.vaccisafe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VaccineRecyclerAdapter extends RecyclerView.Adapter<VaccineRecyclerAdapter.MyViewHolder> {

    private ArrayList<VaccineModel> vaccines;

    public VaccineRecyclerAdapter(ArrayList<VaccineModel> vaccines) {
        this.vaccines = vaccines;
    }

    @NonNull
    @Override
    public VaccineRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineRecyclerAdapter.MyViewHolder holder, int position) {
        String vaccine_name = vaccines.get(position).getName();
        String taken_at = vaccines.get(position).getAge();
        Boolean mark_as = vaccines.get(position).getTaken();
        String dueOn = vaccines.get(position).getDueOn();

        holder.vaccine_name_txt.setText(vaccine_name);
        holder.takenAt.setText(taken_at);

        if (mark_as) {
            holder.mark_as_btn.setText("Taken");
            //holder.dueOn.setText("-->");
            holder.mark_as_btn.setBackgroundColor(Color.parseColor("#4caf50"));
        } else {
            holder.mark_as_btn.setText(dueOn);
            holder.mark_as_btn.setBackgroundColor(Color.parseColor("#f97444"));
        }
    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView vaccine_name_txt;
        private TextView takenAt;
        private Button mark_as_btn;

        public MyViewHolder(final View view) {
            super(view);
            vaccine_name_txt = view.findViewById(R.id.vaccine_name);
            takenAt = view.findViewById(R.id.taken_at);
            mark_as_btn = view.findViewById(R.id.mark_as_btn);

            vaccine_name_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_details_bottomsheet(getAbsoluteAdapterPosition());
                }
            });

            takenAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_details_bottomsheet(getAbsoluteAdapterPosition());
                }
            });

            mark_as_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAbsoluteAdapterPosition();
                    VaccineModel vaccine_obj = vaccines.get(index);

                    if (mark_as_btn.getText().equals("Taken")) {
                        mark_as_btn.setText(vaccine_obj.getDueOn());
                        vaccine_obj.setTaken_at(null);
                        //vaccines.get(getAbsoluteAdapterPosition()).setTaken_at(null);
                        mark_as_btn.setBackgroundColor(Color.parseColor("#f97444"));
                        //dueOn.setText(vaccines.get(getAbsoluteAdapterPosition()).getDueOn());
                    } else {
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        String todaysdate = dateFormat.format(date);
                        vaccine_obj.setTaken_at(todaysdate);

                        mark_as_btn.setText("Taken");
                        mark_as_btn.setBackgroundColor(Color.parseColor("#4caf50"));
                        //dueOn.setText("-->");
                    }
                }
            });

        }
    }

    public void show_details_bottomsheet(int index){

        VaccineModel vaccine_obj = vaccines.get(index);
        Bundle bundle = new Bundle();
        bundle.putString("vaccine_name", vaccine_obj.getDisplayName());
        bundle.putString("vaccine_details", vaccine_obj.getDetails());
        VaccineDetailsBottomSheetDialog bottomSheet = new VaccineDetailsBottomSheetDialog();
        AppCompatActivity my_context = (AppCompatActivity) vaccine_obj.getContext();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(my_context.getSupportFragmentManager(), "exampleBottomSheet");
    }


}
