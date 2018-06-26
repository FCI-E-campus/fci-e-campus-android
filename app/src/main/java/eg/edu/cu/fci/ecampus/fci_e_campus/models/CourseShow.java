package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed on 6/25/2018.
 */

public class CourseShow {
    @SerializedName("COURSECODE")
    private String code;
    @SerializedName("COURSETITLE")
    private String title;

    public CourseShow() {
    }

    public CourseShow(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
