package devpatel.apps.vaccisafe;

import android.content.Context;

public class VaccineModel {
    int id;
    String name;
    String displayName;
    String taken_at, age;
    Context context;
    String dueOn;
    String details;

    public Context getContext() {
        return context;
    }

    public VaccineModel(String name, String taken_at, String age, int id, Context context, String dueOn, String details) {
        this.name = name;
        this.id = id;
        this.taken_at = taken_at;
        this.age = age;
        this.context = context;
        this.dueOn = dueOn;
        this.details = details;
    }

    public String getAge() {
        return age;
    }

    public String getDetails() {
        return details;
    }

    public String getDueOn() {
        return dueOn;
    }

    public void setDueOn(String dueOn) {
        this.dueOn = dueOn;
    }

    public String getDisplayName() {
        this.displayName = this.name + " (" + this.age + ")";
        return displayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaken_at(String taken_at) {
        DataBaseHelper db = new DataBaseHelper(this.context);
        db.setTakenAt(taken_at, String.valueOf(this.id));
        this.taken_at = taken_at;
    }

    public Boolean getTaken() {
        return this.taken_at != null;
    }
}
