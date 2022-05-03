package com.example.vaccisafe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class MakeNewProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    int year_dob;
    int month_dob;
    int day_dob;
    Button add_rec_btn;
    TextInputEditText first_name_et, last_name_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_profile);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        add_rec_btn = findViewById(R.id.add_rec_btn);
        add_rec_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DataBaseHelper db = new DataBaseHelper(MakeNewProfile.this);
                first_name_et = findViewById(R.id.first_name_edit);
                last_name_et = findViewById(R.id.last_name_edit);

                String rec_first_name = first_name_et.getText().toString().trim();
                String rec_last_name = last_name_et.getText().toString().trim();

                RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);

                boolean gender_not_provided = false;
                String rec_gender = "";
                try {
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
                    rec_gender = (String) radioSexButton.getText();
                } catch (
                        NullPointerException e) {
                    gender_not_provided = true;
                }

                if (rec_first_name.equals("")) {
                    Toast.makeText(MakeNewProfile.this, "Please provide a first name", Toast.LENGTH_SHORT).show();
                } else if (rec_last_name.equals("")) {
                    Toast.makeText(MakeNewProfile.this, "Please provide a last name", Toast.LENGTH_SHORT).show();
                } else if (gender_not_provided) {
                    Toast.makeText(MakeNewProfile.this, "Please provide a gender", Toast.LENGTH_SHORT).show();
                } else if (year_dob == 0 || day_dob == 0 || month_dob == 0) {
                    Toast.makeText(MakeNewProfile.this, "Please provide a Date of Birth", Toast.LENGTH_SHORT).show();
                } else {
                    Object[] response_rc_model_combo = db.makeProfile(rec_first_name, rec_last_name, year_dob, month_dob, day_dob, rec_gender);
                    String response = (String) response_rc_model_combo[0];
                    if ("recipient added".equals(response)) {
                        RecipientModel rc_model = (RecipientModel) response_rc_model_combo[1];
                        // start the vaccines list activity
                        Intent myIntent = new Intent(MakeNewProfile.this, VaccineActivity.class);
                        myIntent.putExtra("rc_model", rc_model);
                        startActivity(myIntent);

                    } else if ("error".equals(response)) {
                        Toast.makeText(MakeNewProfile.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    } else if ("duplicate name".equals(response)) {
                        Toast.makeText(MakeNewProfile.this, "This name is already registered.", Toast.LENGTH_LONG).show();
                    } else if ("empty name".equals(response)) {
                        Toast.makeText(MakeNewProfile.this, "Please provide a valid name.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void showDatePickerDialog(View view) {
        //DialogFragment dobFragment = new DatePickerFragmentNotUsed();
        //dobFragment.show(getSupportFragmentManager(), "datePicker");
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        year_dob = year;
        month_dob = month + 1;
        day_dob = day;
        String dob_display = "Date of Birth: " + day_dob + "/" + month_dob + "/" + year_dob;
        TextView dob_textview = findViewById(R.id.dob_textview);
        dob_textview.setText(dob_display);
    }
}