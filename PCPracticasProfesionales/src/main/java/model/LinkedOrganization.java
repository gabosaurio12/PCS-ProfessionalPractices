package model;

import java.util.Arrays;

public class LinkedOrganization {
    private Integer linkedOrganizationID;
    private String name;
    private String description;
    private String address;
    private String email;
    private String alterContact;

    public LinkedOrganization() {
    }

    public int getLinkedOrganizationID() {
        return linkedOrganizationID;
    }

    public void setLinkedOrganizationID(int linkedOrganizationID) {
        this.linkedOrganizationID = linkedOrganizationID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String generalDescription) {
        this.description = generalDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isNull() {
        return linkedOrganizationID == null &&
                name == null &&
                description == null &&
                address == null &&
                email == null &&
                alterContact == null;
    }

    public boolean validateEmail() {
        boolean valid = true;
        if (email.isBlank()) {
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
        boolean[] validationFlags = new boolean[6];
        Arrays.fill(validationFlags, true);
        int lastIndex = validationFlags.length - 1;

        int NAME_FLAG = 0;
        if (name.isBlank()) {
            validationFlags[NAME_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int DESCRIPTION_FLAG = 1;
        if (description.isBlank()) {
            validationFlags[DESCRIPTION_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int ADDRESS_FLAG = 2;
        if (address.isBlank()) {
            validationFlags[ADDRESS_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int EMAIL_FLAG = 3;
        if (!validateEmail()) {
            validationFlags[EMAIL_FLAG] = false;
            validationFlags[lastIndex] = false;
        }
        int ALTER_CONTACT = 4;
        if (alterContact.isBlank()) {
            validationFlags[ALTER_CONTACT] = false;
            validationFlags[lastIndex] = false;
        }
        return validationFlags;
    }

    @Override
    public String toString() {
        return name;
    }
}
