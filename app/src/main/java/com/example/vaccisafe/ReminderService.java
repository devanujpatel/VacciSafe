package com.example.vaccisafe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReminderService extends JobService {

    private static final String TAG = ReminderService.class.getSimpleName();
    private static final String CHANNEL_ID = "VacciSafe Channel 1";

    @Override
    public boolean onStartJob(final JobParameters params) {

        HandlerThread handlerThread = new HandlerThread("SomeOtherThread");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                sendReminders();
                jobFinished(params, true);
            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        Log.d(TAG, "onStopJob() was called");
        return true;
    }

    private void makeNotification(String title, String content) {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                //.setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();

        notificationManager.notify(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            CharSequence name = "VacciSafe Vaccine Reminders";
            String description = "This notification channel is necessary for receiving your timely vaccine reminders.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendReminders() {
        // make calls from database and run the logic as done before
        DataBaseHelper db = new DataBaseHelper(this);
        ArrayList<ReminderModel> reminders = db.getReminders();

        HashMap<Integer, String> rec_to_rem = new HashMap<Integer, String>();

        for (int i = 0; i < reminders.size(); i++) {
            String content;
            if (rec_to_rem.get(reminders.get(i).getRec_pk()) == null) {
                // ArrayList<String> array = new ArrayList<>();
                //array.add(reminders.get(i).getVaccine_name());
                content = reminders.get(i).getVaccine_name();
            } else {
                //ArrayList<String> array = (ArrayList<String>) rec_to_rem.get(reminders.get(i).getRec_pk());
                //array.add(reminders.get(i).getVaccine_name());
                content = (String) rec_to_rem.get(reminders.get(i).getRec_pk());
                content += "->" + reminders.get(i).getVaccine_name();
            }
            content += "\n";
            rec_to_rem.put(reminders.get(i).getRec_pk(), content);
        }

        for (int value : rec_to_rem.keySet()) {
            makeNotification("Vaccine Reminder for " + db.getRec_fname(value), "Your following vaccine(s) are due:\n" + rec_to_rem.get(value));
        }

    }
}