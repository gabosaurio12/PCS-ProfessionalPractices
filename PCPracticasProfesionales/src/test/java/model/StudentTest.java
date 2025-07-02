package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StudentTest {
    @Test
    public void generateUserNameTest() {
        Student student = new Student();
        student.setEmail("student@gmail.com");
        String username = student.generateUserName();
        String expectedUsername = "student";
        Assertions.assertEquals(expectedUsername, username);
    }

    @Test
    public void generatePasswordTest() {
        Student student = new Student();
        student.setTuition("S25000001");
        student.setEmail("student@gmail.com");
        student.setCreditAdvance(300);
        student.setAcademicId(1);
        student.setUserName(student.generateUserName());
        String password = student.generatePassword();
        String expectedPassword = "student300";
        Assertions.assertEquals(expectedPassword, password);
    }

    @Test
    public void generateEmailTest() {
        Student student = new Student();
        student.setTuition("S23014038");
        String email = student.generateEmail();
        String expectedEmail = "zS23014038@estudiantes.uv.mx";
        Assertions.assertEquals(expectedEmail, email);
    }

    @Test
    public void generateEmailEmptyTuitionTest() {
        Student student = new Student();
        student.setTuition("");
        String email = student.generateEmail();
        String expectedEmail = "z@estudiantes.uv.mx";
        Assertions.assertEquals(expectedEmail, email);
    }

    @Test
    public void validateEmailGmailTest() {
        String email = "example@gmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailHotmailTest() {
        String email = "example@hotmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailUVTest() {
        String email = "example@uv.mx";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailWithDotTest() {
        String email = "example.email@gmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailMultipleDotsTest() {
        String email = "example.email.test@gmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertTrue(validateEmail);
    }

    @Test
    public void validateEmailNoAtTestFail() {
        String email = "examplegmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailDoubleAtTestFail() {
        String email = "example@@gmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateEmailNoAtWithDotTestFail() {
        String email = "example.gmail.com";
        Student student = new Student();
        student.setEmail(email);
        boolean validateEmail = student.validateEmail();
        Assertions.assertFalse(validateEmail);
    }

    @Test
    public void validateTuitionTest() {
        String tuition = "S25000000";
        Student student = new Student();
        student.setTuition(tuition);
        Assertions.assertTrue(student.validateTuition());
    }

    @Test
    public void StudentIsNullTest() {
        Student student = new Student();
        Assertions.assertTrue(student.isNull());
    }

    @Test
    public void StudentIsNullTestFail() {
        Student student = new Student();
        student.setTuition("S20000000");
        student.setName("Juan Carlos");
        student.setFirstSurname("Perez");
        student.setSecondSurname("Arriaga");
        student.setEmail(student.generateEmail());
        student.setCreditAdvance(300);
        student.setAcademicId(1);
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());

        Assertions.assertFalse(student.isNull());
    }

    @Test
    public void validateDataTest() {
        Student student = new Student();
        student.setTuition("S20000000");
        student.setName("Juan Carlos");
        student.setFirstSurname("Perez");
        student.setSecondSurname("Arriaga");
        student.setEmail(student.generateEmail());
        student.setCreditAdvance(300);
        student.setAcademicId(1);
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());

        boolean[] flags = student.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataBlankTestFail() {
        Student student = new Student();
        student.setTuition("");
        student.setName("Juan");
        student.setFirstSurname("");
        student.setSecondSurname("Arriaga");
        student.setEmail(student.generateEmail());
        student.setCreditAdvance(300);
        student.setAcademicId(1);
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());

        boolean[] flags = student.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataShortCreditAdvanceTestFail() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setCreditAdvance(270);
        student.setGrade(10F);
        student.setAcademicId(13);

        Assertions.assertFalse(student.validateData()[8]);
    }

    @Test
    public void validateDataBigCreditAdvanceTestFail() {
        Student student = new Student();
        student.setTuition("S23014038");
        student.setName("Gabriel Antonio");
        student.setFirstSurname("González");
        student.setSecondSurname("López");
        student.setEmail(student.generateEmail());
        student.setUserName(student.generateUserName());
        student.setPassword(student.generatePassword());
        student.setCreditAdvance(366);
        student.setGrade(10F);
        student.setAcademicId(13);

        Assertions.assertFalse(student.validateData()[8]);
    }

    @Test
    public void validateDataNullTestFail() {
        Student student = new Student();

        boolean[] flags = student.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
