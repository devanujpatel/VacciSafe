package devpatel.apps.vaccisafe;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
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
import java.util.Calendar;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import hotchemi.android.rate.StoreType;

public class ProfileActivity extends AppCompatActivity {
    Button make_new_profile_btn;
    private RecyclerView profiles_rv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        boolean show_battery_optimize = true;

        SharedPreferences sharedPreferences = getSharedPreferences("First Run", MODE_PRIVATE);
        String alarm_stat = sharedPreferences.getString("first run Status", "first run");

        if (alarm_stat.equals("first run")) {
            show_battery_optimize = false;
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("first run Status", "not first run");
            myEdit.apply();
        }

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        Button about_btn = findViewById(R.id.about_btn);
        about_btn.getBackground().setAlpha(170);
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create the Dialog here
                Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.custom_about_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false); //Optional
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corner));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                TextView Okay = dialog.findViewById(R.id.btn_okay);

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
        // scheduleJob();

        make_new_profile_btn = findViewById(R.id.add_recipient);
        make_new_profile_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, MakeNewProfile.class);
                startActivity(myIntent);
            }
        });

        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY) //default is Google, other option is Amazon
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(2) // default 10 times.
                .setRemindInterval(2) // default 1 day.
                .setShowLaterButton(true) // default true.
                .setDebug(false) // default false.
                .setCancelable(false) // default false.
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        if (which == -1) {
                            open_play_store();
                        }
                    }
                })
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        TextView rate_vaccisafe_tv = findViewById(R.id.rate_us);
        rate_vaccisafe_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_play_store();
            }
        });

        TextView share_tv = findViewById(R.id.share);
        share_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download VacciSafe: an all-in-one solution to manage vaccine schedules.\n https://play.google.com/store/apps/details?id=devpatel.apps.vaccisafe");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

        if (show_battery_optimize) {
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.custom_open_battery_optimization_settings_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corner));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Button open_settings = dialog.findViewById(R.id.open_settings);

                open_settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent();
                        myIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        startActivity(myIntent);
                    }
                });

                TextView Okay = dialog.findViewById(R.id.btn_okay);

                Okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }

        startAlarm();
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

    private void startAlarm() {
        SharedPreferences sharedPreferences = getSharedPreferences("Alarm Status Shared Pref", MODE_PRIVATE);
        String alarm_stat = sharedPreferences.getString("alarm status", "Not yet set");

        if (alarm_stat.equals("Not yet set")) {

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("alarm status", "alarm set");
            myEdit.apply();
        }
    }

    /*

        private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
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
    }*/
}