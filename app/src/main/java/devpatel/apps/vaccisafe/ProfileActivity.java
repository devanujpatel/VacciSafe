package devpatel.apps.vaccisafe;

import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {
    Button make_new_profile_btn;
    private RecyclerView profiles_rv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();

        Button about_btn = findViewById(R.id.about_btn);
        about_btn.getBackground().setAlpha(170);
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create the Dialog here
                Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false); //Optional
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corner));
                }
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Button Okay = dialog.findViewById(R.id.btn_okay);

                Okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // look for reminders once every day
        Log.d(TAG, "Scheduling job!");
        scheduleJob();

        make_new_profile_btn = findViewById(R.id.add_recipient);
        make_new_profile_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, MakeNewProfile.class);
                startActivity(myIntent);
            }
        });

        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(0) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        open_play_store();
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);

        TextView rate_vaccisafe_tv = findViewById(R.id.rate_us);
        rate_vaccisafe_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_play_store();
            }
        });
        displayRecList();
    }

    public void open_play_store() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=devpatel.apps.vaccisafe" + appPackageName)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        displayRecList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayRecList() {
        DataBaseHelper db = new DataBaseHelper(ProfileActivity.this);
        profiles_rv = findViewById(R.id.profiles_list);
        ArrayList<RecipientModel> recipients = db.get_recipients();
        RecAdapter recAdapter = new RecAdapter(this, recipients);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        profiles_rv.setLayoutManager(linearLayoutManager);
        profiles_rv.setAdapter(recAdapter);
    }

    private void scheduleJob() {
        Log.d(TAG, "Scheduling job! 2");
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(
                Context.JOB_SCHEDULER_SERVICE);

// The JobService that we want to run
        final ComponentName name = new ComponentName(this, ReminderService.class);

// Schedule the job
        final int result = jobScheduler.schedule(getJobInfo(13, name));

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Scheduled job successfully!");
        }
    }

    private JobInfo getJobInfo(final int id, final ComponentName name) {
        final long interval = 86400000;
        final JobInfo jobInfo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .build();
        }
        return jobInfo;
    }
}