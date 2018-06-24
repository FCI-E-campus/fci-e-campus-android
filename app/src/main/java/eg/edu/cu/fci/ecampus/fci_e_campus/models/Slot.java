package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import java.sql.Time;

/**
 * Created by ahmed on 6/24/2018.
 */

public class Slot {
    private String day;
    private Time startTime;
    private int groupNumber;
    private int slotType;
    private String location;
    private String courseCode;

    public Slot() {
    }

    public Slot(String day, Time startTime, int groupNumber, int slotType, String location, String courseCode) {
        this.day = day;
        this.startTime = startTime;
        this.groupNumber = groupNumber;
        this.slotType = slotType;
        this.location = location;
        this.courseCode = courseCode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public String getGroupNumber() {
        return groupNumber == 0 ? "All" : "Group " + groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getSlotType() {
        return slotType;
    }

    public void setSlotType(int slotType) {
        this.slotType = slotType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getType() {
        if (slotType == 1) {
            return "Lecture";
        } else if (slotType == 2) {
            return "Lab";
        } else {
            return "Section";
        }
    }
}
