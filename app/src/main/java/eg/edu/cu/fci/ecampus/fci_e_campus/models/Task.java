package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class Task {
    @SerializedName("task_name") private String name;
    private String description;
    @SerializedName("date_created") private Date createdDate;
    @SerializedName("due_date") private Date dueDate;
    private String weight;
    @SerializedName("creator_username") private String creatorUsername;
    @SerializedName("creator_type") private String creatorType;

    private String courseCode;

    public Task() {
    }

    public Task(String name, String description, Date createdDate, Date dueDate
            , String weight, String creatorUsername, String creatorType) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.weight = weight;
        this.creatorUsername = creatorUsername;
        this.creatorType = creatorType;
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
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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
