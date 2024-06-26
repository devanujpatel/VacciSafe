package devpatel.apps.vaccisafe;

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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "vaccisafe.db", null, 2);
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
                        " ('Typhoid Conjugate (TCV)', 0, 6, 0,'Vaccine that prevents typhoid fever', 'ALL'),\n" +
                        " ('Influenza-2', 0, 7, 0,'Influenza vaccines, also known as flu jabs or flu shots, are vaccines that protect against infection by influenza viruses', 'ALL'),\n" +
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
                        " ('Annual Influenza', 2, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('Annual Influenza', 3, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('DTwP /DTaP', 4, 0, 0,'A class of combination vaccines against three infectious diseases: Diphtheria, Pertussis (whooping cough), and Tetanus', 'ALL'),\n" +
                        " ('IPV', 4, 0, 0,'Inactivated Polio Vaccine', 'ALL'),\n" +
                        " ('MMR 3', 4, 0, 0,'The MMR vaccine is a vaccine against Measles, Mumps, and Rubella (German measles)', 'ALL'),\n" +
                        " ('Annual Influenza', 4, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('Annual Influenza', 5, 0, 0,'Annual Influenza Vaccine', 'ALL'),\n" +
                        " ('HPV 1 for Girls', 9, 0, 0,'Human Papillomavirus (HPV) vaccines are vaccines that prevent infection by certain types of human papillomavirus (HPV)', 'F'),\n" +
                        " ('HPV 2 for Girls', 9, 9, 0,'Human Papillomavirus (HPV) vaccines are vaccines that prevent infection by certain types of human papillomavirus (HPV)', 'F'),\n" +
                        " ('Tdap/ Td', 10, 0, 0,'Tetanus and adult diphtheria (Td) vaccine is a combination of tetanus and diphtheria with lower concentration of diphtheria antigen (d) as recommended for older children and adults', 'ALL'),\n" +
                        " ('Tdap/ Td', 16, 0, 0,'Tetanus and adult diphtheria (Td) vaccine is a combination of tetanus and diphtheria with lower concentration of diphtheria antigen (d) as recommended for older children and adults', 'ALL');";

        db.execSQL(createRecipientTableStatement);
        db.execSQL(createVaccinesTableStatement);
        db.execSQL(createVaccineRecordsTableStatement);
        db.execSQL(populateVaccinesTable);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i == 1) {
            ContentValues values = new ContentValues();
            values.put("name", "Tdap /Td");
            values.put("given_at_age_from_year", 16);
            values.put("given_at_age_from_month", 0);
            values.put("given_at_age_from_weeks", 0);
            values.put("details", "Tetanus and adult diphtheria (Td) vaccine is a combination of tetanus and diphtheria with lower concentration of diphtheria antigen (d) as recommended for older children and adults");
            values.put("gender", "ALL");
            long vaccine_fk = sqLiteDatabase.insert("vaccines", null, values);

            Cursor recipients_cursor = sqLiteDatabase.rawQuery("SELECT * FROM recipients;", null);

            if (recipients_cursor.moveToFirst()) {
                do {
                    add_vaccine_record_to_db(16, 0, 0, (int) vaccine_fk, sqLiteDatabase, recipients_cursor.getInt(0), recipients_cursor.getInt(3), recipients_cursor.getInt(4), recipients_cursor.getInt(5));
                } while (recipients_cursor.moveToNext());
            } else {
                Log.d(TAG, "DEV get_recipients: error");
            }
            recipients_cursor.close();
        }
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
            vaccine_cursor = db.rawQuery("SELECT * FROM vaccines ORDER BY vaccine_pk;", null);
                    //given_at_age_from_year ASC, given_at_age_from_month ASC, given_at_age_from_weeks ASC;", null);
        } else {
            vaccine_cursor = db.rawQuery("SELECT * FROM vaccines WHERE gender != 'F' ORDER BY vaccine_pk;", null);
                    //ORDER BY given_at_age_from_year ASC, given_at_age_from_month ASC, given_at_age_from_weeks ASC;", null);
        }

        if (vaccine_cursor.moveToFirst()) {
            do {
                add_vaccine_record_to_db(vaccine_cursor.getInt(2), vaccine_cursor.getInt(3), vaccine_cursor.getInt(4), vaccine_cursor.getInt(0), db, getRec(first_name, last_name, db), dob_year, dob_month, day_dob);
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
    private void add_vaccine_record_to_db(int vac_add_year, int vac_add_month, int vac_add_weeks, int vaccine_fk, SQLiteDatabase db, int rec_pk, int g_dob_year, int g_dob_month, int g_dob_day) {
        // start with the dob and later add the required years, months and days
        LocalDate vaccine_date = LocalDate.of(g_dob_year, g_dob_month, g_dob_day);

        vaccine_date = vaccine_date.plusYears(vac_add_year);
        vaccine_date = vaccine_date.plusMonths(vac_add_month);
        vaccine_date = vaccine_date.plusDays(vac_add_weeks * 7);

        ContentValues vaccine_values = new ContentValues();
        vaccine_values.put("vaccine_fk", vaccine_fk);
        vaccine_values.put("recipient_fk", rec_pk);

        LocalDate today_date = LocalDate.now();

        //if (today.compareTo(vaccine_date) > 0) {
        if (vaccine_date.isBefore(today_date) || vaccine_date.equals(today_date)) {
            // recipient past the recommended period for vaccination
            // put vaccine_taken day as today's date
            // reminder date as null
            vaccine_values.put("vac_taken_date", today_date.toString());
        }
        vaccine_values.put("reminder_date_year", vaccine_date.getYear());
        vaccine_values.put("reminder_date_month", vaccine_date.getMonthValue());
        vaccine_values.put("reminder_date_day", vaccine_date.getDayOfMonth());

        db.insert("vaccine_records", null, vaccine_values);
    }

    private String getDetails(int pk, SQLiteDatabase db) {
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor details_cursor = db.rawQuery("SELECT details FROM vaccines WHERE vaccine_pk = " + pk, null);
        String details = null;
        if (details_cursor.moveToFirst()) {
            do {
                details = details_cursor.getString(0);
            } while (details_cursor.moveToNext());
        }
        details_cursor.close();
        //db.close();
        return details;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getRecDob(int pk, SQLiteDatabase db) {
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor dob_cursor = db.rawQuery("SELECT year_dob, month_dob, day_dob FROM recipients WHERE recipient_pk = " + pk, null);
        String dob_string = null;
        if (dob_cursor.moveToFirst()) {
            do {
                DateTimeFormatter mydateformat = DateTimeFormatter.ofPattern("d/MM/uuuu");
                LocalDate rec_dob = LocalDate.of(Integer.parseInt(dob_cursor.getString(0)), Integer.parseInt(dob_cursor.getString(1)), Integer.parseInt(dob_cursor.getString(2)));
                dob_string = rec_dob.format(mydateformat);
                // dob_string = dob_cursor.getString(2) + "-" + dob_cursor.getString(1) + "-" + dob_cursor.getString(0);
            } while (dob_cursor.moveToNext());
        }
        dob_cursor.close();
        //db.close();
        return dob_string;
    }

    private String getVacName(int vac_pk, SQLiteDatabase db) {
        // SQLiteDatabase db = this.getReadableDatabase();
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
        //db.close();
        return name;
    }

    public int getRec(String first_name, String last_name, SQLiteDatabase db) {
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT recipient_pk FROM recipients WHERE first_name = '" + first_name + "' AND last_name = '" + last_name + "';", null);
        int pk;
        if (c.moveToFirst()) {
            pk = c.getInt(0);
        } else {
            pk = -1;
        }
        c.close();
        //db.close();
        return pk;
    }

    public void setTakenAt(String taken_at, String vaccine_fk, String rec_fk) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (taken_at.equals("null")){
            values.putNull("vac_taken_date");
        } else{
            values.put("vac_taken_date ", taken_at);
        }

        db.update("vaccine_records", values, "vaccine_fk=? AND recipient_fk=?",
                new String[]{vaccine_fk, rec_fk});
        db.close();
    }

    public String getRec_fname(int pk, SQLiteDatabase db) {
        // SQLiteDatabase db = this.getReadableDatabase();
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
        //db.close();
        return fname;
    }

    public String getRec_lname(int pk, SQLiteDatabase db) {
        // SQLiteDatabase db = this.getReadableDatabase();
        String lname = null;

        Cursor rec_cursor = db.rawQuery("SELECT last_name FROM recipients WHERE recipient_pk = " + pk + ";", null);
        if (rec_cursor.moveToFirst()) {
            do {
                lname = rec_cursor.getString(0);
            } while (rec_cursor.moveToNext());

        } else {
            Log.d(TAG, "DEV get_recipients: error");
        }
        rec_cursor.close();
        //db.close();
        return lname;
    }

    public void delete_rec(int pk) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("vaccine_records", "recipient_fk=" + pk, null);
        db.delete("recipients", "recipient_pk = " + pk, null);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ReminderModel> getReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Date today = new Date();
        //LocalDate today_ld = LocalDate.of(today.getYear() + 1900, today.getMonth() + 1, today.getDay() + 1);
        LocalDate today_ld = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        today_ld = today_ld.plusDays(2);
        Log.d(TAG, "getReminders: " + today_ld.toString());
        Cursor reminder_cursor = db.rawQuery("SELECT vaccine_fk, recipient_fk, reminder_date_year, reminder_date_month, reminder_date_day FROM vaccine_records WHERE vac_taken_date IS NULL", null);
        ArrayList<ReminderModel> reminders = new ArrayList<>();
        Log.d(TAG, "getReminders: " + reminder_cursor.getCount());
        if (reminder_cursor.moveToFirst()) {
            do {
                LocalDate reminder_date = LocalDate.of(reminder_cursor.getInt(2), reminder_cursor.getInt(3), reminder_cursor.getInt(4));
                if (reminder_date.isBefore(today_ld)) {
                    Log.d(TAG, "getReminders: adding one");
                    reminders.add(new ReminderModel(getVacName(reminder_cursor.getInt(0), db), getRec_fname(reminder_cursor.getInt(1), db), getRec_lname(reminder_cursor.getInt(1), db), reminder_cursor.getInt(0), reminder_cursor.getInt(1), reminder_date));
                } else if (reminder_date.equals(today_ld)) {
                    Log.d(TAG, "getReminders: adding one");
                    reminders.add(new ReminderModel(getVacName(reminder_cursor.getInt(0), db), getRec_fname(reminder_cursor.getInt(1), db), getRec_lname(reminder_cursor.getInt(1), db), reminder_cursor.getInt(0), reminder_cursor.getInt(1), reminder_date));
                }
            } while (reminder_cursor.moveToNext());
        }
        reminder_cursor.close();
        db.close();
        return reminders;
    }

    public String getAge(int id, SQLiteDatabase db) {
        String age = "";
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT given_at_age_from_year, given_at_age_from_month, given_at_age_from_weeks FROM vaccines WHERE vaccine_pk = '" + String.valueOf(id) + "';", null);
        if (c.moveToFirst()) {

            if (c.getString(0).equals("0") && c.getString(1).equals("0") && c.getString(2).equals("0")) {
                return "Birth";
            }

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
        // db.close();
        return age;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<VaccineModel> getVaccineRecords(int pk) {
        ArrayList<VaccineModel> vaccines = new ArrayList<>();
        String[] columns = {"vaccine_fk", "vac_taken_date", "reminder_date_year", "reminder_date_month", "reminder_date_day"};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor vaccine_cursor = db.query("vaccine_records", columns, "recipient_fk=?", new String[]{String.valueOf(pk)}, null, null, "record_pk");

        if (vaccine_cursor.moveToFirst()) {
            do {
                String details = getDetails(vaccine_cursor.getInt(0), db);

                LocalDate today = LocalDate.now();

                LocalDate due_on_ld = LocalDate.of(Integer.parseInt(vaccine_cursor.getString(2)), Integer.parseInt(vaccine_cursor.getString(3)), Integer.parseInt(vaccine_cursor.getString(4)));
                DateTimeFormatter mydateformat = DateTimeFormatter.ofPattern("d/MM/uuuu");
                String dueOn = due_on_ld.format(mydateformat);

                boolean button_clickable = true;
                if (due_on_ld.isAfter(today)) {
                    button_clickable = false;
                }

                VaccineModel vaccineModel = new VaccineModel(this.getVacName(vaccine_cursor.getInt(0), db), vaccine_cursor.getString(1), this.getAge(vaccine_cursor.getInt(0), db), vaccine_cursor.getInt(0), this.context, dueOn, details, button_clickable, pk);
                vaccines.add(vaccineModel);

            } while (vaccine_cursor.moveToNext());

        } else {
            // would be empty though
            Log.d(TAG, "DEV getVaccineRecords: error!");
            vaccines.add(new VaccineModel("Error", "Error", "error", -1, this.context, "error", "error", false, 0));
            vaccine_cursor.close();
            db.close();
            return vaccines;
        }
        vaccine_cursor.close();
        db.close();
        return vaccines;
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
}