package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class Presentation {
    private Integer presentationID;
    private Double presentationGrade;
    private String presentationPath;
    private Date date;
    private Integer studentId;

    public Presentation() {
    }

    public Integer getPresentationID() {
        return presentationID;
    }

    public void setPresentationID(Integer presentationID) {
        this.presentationID = presentationID;
    }

    public Double getPresentationGrade() {
        return presentationGrade;
    }

    public void setPresentationGrade(Double presentationGrade) {
        this.presentationGrade = presentationGrade;
    }

    public String getPresentationPath() {
        return presentationPath;
    }

    public void setPresentationPath(String presentationPath) {
        this.presentationPath = presentationPath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[4];
        Arrays.fill(validationFlags, true);

        if (date == null) {
            validationFlags[0] = false;
            validationFlags[3] = false;
        }
        if (studentId == null) {
            validationFlags[1] = false;
            validationFlags[3] = false;
        }
        if (presentationGrade != null) {
            if (presentationGrade < 0 || presentationGrade > 100) {
                validationFlags[2] = false;
                validationFlags[3] = false;
            }
        }

        return validationFlags;
    }

    public boolean isNull() {
        return presentationID == null &&
                presentationGrade == null &&
                presentationPath == null &&
                date == null &&
                studentId == null;
    }
}
