package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class AcademicTest {
    @Test
    public void generateUserNameTest() {
        Academic academic = new Academic();
        academic.setEmail("academic@gmail.com");
        String username = academic.generateUserName();
        String expectedUsername = "academic";
        Assertions.assertEquals(expectedUsername, username);
    }

    @Test
    public void generatePasswordTest() {
        Academic academic = new Academic();
        academic.setPersonalNumber("12345");
        academic.setEmail("academic@gmail.com");
        academic.setUserName(academic.generateUserName());
        String password = academic.generatePassword();
        String expectedPassword = "academic12345";
        Assertions.assertEquals(expectedPassword, password);
    }

    @Test
    public void validateEmailGmailTest() {
        String email = "example@gmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailHotmailTest() {
        String email = "example@hotmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailUVTest() {
        String email = "example@uv.mx";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailWithDotTest() {
        String email = "example.email@gmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailMultipleDotsTest() {
        String email = "example.email.test@gmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailNoAtTestFail() {
        String email = "examplegmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailDoubleAtTestFail() {
        String email = "example@@gmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailNoAtWithDotTestFail() {
        String email = "example.gmail.com";
        Academic academic = new Academic();
        academic.setEmail(email);
        boolean validateEmail = academic.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validatePersonalNumberTest() {
        Random random = new Random();
        int personalNumberInt = random.nextInt(90000) + 10000;
        String personalNumber = String.valueOf(personalNumberInt);
        Academic academic = new Academic();
        academic.setPersonalNumber(personalNumber);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberMinTest() {
        String personalNumber = "1";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalNumber);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberMaxTest() {
        String personalNumber = "99999";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalNumber);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberMinWithZeroTest() {
        String personalNumber = "01";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalNumber);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberMinWithZerosTest() {
        String personalNumber = "00001";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalNumber);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberMinWithMultipleZerosTest() {
        String personalNumber = "0001";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalNumber);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberLeftZerosTest() {
        String personalName = "000001";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalName);
        Assertions.assertTrue(academic.validatePersonalNumber());
        System.out.println(academic.getPersonalNumber());
    }

    @Test
    public void validatePersonalNumberLeftZerosLongTest() {
        String personalName = "0000084513";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalName);
        Assertions.assertTrue(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberLeftZerosLongTestFail() {
        String personalName = "845135400";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalName);
        Assertions.assertFalse(academic.validatePersonalNumber());
    }

    @Test
    public void validatePersonalNumberZeroFalseTest() {
        String personalName = "0";
        Academic academic = new Academic();
        academic.setPersonalNumber(personalName);
        Assertions.assertFalse(academic.validatePersonalNumber());
    }

    @Test
    public void academicIsNullTest() {
        Academic academic = new Academic();
        Assertions.assertTrue(academic.isNull());
    }

    @Test
    public void academicIsNullTestFail() {
        Academic academic = new Academic();
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revoover@uv.mx");
        academic.setPersonalNumber("99999");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        Assertions.assertFalse(academic.isNull());
    }

    @Test
    public void validateDataTest() {
        Academic academic = new Academic();
        academic.setName("Juan Carlos");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("revoover@uv.mx");
        academic.setPersonalNumber("99999");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        boolean[] flags = academic.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestFail() {
        Academic academic = new Academic();
        academic.setName("");
        academic.setFirstSurname("Perez");
        academic.setSecondSurname("Arriaga");
        academic.setEmail("");
        academic.setPersonalNumber("");
        academic.setUserName(academic.generateUserName());
        academic.setPassword(academic.generatePassword());
        academic.setRole("ACADÉMICO");

        boolean[] flags = academic.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
