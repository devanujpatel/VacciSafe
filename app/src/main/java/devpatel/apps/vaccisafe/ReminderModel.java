package devpatel.apps.vaccisafe;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class ReminderModel {
    String vaccine_name, rec_fname, rec_lname;
    int vaccine_pk, rec_pk;
    LocalDate reminder_date;

    public ReminderModel(String vaccine_name, String rec_fname, String rec_lname, int vaccine_pk, int rec_pk, LocalDate reminder_date) {
        this.vaccine_name = vaccine_name;
        this.rec_fname = rec_fname;
        this.rec_lname = rec_lname;
        this.vaccine_pk = vaccine_pk;
        this.rec_pk = rec_pk;
        this.reminder_date = reminder_date;
    }

    public String getRec_fname() {
        return rec_fname;
    }

    public String getRec_lname() {
        return rec_lname;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public int getRec_pk() {
        return rec_pk;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getreminder_date() {
        char[] charArray = reminder_date.getMonth().toString().toLowerCase().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        StringBuilder resultPlaceHolder = new StringBuilder(reminder_date.getMonth().toString().length());
        resultPlaceHolder.append(new String(charArray));
        return reminder_date.getDayOfMonth() + " " + resultPlaceHolder + " " + reminder_date.getYear();
    }

}
