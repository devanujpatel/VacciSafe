package com.example.vaccisafe;

public class ReminderModel {
    String vaccine_name, rec_fname, rec_lname;
    int vaccine_pk, rec_pk;

    public ReminderModel(String vaccine_name, String rec_fname, String rec_lname, int vaccine_pk, int rec_pk) {
        this.vaccine_name = vaccine_name;
        this.rec_fname = rec_fname;
        this.rec_lname = rec_lname;
        this.vaccine_pk = vaccine_pk;
        this.rec_pk = rec_pk;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public int getRec_pk() {
        return rec_pk;
    }

}
