package devpatel.apps.vaccisafe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        sendReminders(context);
    }

    private void makeNotification(String title, String content, int value, Context my_this) {

        Intent click_intent = new Intent(my_this, VaccineActivity.class);
        PendingIntent click_pd_intent = PendingIntent.getActivity(my_this, 1, click_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(my_this, "VacciSafe Notification Channel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(click_pd_intent)
                .setAutoCancel(true)
                .build();

        //create notification channel (docs say can call repeatedly: it will have not effect unless channel_id is different)
        NotificationChannel notificationChannel = new NotificationChannel("VacciSafe Notification Channel", "VacciSafe Vaccine Reminders", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("This notification channel is necessary for receiving timely vaccine reminders.");
        NotificationManager manager = my_this.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(my_this);
        notificationManager.notify(value, notification);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendReminders(Context my_this) {
        // make calls from database and run the logic as done before
        DataBaseHelper db = new DataBaseHelper(my_this);
        ArrayList<ReminderModel> reminders = db.getReminders();

        HashMap<Integer, String> rec_to_rem = new HashMap<Integer, String>();
        HashMap<Integer, String> rec_to_name = new HashMap<Integer, String>();

        for (int i = 0; i < reminders.size(); i++) {
            String content;
            if (rec_to_rem.get(reminders.get(i).getRec_pk()) == null) {
                content = "->" + reminders.get(i).getVaccine_name();
                rec_to_name.put(reminders.get(i).getRec_pk(), reminders.get(i).getRec_fname() + " " + reminders.get(i).getRec_lname());
            } else {
                content = (String) rec_to_rem.get(reminders.get(i).getRec_pk());
                content += "->" + reminders.get(i).getVaccine_name();
            }
            content += my_this.getString(R.string.due_on_with_spaces) + reminders.get(i).getreminder_date();
            content += "\n";
            rec_to_rem.put(reminders.get(i).getRec_pk(), content);
        }

        for (int value : rec_to_rem.keySet()) {
            makeNotification(my_this.getString(R.string.vaccisafe_reminder_for) + rec_to_name.get(value), rec_to_rem.get(value), value, my_this);
        }
    }

}