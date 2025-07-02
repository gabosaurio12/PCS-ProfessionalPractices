package model;

import java.util.Arrays;

public class Student extends UniversityAffiliate {
    private String tuition;
    private Integer creditAdvance;
    private Integer academicId;
    private Integer projectId;
    private Float grade;

    public Student() {
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public Integer getCreditAdvance() {
        return creditAdvance;
    }

    public void setCreditAdvance(Integer creditAdvance) {
        this.creditAdvance = creditAdvance;
    }

    public Integer getAcademicId() {
        return academicId;
    }

    public void setAcademicId(Integer academicId) {
        this.academicId = academicId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String generateUserName() {
        String user = "";
        if (!super.getEmail().isBlank()) {
            String[] emailParts = super.getEmail().split("@");
            user = emailParts[0];
        }

        return user;
    }

    public String generatePassword() {
        return super.getUserName().concat(String.valueOf(creditAdvance));
    }

    public String getNoSpacedName() {
        String[] nameParts = super.getName().split(" ");
        String noSpacedName = "";
        for (String i : nameParts) {
            noSpacedName = noSpacedName.concat(i);
        }

        return noSpacedName;
    }

    public String generateEmail() {
        return "z".concat(tuition).concat("@estudiantes.uv.mx");
    }

    public boolean isNull() {
        return tuition == null &&
                creditAdvance == null &&
                academicId == null &&
                projectId == null &&
                grade == null &&
                super.getName() == null &&
                super.getFirstSurname() == null &&
                super.getSecondSurname() == null &&
                super.getEmail() == null &&
                super.getUserName() == null &&
                super.getPassword() == null &&
                super.getId() == null;
    }

    public boolean validateTuition() {
        return tuition != null &&!tuition.isBlank() && tuition.length() == 9;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[9];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        if (!validateTuition()) {
            validationFlags[0] = false;
            validationFlags[lastIndex] = false;
        }
        if (getName() == null || getName().isBlank()) {
            validationFlags[1] = false;
            validationFlags[lastIndex] = false;
        }
        if (getFirstSurname() == null || getFirstSurname().isBlank()) {
            validationFlags[2] = false;
            validationFlags[lastIndex] = false;
        }
        if (!validateEmail()) {
            validationFlags[3] = false;
            validationFlags[lastIndex] = false;
        }
        int minCreditAdvance = 292;
        int maxCreditAdvance = 365;
        if (creditAdvance == null || creditAdvance < minCreditAdvance || creditAdvance > maxCreditAdvance) {
            validationFlags[4] = false;
            validationFlags[lastIndex] = false;
        }
        if (academicId == null) {
            validationFlags[5] = false;
            validationFlags[lastIndex] = false;
        }
        if (getUserName() == null || getUserName().isBlank()) {
            validationFlags[6] = false;
            validationFlags[lastIndex] = false;
        }
        if (getPassword() == null || getPassword().isBlank()) {
            validationFlags[7] = false;
            validationFlags[lastIndex] = false;
        }

        return validationFlags;
    }

    @Override
    public String toString() {
        return getName().concat(" ").concat(getFirstSurname());
    }
}
