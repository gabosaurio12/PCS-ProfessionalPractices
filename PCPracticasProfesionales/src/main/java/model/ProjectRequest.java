package model;

import java.util.Arrays;

public class ProjectRequest {
    private Integer id;
    private String name;
    private Integer studentId;
    private String documentPath;

    public ProjectRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[3];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        if (name == null || name.isBlank()) {
            validationFlags[0] = false;
            validationFlags[lastIndex] = false;
        }
        if (documentPath == null || documentPath.isBlank()) {
            validationFlags[1] = false;
            validationFlags[lastIndex] = false;
        }
        return validationFlags;
    }
}
