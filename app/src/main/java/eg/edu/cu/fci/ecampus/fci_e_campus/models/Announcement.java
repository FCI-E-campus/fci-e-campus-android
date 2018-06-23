package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Announcement {

    @SerializedName("ANNOUNCEMENTID") private String id;
    @SerializedName("ADMINUSERNAME") private String adminUserName;
    @SerializedName("ANNOUNCEMENTTITLE") private String title;
    @SerializedName("ANNOUNCEMENTBODY") private String body;
    @SerializedName("DATEPUBLISHED") private Date publishDate;

    public Announcement() {
    }

    public Announcement(String id, String adminUserName, String title, String body, Date publishDate) {
        this.id = id;
        this.adminUserName = adminUserName;
        this.title = title;
        this.body = body;
        this.publishDate = publishDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id='" + id + '\'' +
                ", adminUserName='" + adminUserName + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", publishDate=" + publishDate +
                '}';
    }
}
