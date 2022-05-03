package com.example.vaccisafe;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {
    Button make_new_profile_btn;
    private RecyclerView profiles_rv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        // look for reminders once every day
        scheduleJob();

        make_new_profile_btn = findViewById(R.id.add_recipient);
        make_new_profile_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, MakeNewProfile.class);
                startActivity(myIntent);
            }
        });

        displayRecList();
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
        final long interval = 24 * 60 * 60 * 1000; // run once every day
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

    public void openDialog() {
        languageChangeDialog languageChangeDialog = new languageChangeDialog();
        languageChangeDialog.show(getSupportFragmentManager(), "Language Dialog");
    }

}