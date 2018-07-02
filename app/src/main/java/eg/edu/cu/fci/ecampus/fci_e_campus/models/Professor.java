package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

public class Professor extends Teacher {
    @SerializedName("PROFUSERNAME") private String username;

    public Professor(){}

    public Professor(String username, String firstName, String lastName) {
        super(firstName, lastName);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
