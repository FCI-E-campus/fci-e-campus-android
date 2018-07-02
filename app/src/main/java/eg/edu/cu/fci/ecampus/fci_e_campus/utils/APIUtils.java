package eg.edu.cu.fci.ecampus.fci_e_campus.utils;

public abstract class APIUtils {

    private APIUtils() {
    }

    ;

    public static final String getErrorMsg(int errorCode) {
        switch (errorCode) {
            case 1:
                return "This user name does not exist";
            case 2:
                return "Incorrect password";
            case 3:
                return "User is not activated";
            case 4:
                return "This username is taken, select another username";
            case 5:
                return "This is not academic mail";
            case 6:
                return "This department id already exists";
            case 7:
                return "This course id is exist";
            case 8:
                return "This course does not exist";
            case 9:
                return "This professor assigned before for this course";
            case 10:
                return "This professor is assigned before for this course";
            case 11:
                return "This course does not exist";
            case 12:
                return "This student does not exist";
            case 13:
                return "This professor does not exist";
            case 14:
                return "This announcement id does not exist";
            case 15:
                return "This task id does not exist";
            case 16:
                return "This post id does not exist";
            case 17:
                return "Incorrect activation code";
            case 18:
                return "Username is required";
            case 19:
                return "Department is required";
            case 20:
                return "Major department must be different from minor department";
            case 21:
                return "Password is required";
            case 22:
                return "First name and Last name are required";
            case 23:
                return "Email is required";
            case 24:
                return "Phone number is required";
            case 25:
                return "Date of Birth is required";
            case 26:
                return "Faculty ID is required";
            case 27:
                return "Nonexistent department";
            case 28:
                return "Invalid Pass code";
            case 29:
                return "You are already joined in this course";
            case 30:
                return "Student didn't join any courses.";
            case 31:
                return "You are not assigned to any courses.";
            case 32:
                return "You are not assigned to any courses.";
            default:
                return "Error Code: " + errorCode;
        }
    }
}
