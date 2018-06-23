package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import java.util.Date;

/**
 * Created by ahmed on 6/23/2018.
 */

public class Task {
    private String name;
    private String description;
    private Date createdDate;
    private Date dueDate;
    private String creatorUsername;

    public Task() {
    }

    public Task(String name, String description, Date createdDate, Date dueDate, String creatorUsername) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.creatorUsername = creatorUsername;
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

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
