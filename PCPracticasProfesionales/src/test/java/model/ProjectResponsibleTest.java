package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProjectResponsibleTest {
    @Test
    public void isNullTest() {
        ProjectResponsible responsible = new ProjectResponsible();
        Assertions.assertTrue(responsible.isNull());
    }

    @Test
    public void isNullTestFail() {
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setName("Juana de Arco");
        Assertions.assertFalse(responsible.isNull());
    }

    @Test
    public void validateEmailGmailTest() {
        String email = "example@gmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailHotmailTest() {
        String email = "example@hotmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailUVTest() {
        String email = "example@uv.mx";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailWithDotTest() {
        String email = "example.email@gmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailMultipleDotsTest() {
        String email = "example.email.test@gmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailNoAtTestFail() {
        String email = "examplegmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailDoubleAtTestFail() {
        String email = "example@@gmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailNoAtWithDotTestFail() {
        String email = "example.gmail.com";
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setEmail(email);
        boolean validateEmail = responsible.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateDataTest() {
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setName("Mario Bros");
        responsible.setEmail("itsmemario@gmail.com");
        responsible.setAlterContact("2287753746");
        responsible.setPeriod("202551");
        responsible.setLinkedOrganizationId(1);
        boolean[] flags = responsible.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestFail() {
        ProjectResponsible responsible = new ProjectResponsible();

        responsible.setEmail("");
        responsible.setAlterContact("2287753746");
        responsible.setPeriod("202551");
        boolean[] flags = responsible.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
