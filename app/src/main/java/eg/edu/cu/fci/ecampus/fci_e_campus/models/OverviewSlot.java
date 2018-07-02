package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;

/**
 * Created by ahmed on 7/1/2018.
 */

public class OverviewSlot {
    @SerializedName("name") private String name;
    @SerializedName("duetime") private String timeString;
    private Date time;
    @SerializedName("place") private String place;

    public OverviewSlot() {
        time = new Date();
    }

    public OverviewSlot(String name, String timeString, Date time, String place) {
        this.name = name;
        this.timeString = timeString;
        this.time = time;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public Date getTime() {
        try {
            this.time = DateUtils.convertSlot(this.timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getConvertedTime() throws ParseException {
        return DateUtils.convertSlot(getTime());
    }
}
