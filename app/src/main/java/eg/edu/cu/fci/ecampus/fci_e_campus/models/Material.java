package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import java.util.Date;

/**
 * Created by ahmed on 6/21/2018.
 */

public class Material {
    private String name;
    private String description;
    private String link;
    private String uploaderUsername;
    private String uploaderType;
    private Date date;
    private String type;

    public Material(String name, String description, String link, String uploaderUsername, String uploaderType, Date date, String type) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.uploaderUsername = uploaderUsername;
        this.uploaderType = uploaderType;
        this.date = date;
        this.type = type;
    }

    public Material() {

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUploaderUsername() {
        return uploaderUsername;
    }

    public void setUploaderUsername(String uploaderUsername) {
        this.uploaderUsername = uploaderUsername;
    }

    public String getUploaderType() {
        return uploaderType;
    }

    public void setUploaderType(String uploaderType) {
        this.uploaderType = uploaderType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
