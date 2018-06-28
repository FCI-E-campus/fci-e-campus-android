package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;


public class Task {
    @SerializedName("task_name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("date_created")
    private String createdDateString;
    @SerializedName("due_date")
    private String dueDateString;
    private Date createdDate;
    private Date dueDate;
    @SerializedName("weight")
    private String weight;
    @SerializedName("creator_username")
    private String creatorUsername;
    @SerializedName("creator_type")
    private String creatorType;
    private String courseCode;

    public Task() {
        this.createdDate = new Date();
        this.dueDate = new Date();
    }

    public Task(String name, String description, String createdDateString, String dueDateString
            , String weight, String creatorUsername, String creatorType) {
        this.name = name;
        this.description = description;
        this.createdDateString = createdDateString;
        this.dueDateString = dueDateString;
        this.weight = weight;
        this.creatorUsername = creatorUsername;
        this.creatorType = creatorType;
        try {
            this.createdDate = DateUtils.convert(this.createdDateString);
            this.dueDate = DateUtils.convert(this.dueDateString);
        } catch (ParseException e) {
            Log.e("Parse Exception", e.toString());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        try {
            this.createdDate = DateUtils.convert(this.createdDateString);
        } catch (ParseException e) {
            Log.e("Parse Exception", e.toString());
        }
        return createdDate;
    }

    public void setCreatedDateString(String createdDateString) {
        this.createdDateString = createdDateString;
    }

    public Date getDueDate() {
        try {
            this.dueDate = DateUtils.convert(this.dueDateString);
        } catch (ParseException e) {
            Log.e("Parse Exception", e.toString());
        }
        return dueDate;
    }

    public void setDueDateString(String dueDateString) {
        this.dueDateString = dueDateString;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
