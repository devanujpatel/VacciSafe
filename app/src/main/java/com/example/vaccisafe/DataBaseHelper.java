package com.example.vaccisafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "vaccisafe.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createRecipientTableStatement = "CREATE TABLE recipients(\n" +
                "\trecipient_pk INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tfirst_name TEXT NOT NULL,\n" +
                "\tlast_name TEXT NOT NULL,\n" +
                "\tyear_dob INT NOT NULL,\n" +
                "\tmonth_dob INT NOT NULL,\n" +
                "\tday_dob INT NOT NULL,\n" +
                "\tgender TEXT NOT NULL);";

        String createVaccinesTableStatement =
                "CREATE TABLE vaccines (\n" +
                        "\tvaccine_pk INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    \tname TEXT NOT NULL,\n" +
                        "    \tgiven_at_age_from_year TEXT NOT NULL,\n" +
                        "    \tgiven_at_age_from_month TEXT NOT NULL,\n" +
                        "    \tgiven_at_age_from_weeks TEXT NOT NULL,\n" +
                        "    \tdetails TEXT,\n" +
                        "    \tgender TEXT\n" +
                        ");";

        String createVaccineRecordsTableStatement =
                "CREATE TABLE vaccine_records(\n" +
                        "\trecord_pk INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    \tvaccine_fk INT NOT NULL,\n" +
                        "    \trecipient_fk INT NOT NULL,\n" +
                        "    \treminder_date_year INT DEFAULT null,\n" +
                        "    \treminder_date_month INT DEFAULT null,\n" +
                        "    \treminder_date_day INT DEFAULT null,\n" +
                        "    \tvac_taken_date TEXT DEFAULT null," +
                        "\n" +
                        "\t CONSTRAINT vaccine_fk_in_vaccine_records\n" +
                        "    \t\tFOREIGN KEY (vaccine_fk)\n" +
                        "    \t\tREFERENCES vaccines(vaccine_pk)\n," +
                        "      \n" +
                        "  \tCONSTRAINT recipient_fk_in_vaccine_records\n" +
                        "    \t\tFOREIGN KEY (recipient_fk)\n" +
                        "    \t\tREFERENCES recipients(recipient_pk))\n";

        String populateVaccinesTable =
                "INSERT INTO vaccines(name, given_at_age_from_year, given_at_age_from_month, given_at_age_from_weeks, details, gender) VALUES  " +

                        " ('BCG', 0, 0, 0,'Bacille Calmette-Guérin (BCG) is a vaccine for Tuberculosis (TB) disease', 'ALL'),\n" +
                        " ('Hep B1', 0, 0, 0,'Hepatitis Dose - At birth or as early as possible within 24 hours, 0.5 ml, Intramuscular, Antero lateral side of mid thigh', 'ALL'),\n" +
                        " ('OPV', 0, 0, 0,'Polio vaccine, At birth or as early as possible within the first 15 days, 2 drops,	Oral', 'ALL'),\n" +
                        " ('DTwP /DTaP1', 0, 1, 2,'A class of combination vaccines against three infectious diseases: diphtheria, pertussis, and tetanus', 'ALL'),\n" +
                        " ('Hib-1', 0, 1, 2,'Hib vaccine can prevent Haemophilus influenzae type b disease', 'ALL'),\n" +
                        " ('IPV-1', 0, 1, 2,'Inactivated Polio Vaccine', 'ALL'),\n" +
                        " ('Hep B2', 0, 1, 2,'Hepatitis B vaccine prevents Hepatitis B', 'ALL'),\n" +
                        " ('PCV 1', 0, 1, 2,'Pneumococcal conjugate vaccine is a pneumococcal vaccine and a conjugate vaccine used to protect infants, young children, and adults against disease caused by the bacterium Streptococcus pneumoniae (pneumococcus)', 'ALL'),\n" +
                        " ('Rota-1', 0, 1, 2,'Rotavirus vaccine is a vaccine used to protect against rotavirus infections, which are the leading cause of severe diarrhea among young children', 'ALL'),\n" +
                        " ('DTwP /DTaP2', 0, 2, 2,'A class of combination vaccines against three infectious diseases: diphtheria, pertussis, and tetanus', 'ALL'),\n" +
                        " ('Hib-2', 0, 2, 2,'Hib vaccine can prevent Haemophilus Influenzae type b disease', 'ALL'),\n" +
                        " ('IPV-2', 0, 2, 2,'Inactivated Polio Vaccine', 'ALL'),\n" +
                        " ('Hep B3', 0, 2, 2,'Hepatitis B vaccine prevents Hepatitis B', 'ALL'),\n" +
                        " ('PCV 2', 0, 2, 2,'Pneumococcal conjugate vaccine is a pneumococcal vaccine and a conjugate vaccine used to protect infants, young children, and adults against disease caused by the bacterium Streptococcus pneumoniae (pneumococcus)', 'ALL'),\n" +
                        " ('Rota-2', 0, 2, 2,'Rotavirus vaccine is a vaccine used to protect against rotavirus infections, which are the leading cause of severe diarrhea among young children', 'ALL'),\n" +
                        " ('DTwP /DTaP3', 0, 3, 2,'A class of combination vaccines against three infectious diseases: Diphtheria, Pertussis (whooping cough), and Tetanus', 'ALL'),\n" +
                        " ('Hib-3', 0, 3, 2,'Hib vaccine can prevent Haemophilus Influenzae type b disease', 'ALL'),\n" +
                        " ('IPV-3', 0, 3, 2,'Inactivated Polio Vaccine', 'ALL'),\n" +
                        " ('Hep B4', 0, 3, 2,'Hepatitis B vaccine prevents Hepatitis B', 'ALL'),\n" +
                        " ('PCV 3', 0, 3, 2,'Pneumococcal conjugate vaccine is a pneumococcal vaccine and a conjugate vaccine used to protect infants, young children, and adults against disease caused by the bacterium Streptococcus pneumoniae (pneumococcus)', 'ALL'),\n" +
                        " ('Rota-3', 0, 3, 2,'Rotavirus vaccine is a vaccine used to protect against rotavirus infections, which are the leading cause of severe diarrhea among young children', 'ALL'),\n" +
                        " ('Influenza-1', 0, 6, 0,'Influenza vaccines, also known as flu jabs or flu shots, are vaccines that protect against infection by influenza viruses', 'ALL'),\n" +
                        " ('Influenza-2', 0, 7, 0,'Influenza vaccines, also known as flu jabs or flu shots, are vaccines that protect against infection by influenza viruses', 'ALL'),\n" +
                        " ('Typhoid Conjugate Vaccine', 0, 6, 0,'Vaccine that prevents typhoid fever', 'ALL'),\n" +
                        " ('MMR 1', 0, 9, 0,'The MMR vaccine is a vaccine against Measles, Mumps, and Rubella (German measles)', 'ALL'),\n" +
                        " ('Hepatitis A-1', 1, 0, 0,'Hepatitis A vaccine is a vaccine that prevents Hepatitis A', 'ALL'),\n" +
                        " ('PCV Booster', 1, 0, 0,'Pneumococcal conjugate vaccine is a pneumococcal vaccine and a conjugate vaccine used to protect infants, young children, and adults against disease caused by the bacterium Streptococcus pneumoniae (pneumococcus)', 'ALL'),\n" +
                        " ('MMR 2', 1, 3, 0,'The MMR vaccine is a vaccine against Measles, Mumps, and Rubella (German measles)', 'ALL'),\n" +
                        " ('Varicella', 1, 3, 0,'Varicella vaccine, also known as Chickenpox vaccine, is a vaccine that protects against Chickenpox', 'ALL'),\n" +
                        " ('DTwP /DTaP', 1, 4, 0,'A class of combination vaccines against three infectious diseases: Diphtheria, Pertussis (whooping cough), and Tetanus', 'ALL'),\n" +
                        " ('Hib', 1, 4, 0,'The Haemophilus influenzae type B vaccine, also known as Hib vaccine, is a vaccine used to prevent Haemophilus influenzae type b (Hib) infection', 'ALL'),\n" +
                        " ('IPV', 1, 4, 0,'Inactivated Polio Vaccine', 'ALL'),\n" +
                        " ('Hepatitis A-2', 1, 6, 0,'Hepatitis A vaccine is a vaccine that prevents Hepatitis A', 'ALL'),\n" +
                        " ('Varicella 2', 1, 6, 0,'Varicella vaccine, also known as Chickenpox vaccine, is a vaccine that protects against Chickenpox', 'ALL'),\n" +
                        " ('Annual Influenza Vaccine', 2, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('Annual Influenza Vaccine', 3, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('DTwP /DTaP', 4, 0, 0,'A class of combination vaccines against three infectious diseases: Diphtheria, Pertussis (whooping cough), and Tetanus', 'ALL'),\n" +
                        " ('IPV', 4, 0, 0,'Inactivated Polio Vaccine', 'ALL'),\n" +
                        " ('MMR 3', 4, 0, 0,'The MMR vaccine is a vaccine against Measles, Mumps, and Rubella (German measles)', 'ALL'),\n" +
                        " ('Annual Influenza Vaccine', 4, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('Annual Influenza Vaccine', 5, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('HPV 1 for Girls', 9, 0, 0,'Human Papillomavirus (HPV) vaccines are vaccines that prevent infection by certain types of human papillomavirus (HPV)', 'F'),\n" +
                        " ('HPV 2 for Girls', 9, 9, 0,'Human Papillomavirus (HPV) vaccines are vaccines that prevent infection by certain types of human papillomavirus (HPV)', 'F'),\n" +
                        " ('Tdap/ Td', 10, 0, 0,'Tetanus and adult diphtheria (Td) vaccine is a combination of tetanus and diphtheria with lower concentration of diphtheria antigen (d) as recommended for older children and adults', 'ALL');";

        db.execSQL(createRecipientTableStatement);
        db.execSQL(createVaccinesTableStatement);
        db.execSQL(createVaccineRecordsTableStatement);
        db.execSQL(populateVaccinesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Object[] makeProfile(String first_name, String last_name, int dob_year, int dob_month, int day_dob, String gender) {
        Object[] return_values = new Object[2];

        if (first_name.equals("") || last_name.equals("")) {
            return_values[0] = "empty name";
            return_values[1] = null;
            return return_values;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor first_names = db.rawQuery("SELECT first_name FROM recipients;", null);

        if (first_names.moveToFirst()) {
            do {
                if (first_name.equals(first_names.getString(0))) {
                    Cursor last_names = db.rawQuery("SELECT last_name FROM recipients;", null);
                    if (last_names.moveToFirst()) {
                        do {
                            if (last_name.equals(last_names.getString(0))) {
                                return_values[0] = "duplicate name";
                                return_values[1] = null;
                                return return_values;
                            }
                        } while (last_names.moveToNext());
                    }
                }
            } while (first_names.moveToNext());
        }

        ContentValues values = new ContentValues();
        values.put("first_name", first_name);
        values.put("last_name", last_name);
        values.put("year_dob", dob_year);
        values.put("month_dob", dob_month);
        values.put("day_dob", day_dob);
        values.put("gender", gender);

        long success = db.insert("recipients", null, values);

        if (success == -1) {
            Log.e(TAG, "makeProfile: adding recipient to db error place 1");
            return_values[0] = "error";
            return_values[1] = null;
            return return_values;
        }

        Cursor vaccine_cursor;
        // get all vaccines data
        if (gender.equals("Female")) {
            vaccine_cursor = db.rawQuery("SELECT * FROM vaccines ORDER BY given_at_age_from_year ASC, given_at_age_from_month ASC, given_at_age_from_weeks ASC;", null);
        } else {
            vaccine_cursor = db.rawQuery("SELECT * FROM vaccines WHERE gender != 'F' ORDER BY given_at_age_from_year ASC, given_at_age_from_month ASC, given_at_age_from_weeks ASC;", null);
        }
        Log.e(TAG, "makeProfile: " + vaccine_cursor.getCount());

        if (vaccine_cursor.moveToFirst()) {
            do {
                int vac_add_year = vaccine_cursor.getInt(2);
                int vac_add_month = vaccine_cursor.getInt(3);
                int vac_add_weeks = vaccine_cursor.getInt(4);

                Log.e(TAG, "makeProfile: " + vac_add_year + vac_add_month + vac_add_weeks);

                // start with the dob and later add the required years, months and days
                LocalDate vaccine_date = LocalDate.of(dob_year, dob_month, day_dob);

                vaccine_date = vaccine_date.plusYears(vac_add_year);
                vaccine_date = vaccine_date.plusMonths(vac_add_month);
                vaccine_date = vaccine_date.plusDays(vac_add_weeks * 7);

                ContentValues vaccine_values = new ContentValues();
                vaccine_values.put("vaccine_fk", vaccine_cursor.getInt(0));
                vaccine_values.put("recipient_fk", getRec(first_name, last_name));

                LocalDate today_date = LocalDate.now();

                // to get the formatted today date to enter in database and to show on screen
                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //Calendar cal = Calendar.getInstance();
                //Date date = cal.getTime();
                //String today_date_string = dateFormat.format(date);

                Log.e(TAG, "makeProfile: today date" + today_date.toString());
                Log.e(TAG, "makeProfile: vaccine date" + vaccine_date.toString());

                //if (today.compareTo(vaccine_date) > 0) {
                if (vaccine_date.isBefore(today_date) || vaccine_date.equals(today_date)) {
                    // recipient past the recommended period for vaccination
                    // put vaccine_taken day as today's date
                    // reminder date as null
                    Log.d(TAG, "makeProfile: case 1");
                    vaccine_values.put("vac_taken_date", today_date.toString());
                }
                 /*
                if (vaccine_date.equals(today_date)) {
                    Log.d(TAG, "makeProfile: case 1");
                    vaccine_values.put("vac_taken_date", today_date_string);
                }
                 (vaccine_date.isAfter(today_date))
                */
                else {
                    Log.d(TAG, "makeProfile: case 2");
                    vaccine_values.put("reminder_date_year", vaccine_date.getYear());
                    vaccine_values.put("reminder_date_month", vaccine_date.getMonthValue());
                    vaccine_values.put("reminder_date_day", vaccine_date.getDayOfMonth());
                }

                SQLiteDatabase new_db = this.getWritableDatabase();
                new_db.insert("vaccine_records", null, vaccine_values);
                new_db.close();
            } while (vaccine_cursor.moveToNext());
        }
        vaccine_cursor.close();
        first_names.close();
        db.close();
        return_values[0] = "recipient added";
        LocalDate dob = LocalDate.of(dob_year, dob_month, day_dob);
        DateTimeFormatter mydateformat = DateTimeFormatter.ofPattern("d/MM/uuuu");
        return_values[1] = new RecipientModel(first_name, last_name, dob.format(mydateformat), (int) success);
        return return_values;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<RecipientModel> get_recipients() {
        ArrayList<RecipientModel> recipients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor rec_cursor = db.rawQuery("SELECT first_name, last_name, year_dob, month_dob, day_dob, recipient_pk FROM recipients;", null);
        if (rec_cursor.moveToFirst()) {
            do {
                LocalDate dob = LocalDate.of(Integer.parseInt(rec_cursor.getString(2)), Integer.parseInt(rec_cursor.getString(3)), Integer.parseInt(rec_cursor.getString(4)));
                DateTimeFormatter mydateformat = DateTimeFormatter.ofPattern("d/MM/uuuu");
                RecipientModel recipientModel = new RecipientModel(rec_cursor.getString(0), rec_cursor.getString(1), dob.format(mydateformat), rec_cursor.getInt(5));
                recipients.add(recipientModel);
            } while (rec_cursor.moveToNext());

        } else {
            Log.d(TAG, "DEV get_recipients: error");
        }
        rec_cursor.close();
        db.close();
        return recipients;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<VaccineModel> getVaccineRecords(int pk) {
        ArrayList<VaccineModel> vaccines = new ArrayList<>();
        String[] columns = {"vaccine_fk, vac_taken_date", "reminder_date_year", "reminder_date_month", "reminder_date_day"};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor vaccine_cursor = db.query("vaccine_records", columns, "recipient_fk=?", new String[]{String.valueOf(pk)}, null, null, "vaccine_fk");
        Log.d(TAG, "DEV PATEL getVaccineRecords: column count: " + vaccine_cursor.getColumnCount());
        if (vaccine_cursor.moveToFirst()) {
            do {

                String details = getDetails(vaccine_cursor.getInt(0));

                Log.d(TAG, "DEV getVaccineRecords: adding: " + this.getVacName(vaccine_cursor.getInt(0)));
                String dueOn;
                if (vaccine_cursor.getString(4) != null) {
                    LocalDate rec_dob = LocalDate.of(Integer.parseInt(vaccine_cursor.getString(2)), Integer.parseInt(vaccine_cursor.getString(3)), Integer.parseInt(vaccine_cursor.getString(4)));
                    //rec_dob.plusYears(Long.parseLong(vaccine_cursor.getString(2)));
                    //rec_dob.plusMonths(Long.parseLong(vaccine_cursor.getString(3)));
                    //rec_dob.plusDays(Long.parseLong(vaccine_cursor.getString(4)));
                    //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    //dueOn = dateFormat.format(rec_dob);
                    // String text = date.format(mydateformat);
                    //dueOn = rec_dob.toString();
                    DateTimeFormatter mydateformat = DateTimeFormatter.ofPattern("d/MM/uuuu");
                    dueOn = rec_dob.format(mydateformat);
                } else {
                    dueOn = getRecDob(pk);
                }
                VaccineModel vaccineModel = new VaccineModel(this.getVacName(vaccine_cursor.getInt(0)), vaccine_cursor.getString(1), this.getAge(vaccine_cursor.getInt(0)), vaccine_cursor.getInt(0), this.context, dueOn, details);
                vaccines.add(vaccineModel);
            } while (vaccine_cursor.moveToNext());

        } else {
            // would be empty though
            Log.d(TAG, "DEV getVaccineRecords: error!");
            vaccines.add(new VaccineModel("Error", "Error", "error", -1, this.context, "error", "error"));
            vaccine_cursor.close();
            db.close();
            return vaccines;
        }
        vaccine_cursor.close();
        db.close();
        return vaccines;
    }

    private String getDetails(int pk) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor details_cursor = db.rawQuery("SELECT details FROM vaccines WHERE vaccine_pk = " + pk, null);
        String details = null;
        if (details_cursor.moveToFirst()) {
            do {
                details = details_cursor.getString(0);
            } while (details_cursor.moveToNext());
        }
        details_cursor.close();
        db.close();
        return details;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getRecDob(int pk) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dob_cursor = db.rawQuery("SELECT year_dob, month_dob, day_dob FROM recipients WHERE recipient_pk = " + pk, null);
        String dob_string = null;
        if (dob_cursor.moveToFirst()) {
            do {
                // TODO: change here
                DateTimeFormatter mydateformat = DateTimeFormatter.ofPattern("d/MM/uuuu");
                LocalDate rec_dob = LocalDate.of(Integer.parseInt(dob_cursor.getString(0)), Integer.parseInt(dob_cursor.getString(1)), Integer.parseInt(dob_cursor.getString(2)));
                dob_string = rec_dob.format(mydateformat);
                // dob_string = dob_cursor.getString(2) + "-" + dob_cursor.getString(1) + "-" + dob_cursor.getString(0);
            } while (dob_cursor.moveToNext());
        }
        dob_cursor.close();
        db.close();
        return dob_string;
    }

    private String getVacName(int vac_pk) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM vaccines WHERE vaccine_pk = ?", new String[]{String.valueOf(vac_pk)});
        String name = null;
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(0);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "getVacName: at get vac name place2 ");
            name = "error at getVacName";
        }
        cursor.close();
        db.close();
        return name;
    }

    public int getRec(String first_name, String last_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT recipient_pk FROM recipients WHERE first_name = '" + first_name + "' AND last_name = '" + last_name + "';", null);
        int pk;
        if (c.moveToFirst()) {
            pk = c.getInt(0);
        } else {
            pk = -1;
        }
        c.close();
        db.close();
        Log.d(TAG, "DEV getRec: rec pk is " + pk);
        return pk;
    }

    public String getAge(int id) {
        String age = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT given_at_age_from_year, given_at_age_from_month, given_at_age_from_weeks FROM vaccines WHERE vaccine_pk = '" + String.valueOf(id) + "';", null);
        if (c.moveToFirst()) {

            if (c.getString(0).equals("0") && c.getString(1).equals("0") && c.getString(2).equals("0")) {
                return "Birth";
            }

            Log.d(TAG, "getAge: " + c.getString(0) + c.getString(1) + c.getString(2));

            if (!c.getString(0).equals("0")) {
                age += c.getString(0);
                age += "Y ";
            }

            if (!c.getString(1).equals("0")) {
                age += c.getString(1);
                age += "M ";
            }

            if (!c.getString(2).equals("0")) {
                age += c.getString(2);
                age += "W";
            }

        } else {
            Log.e(TAG, "getAge: age error place 3");
            age = "error";
        }
        c.close();
        db.close();
        Log.d(TAG, "DEV getRec: rec pk is " + age);
        return age;
    }

    public void setTakenAt(String taken_at, String vaccine_fk) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("vac_taken_date ", taken_at);

        db.update("vaccine_records", values, String.format("%s = ?", "vaccine_fk"),
                new String[]{vaccine_fk});
        db.close();
    }

    public ArrayList<ReminderModel> getReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Date today = new Date();
        // Cursor reminder_cursor = db.rawQuery("SELECT vaccine_fk, recipient_fk FROM vaccine_records WHERE reminder_date_year = " + (today.getYear() +1900) + " AND reminder_date_month = " + (today.getMonth() + 1) + " AND reminder_date_day = " + today.getDate(), null);
        Cursor reminder_cursor = db.rawQuery("SELECT vaccine_fk, recipient_fk FROM vaccine_records WHERE reminder_date_year=" + 2022 + " AND reminder_date_month = " + 4 + " AND reminder_date_day = " + 3, null);
        ArrayList<ReminderModel> reminders = new ArrayList<>();

        if (reminder_cursor.moveToFirst()) {
            do {
                reminders.add(new ReminderModel(getVacName(reminder_cursor.getInt(0)), getRec_fname(reminder_cursor.getInt(1)), getRec_fname(reminder_cursor.getInt(1)), reminder_cursor.getInt(0), reminder_cursor.getInt(1)));
            } while (reminder_cursor.moveToNext());
        }
        Log.e(TAG, "getReminders: " + reminders.size());
        reminder_cursor.close();
        db.close();
        return reminders;
    }

    public String getRec_fname(int pk) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fname = null;

        Cursor rec_cursor = db.rawQuery("SELECT first_name FROM recipients WHERE recipient_pk = " + pk + ";", null);
        if (rec_cursor.moveToFirst()) {
            do {
                fname = rec_cursor.getString(0);
            } while (rec_cursor.moveToNext());

        } else {
            Log.d(TAG, "DEV get_recipients: error");
        }
        rec_cursor.close();
        db.close();
        return fname;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getDOB(int pk) {
        SQLiteDatabase db = this.getReadableDatabase();

        LocalDate rec_dob = null;

        Cursor rec_cursor = db.rawQuery("SELECT year_dob, month_dob, day_dob FROM recipients WHERE recipient_pk = " + pk + ";", null);
        if (rec_cursor.moveToFirst()) {
            do {
                rec_dob = LocalDate.of(rec_cursor.getInt(0), rec_cursor.getInt(1), rec_cursor.getInt(2));
            } while (rec_cursor.moveToNext());

        } else {
            Log.d(TAG, "DEV get_recipients: error");
        }
        rec_cursor.close();
        db.close();
        return rec_dob;
    }

    public void delete_rec(int pk) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("vaccine_records", "recipient_fk=" + pk, null);
        db.delete("recipients", "recipient_pk = " + pk, null);
        db.close();
    }
}

/*
public void viewVaccines() {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM vaccines ORDER BY given_at_age_from_year ASC, given_at_age_from_month ASC, given_at_age_from_weeks ASC;";
    Cursor cursor = db.rawQuery(query, null);
    if (cursor.moveToFirst()) {
        do {
            Log.d(TAG, "DEV viewVaccines: " + cursor.getString(1));
        } while (cursor.moveToNext());

    } else {
        Log.d(TAG, "DEV viewVaccines: no data in vaccines table!");
    }
    cursor.close();
    db.close();
}


    public String getRec_lname(int pk) {
        SQLiteDatabase db = this.getReadableDatabase();
        String lname = null;
        Cursor rec_cursor = db.rawQuery("SELECT last_name FROM recipients WHERE recipient_pk = " + pk + "''", null);
        if (rec_cursor.moveToFirst()) {
            do {
                lname = rec_cursor.getString(0);
            } while (rec_cursor.moveToNext());
        } else {
            Log.d(TAG, "DEV get_recipients: error");
        }
        rec_cursor.close();
        db.close();
        return lname;
    }

*/