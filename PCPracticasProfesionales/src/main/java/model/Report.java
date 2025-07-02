package model;

import java.sql.Date;
import java.util.Arrays;

public class Report {
    private Integer id;
    private String reportPath;
    private Integer hours;
    private String type;
    private Date date;
    private String period;
    private Integer studentId;

    public Report() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[5];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        if (reportPath == null || reportPath.isBlank()) {
            validationFlags[0] = false;
            validationFlags[lastIndex] = false;
        }
        if (hours == null || hours == 0) {
            validationFlags[1] = false;
            validationFlags[lastIndex] = false;
        }
        if (type == null || type.isBlank()) {
            validationFlags[2] = false;
            validationFlags[lastIndex] = false;
        }
        if (date == null) {
            validationFlags[3] = false;
            validationFlags[lastIndex] = false;
        }
        return validationFlags;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportPath='" + reportPath + '\'' +
                ", hours=" + hours +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", period='" + period + '\'' +
                ", studentId=" + studentId +
                '}';
    }
}
