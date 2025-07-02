package model;

import java.util.Arrays;

public class ProjectResponsible {
    private Integer projectResponsibleID;
    private String name;
    private String email;
    private String alterContact;
    private String period;
    private Integer linkedOrganizationId;

    public ProjectResponsible() {
    }

    public Integer getProjectResponsibleID() {
        return projectResponsibleID;
    }

    public void setProjectResponsibleID(Integer projectResponsibleID) {
        this.projectResponsibleID = projectResponsibleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlterContact() {
        return alterContact;
    }

    public void setAlterContact(String alterContact) {
        this.alterContact = alterContact;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getLinkedOrganizationId() {
        return linkedOrganizationId;
    }

    public void setLinkedOrganizationId(Integer linkedOrganizationId) {
        this.linkedOrganizationId = linkedOrganizationId;
    }

    public boolean isNull() {
        return projectResponsibleID == null &&
                name == null &&
                email == null &&
                alterContact ==  null &&
                period == null &&
                linkedOrganizationId == null;
    }

    public boolean validateEmail() {
        boolean valid = true;
        if (getEmail().isBlank()) {
            valid = false;
        } else {
            String[] emailParts = getEmail().split("@");
            if (emailParts.length != 2) {
                valid = false;
            }
        }
        return valid;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[4];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        int NAME_FLAG = 0;
        if (name == null || name.isBlank()) {
            validationFlags[NAME_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int EMAIL_FLAG = 1;
        if (!validateEmail()) {
            validationFlags[EMAIL_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int ORGANIZATION_FLAG = 2;
        if (linkedOrganizationId == null) {
            validationFlags[ORGANIZATION_FLAG] = false;
            validationFlags[lastIndex] = false;
        }

        return validationFlags;
    }

    @Override
    public String toString() {
        return name;
    }
}
