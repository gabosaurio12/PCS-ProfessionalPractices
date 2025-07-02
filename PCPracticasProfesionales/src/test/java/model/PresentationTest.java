package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

public class PresentationTest {

    @Test
    public void validateDataTest() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(50.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataLowestGradeTest() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(0.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataHighestGradeTest() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(100.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataLowestGradeTestFail() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(-10.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataHighestGradeTestFail() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(150.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataNoDateTestFail() {
        Presentation presentation = new Presentation();
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(150.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataNoStudentIDTestFail() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        presentation.setPresentationGrade(150.0);
        boolean[] flags = presentation.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void isNullTest() {
        Presentation presentation = new Presentation();
        Assertions.assertTrue(presentation.isNull());
    }

    @Test
    public void isNullTestFail() {
        Presentation presentation = new Presentation();
        LocalDate date = LocalDate.of(2026, 6, 23);
        presentation.setDate(Date.valueOf(date));
        presentation.setPresentationPath("/path/to/presentation/pdf");
        int studentID = 1;
        presentation.setStudentId(studentID);
        presentation.setPresentationGrade(50.0);
        Assertions.assertFalse(presentation.isNull());
    }
}
