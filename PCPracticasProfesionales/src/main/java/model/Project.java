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
        int lastIndex = validationFlags.length - 1;

        int TITLE_FLAG = 0;
        if (title.isBlank()) {
            validationFlags[TITLE_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int OPEN_SPOTS_FLAG = 1;
        if (openSpots == null) {
            validationFlags[OPEN_SPOTS_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int STATUS_FLAG = 2;
        if (status.isBlank()) {
            validationFlags[STATUS_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int RESPONSIBLE_FLAG = 3;
        if (projectResponsibleId == null) {
            validationFlags[RESPONSIBLE_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int CATEGORY_FLAG = 4;
        if (category.isBlank()) {
            validationFlags[CATEGORY_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int BEGINNING_DATE_FLAG = 5;
        if (beginningDate == null || beginningDate.equals(new Date(0))) {
            validationFlags[BEGINNING_DATE_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int ENDING_DATE_FLAG = 6;
        if (endingDate == null || endingDate.equals(new Date(0))) {
            validationFlags[ENDING_DATE_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int ORGANIZATION_FLAG = 7;
        if (linkedOrganizationId == null) {
            validationFlags[ORGANIZATION_FLAG] = false;
            validationFlags[lastIndex] = false;
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
