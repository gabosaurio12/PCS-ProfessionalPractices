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

        int REPORT_FLAG = 0;
        if (reportPath == null || reportPath.isBlank()) {
            validationFlags[REPORT_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int HOURS_FLAG = 1;
        if (hours == null || hours == 0) {
            validationFlags[HOURS_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int TYPE_FLAG = 2;
        if (type == null || type.isBlank()) {
            validationFlags[TYPE_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int DATE_FLAG = 3;
        if (date == null) {
            validationFlags[DATE_FLAG] = false;
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
