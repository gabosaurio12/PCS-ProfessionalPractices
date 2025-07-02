package model;

import java.util.Arrays;

public class UniversityAffiliate {
    private Integer id;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String userName;
    private String password;


    public UniversityAffiliate() {
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

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNull() {
        return id == null &
                name == null &&
                firstSurname == null &&
                secondSurname == null &&
                email == null &&
                userName == null &&
                password == null;
    }

    public boolean validateEmail() {
        boolean valid = true;
        if (email == null || email.isBlank()) {
            valid = false;
        } else {
            String[] emailParts = email.split("@");
            if (emailParts.length != 2) {
                valid = false;
            }
        }
        return valid;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[3];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        if (!validateEmail()) {
            validationFlags[0] = false;
            validationFlags[lastIndex] = false;
        }
        if (userName == null || userName.isBlank()) {
            validationFlags[1] = false;
            validationFlags[lastIndex] = false;
        }

        return validationFlags;
    }
}
