package eg.edu.cu.fci.ecampus.fci_e_campus.utils;

public abstract class APIUtils {

    private APIUtils(){};

    public static final String getErrorMsg(int errorCode){
        switch (errorCode) {
            // TODO add the remaining error codes
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
            default:
                return "Error Code: " + errorCode;
        }
    }
}
