package devpatel.apps.vaccisafe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.Viewholder> {
    private Context context;
    private ArrayList<RecipientModel> recArrayList;

    public RecAdapter(Context context, ArrayList<RecipientModel> recArrayList) {
        this.context = context;
        this.recArrayList = recArrayList;
    }

    @NonNull
    @Override
    public RecAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_home_page_btn, parent, false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        RecipientModel model = recArrayList.get(position);
        holder.name.setText(model.getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVaccineActivity(model);
            }
        });
    }

    private void startVaccineActivity(RecipientModel rc_model) {
        Intent myIntent = new Intent(this.context, VaccineActivity.class);
        myIntent.putExtra("rc_model", rc_model);
        context.startActivity(myIntent);
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return recArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView name;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_profile_act);
        }
    }
}
