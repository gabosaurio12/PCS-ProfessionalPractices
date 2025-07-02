package model;

import java.util.Arrays;

public class Academic extends UniversityAffiliate {
    private String personalNumber;
    private String role;

    public Academic() {
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String generateUserName() {
        String[] emailParts = getEmail().split("@");
        return emailParts[0];
    }

    public String generatePassword() {
        return getUserName().concat(personalNumber);
    }

    public boolean validatePersonalNumber() {
        boolean validationFlag = true;
        if (!personalNumber.isBlank()) {
            int maxPersonalNumberLength = 5;
            char[] charPersonalNumber = personalNumber.toCharArray();
            int firstNumberIndex = 0;
            for (int i = 0; i < charPersonalNumber.length; i++) {
                if (charPersonalNumber[i] != '0') {
                    firstNumberIndex = i;
                    break;
                }
            }
            String cleanPersonalNumber = "";
            for (int i = firstNumberIndex; i < charPersonalNumber.length; i++) {
                cleanPersonalNumber = cleanPersonalNumber.concat(String.valueOf(charPersonalNumber[i]));
            }
            if (cleanPersonalNumber.length() > maxPersonalNumberLength) {
                validationFlag = false;
            }
            int personalNumberInt = Integer.parseInt(cleanPersonalNumber);
            if (personalNumberInt < 1) {
                validationFlag = false;
            }
            this.setPersonalNumber(cleanPersonalNumber);
        } else {
            validationFlag = false;
        }

        return validationFlag;
    }

    public boolean isNull() {
        return personalNumber == null &&
                role == null &&
                super.getName() == null &&
                super.getFirstSurname() == null &&
                super.getSecondSurname() == null &&
                super.getEmail() == null &&
                super.getUserName() == null &&
                super.getPassword() == null &&
                super.getId() == null;
    }

    public boolean[] validateData() {
        boolean[] validationFlags = new boolean[6];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        int PERSONAL_NUMBER_FLAG = 0;
        if (!validatePersonalNumber()) {
            validationFlags[PERSONAL_NUMBER_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int NAME_FLAG = 1;
        if (getName().isBlank()) {
            validationFlags[NAME_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int FIRST_SURNAME_FLAG = 2;
        if (getFirstSurname().isBlank()) {
            validationFlags[FIRST_SURNAME_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int EMAIL_FLAG = 3;
        if (!validateEmail()) {
            validationFlags[EMAIL_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int ROLE_FLAG = 4;
        if (role.isBlank()) {
            validationFlags[ROLE_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        return validationFlags;
    }

    @Override
    public String toString() {
        return this.getName() + " " + getFirstSurname();
    }
}
