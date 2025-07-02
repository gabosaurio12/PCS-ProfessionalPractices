package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinkedOrganizationTest {

    @Test
    public void isNullTest() {
        LinkedOrganization organization = new LinkedOrganization();
        Assertions.assertTrue(organization.isNull());
    }

    @Test
    public void isNullTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("Marinela");
        organization.setDescription("Empresa de dulces");
        organization.setAddress("México DP: 10, CP: 29384");
        organization.setEmail("marinela@gmail.com");
        organization.setAlterContact("2287483901");
        Assertions.assertFalse(organization.isNull());
    }

    @Test
    public void validateEmailGmailTest() {
        String email = "example@gmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailHotmailTest() {
        String email = "example@hotmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailUVTest() {
        String email = "example@uv.mx";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailWithDotTest() {
        String email = "example.email@gmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailMultipleDotsTest() {
        String email = "example.email.test@gmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailNoAtTestFail() {
        String email = "examplegmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailDoubleAtTestFail() {
        String email = "example@@gmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailNoAtWithDotTestFail() {
        String email = "example.gmail.com";
        LinkedOrganization organization = new LinkedOrganization();
        organization.setEmail(email);
        boolean validateEmail = organization.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateDataTest() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("Marinela");
        organization.setDescription("Empresa de dulces");
        organization.setAddress("México DP: 10, CP: 29384");
        organization.setEmail("marinela@gmail.com");
        organization.setAlterContact("2287483901");

        boolean[] flags = organization.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("Marinela");
        organization.setDescription("Empresa de dulces");
        organization.setAddress("México DP: 10, CP: 29384");
        organization.setEmail("");
        organization.setAlterContact("2287483901");

        boolean[] flags = organization.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
