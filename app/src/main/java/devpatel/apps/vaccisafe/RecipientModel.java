package devpatel.apps.vaccisafe;

import java.io.Serializable;

public class RecipientModel implements Serializable {
    private final String dob;
    String first_name, last_name;
    int pk;

    public RecipientModel(String first_name, String last_name, String dob, int pk) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.pk = pk;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getName() {
        return this.first_name + " " + this.last_name;
    }

    public String getDob() {
        return dob;
    }

    public int getPk() {
        return pk;
    }

}
