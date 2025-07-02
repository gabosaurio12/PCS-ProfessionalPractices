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
        int lastIndex = validationFlags.length - 1;

        int NRC_FLAG = 0;
        if (nrc.isBlank()) {
            validationFlags[NRC_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int PERIOD_FLAG = 1;
        if (period.isBlank()) {
            validationFlags[PERIOD_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        return validationFlags;
    }

    @Override
    public String toString() {
        return period;
    }
}
