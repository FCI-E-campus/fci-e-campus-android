package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

public class Teacher extends User {
    @SerializedName("FIRSTNAME") private String firstName;
    @SerializedName("LASTNAME") private String lastName;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
