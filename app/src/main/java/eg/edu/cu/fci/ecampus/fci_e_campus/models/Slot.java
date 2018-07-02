package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;

/**
 * Created by ahmed on 6/24/2018.
 */

public class Slot {
    @SerializedName("SLOTID") private int slotId;
    @SerializedName("DAY") private String day;
    @SerializedName("STARTTIME") private String startTimeString;
    private Date startTime;
    @SerializedName("DURATION") private int duration;
    @SerializedName("GROUPID") private int groupNumber;
    @SerializedName("SLOTTYPE") private String slotType;
    @SerializedName("PLACE") private String location;
    @SerializedName("COURSECODE") private String courseCode;

    private String courseTitle;

    public Slot() {
        this.startTime = new Date();
    }

    public Slot(String day, Time startTime, int groupNumber, String slotType
            , String location, String courseCode) {
        this.day = day;
        this.startTime = startTime;
        this.groupNumber = groupNumber;
        this.slotType = slotType;
        this.location = location;
        this.courseCode = courseCode;
    }

    public Slot(String day, String startTimeString, int groupNumber, String slotType
            , String location, String courseCode) {
        this.day = day;
        this.startTimeString = startTimeString;
        this.groupNumber = groupNumber;
        this.slotType = slotType;
        this.location = location;
        this.courseCode = courseCode;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getStartTime() {
        try {
            this.startTime = DateUtils.convertSlot(this.startTimeString);
        } catch (ParseException e) {
            Log.e("Parse Exception", e.toString());
        }
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getGroupNumber() {
        return groupNumber == 0 ? "All" : "Group " + groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
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

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getTime() throws ParseException {
        return day.substring(0, 1).toUpperCase() + day.substring(1) + " " + DateUtils.convertSlot(getStartTime());
    }

    public String getType() {
        switch (slotType) {
            case "lec":
                return "Lecture";
            case "lab":
                return "Lab";
            default:
                return "Section";
        }
    }

    @Override
    public String toString() {
        return "Slot{" +
                "slotId=" + slotId +
                ", day='" + day + '\'' +
                ", startTimeString='" + startTimeString + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", groupNumber=" + groupNumber +
                ", slotType='" + slotType + '\'' +
                ", location='" + location + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                '}';
    }
}
