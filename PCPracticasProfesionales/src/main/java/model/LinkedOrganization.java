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


        if (name.isBlank()) {
            validationFlags[0] = false;
            validationFlags[lastIndex] = false;
        }
        if (description.isBlank()) {
            validationFlags[1] = false;
            validationFlags[lastIndex] = false;
        }
        if (address.isBlank()) {
            validationFlags[2] = false;
            validationFlags[lastIndex] = false;
        }
        if (!validateEmail()) {
            validationFlags[3] = false;
            validationFlags[lastIndex] = false;
        }
        if (alterContact.isBlank()) {
            validationFlags[4] = false;
            validationFlags[lastIndex] = false;
        }
        return validationFlags;
    }

    @Override
    public String toString() {
        return name;
    }
}
