package model;

import java.sql.Date;
import java.util.Arrays;

public class Project {
    private Integer projectID;
    private String title;
    private Integer linkedOrganizationId;
    private String category;
    private Date beginningDate;
    private Date endingDate;
    private String status;
    private String documentInfoPath;
    private Integer openSpots;
    private Integer projectResponsibleId;

    public Project() {
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLinkedOrganizationId() {
        return linkedOrganizationId;
    }

    public void setLinkedOrganizationId(Integer linkedOrganizationId) {
        this.linkedOrganizationId = linkedOrganizationId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProjectResponsibleId() {
        return projectResponsibleId;
    }

    public void setProjectResponsibleId(Integer projectResponsibleId) {
        this.projectResponsibleId = projectResponsibleId;
    }

    public Integer getOpenSpots() {
        return openSpots;
    }

    public void setOpenSpots(Integer openSpots) {
        this.openSpots = openSpots;
    }

    public String getDocumentInfoPath() {
        return documentInfoPath;
    }

    public void setDocumentInfoPath(String documentInfoPath) {
        this.documentInfoPath = documentInfoPath;
    }

    public boolean isNull() {
        return projectID == null &&
               title == null &&
               linkedOrganizationId == null &&
               category == null &&
               beginningDate == null &&
               endingDate == null &&
               status == null &&
               documentInfoPath == null &&
               openSpots == null &&
               projectResponsibleId == null;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[9];
        Arrays.fill(validationFlags, true);

        if (title.isBlank()) {
            validationFlags[0] = false;
            validationFlags[8] = false;
        }
        if (openSpots == null) {
            validationFlags[1] = false;
            validationFlags[8] = false;
        }
        if (status.isBlank()) {
            validationFlags[2] = false;
            validationFlags[8] = false;
        }
        if (projectResponsibleId == null) {
            validationFlags[3] = false;
            validationFlags[8] = false;
        }
        if (category.isBlank()) {
            validationFlags[4] = false;
            validationFlags[8] = false;
        }
        if (beginningDate == null || beginningDate.equals(new Date(0))) {
            validationFlags[5] = false;
            validationFlags[8] = false;
        }
        if (endingDate == null || endingDate.equals(new Date(0))) {
            validationFlags[6] = false;
            validationFlags[8] = false;
        }
        if (linkedOrganizationId == null) {
            validationFlags[7] = false;
            validationFlags[8] = false;
        }

        return validationFlags;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", title='" + title + '\'' +
                ", linkedOrganization=" + linkedOrganizationId +
                ", category='" + category + '\'' +
                ", beginningDate=" + beginningDate +
                ", endDate=" + endingDate +
                '}';
    }
}
