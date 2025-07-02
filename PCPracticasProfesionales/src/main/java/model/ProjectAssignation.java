package model;

public class ProjectAssignation {
    private int projectAssignationID;
    private int projectId;
    private int studentId;
    private int coordinatorId;
    private String documentPath;

    public ProjectAssignation() {
    }

    public int getProjectAssignationID() {
        return projectAssignationID;
    }

    public void setProjectAssignationID(int projectAssignationID) {
        this.projectAssignationID = projectAssignationID;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(int coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}
