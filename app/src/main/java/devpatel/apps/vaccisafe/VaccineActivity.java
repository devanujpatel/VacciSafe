package devpatel.apps.vaccisafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class VaccineActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "VacciSafe";
    private static final int NOTIFICATION_ID = 13;
    String first_name;
    String last_name;
    int rec_pk;
    DataBaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);

        Intent intent = getIntent();
        RecipientModel rc_model = (RecipientModel) intent.getSerializableExtra("rc_model");
        first_name = rc_model.getFirst_name();
        last_name = rc_model.getLast_name();
        String rc_dob = rc_model.getDob();

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();

        TextView name = view.findViewById(R.id.name_plus_dob_app_title_plus_bar);
        db = new DataBaseHelper(this);
        name.setText(" - " + first_name + " (" + rc_dob + ")");
        rec_pk = db.getRec(first_name, last_name, db.getReadableDatabase());
        ArrayList<VaccineModel> vaccines = db.getVaccineRecords(rec_pk);
        RecyclerView recyclerView = findViewById(R.id.vaccines_recycler_view);
        VaccineRecyclerAdapter adapter = new VaccineRecyclerAdapter(vaccines);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home_page = new Intent(VaccineActivity.this, ProfileActivity.class);
        startActivity(home_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vaccines_activity_menu, menu);
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_baseline_delete_24), "Delete Recipient")).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                db.delete_rec(rec_pk);
                Intent myIntent = new Intent(VaccineActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_delete_rec);
        item.setTitle(first_name + " " + last_name);
        return super.onPrepareOptionsMenu(menu);
    }

}
