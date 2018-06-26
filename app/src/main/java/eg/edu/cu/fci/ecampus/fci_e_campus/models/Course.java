package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed on 6/25/2018.
 */

public class Course {
    @SerializedName("COURSECODE") private String code;
    @SerializedName("DEPTID") private int depID;
    @SerializedName("COURSETITLE") private String title;
    @SerializedName("DESCRIPTION") private String description;
    @SerializedName("STARTDATE") private String startDate;
    @SerializedName("ENDDATE") private String endDate;
    @SerializedName("PASSCODE") private String passCode;
    public Course(){}

    public Course(String code, int depID, String title, String description, String startDate, String endDate, String passCode) {
        this.code = code;
        this.depID = depID;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.passCode = passCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDepID() {
        return depID;
    }

    public void setDepID(int depID) {
        this.depID = depID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }
}
