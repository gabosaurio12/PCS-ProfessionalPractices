package model;

import java.util.Arrays;

public class Section {
    private Integer sectionID;
    private String nrc;
    private String period;

    public Section() {
    }

    public Integer getSectionID() {
        return sectionID;
    }

    public void setSectionID(Integer sectionID) {
        this.sectionID = sectionID;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isNull() {
        return sectionID == null &&
                nrc == null &&
                period == null;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[3];
        Arrays.fill(validationFlags, true);

        if (nrc.isBlank()) {
            validationFlags[0] = false;
            validationFlags[2] = false;
        }
        if (period.isBlank()) {
            validationFlags[1] = false;
            validationFlags[2] = false;
        }
        return validationFlags;
    }

    @Override
    public String toString() {
        return period;
    }
}
