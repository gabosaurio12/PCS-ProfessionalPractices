package model;

public class StudentProgress {
    private Integer hoursValidated;
    private Integer remainingHours;
    private String tuition;

    public StudentProgress() {
    }

    public Integer getHoursValidated() {
        return hoursValidated;
    }

    public void setHoursValidated(Integer hoursValidated) {
        this.hoursValidated = hoursValidated;
    }

    public Integer getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(Integer remainingHours) {
        this.remainingHours = remainingHours;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public boolean isNull() {
        return hoursValidated ==  null &&
                remainingHours == null &&
                tuition == null;
    }
}
