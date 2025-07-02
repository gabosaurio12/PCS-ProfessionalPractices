package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

public class ProjectTest {

    @Test
    public void isNullTest() {
        Project project = new Project();
        Assertions.assertTrue(project.isNull());
    }

    @Test
    public void isNullTestFail() {
        Project project = new Project();
        project.setTitle("Wikipedia");
        project.setProjectResponsibleId(1);
        project.setOpenSpots(3);
        project.setStatus("En desarrollo");
        project.setDocumentInfoPath("/path/to/document");
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo web");
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));
        project.setEndingDate(Date.valueOf(LocalDate.of(2026, 6, 15)));
        Assertions.assertFalse(project.isNull());
    }

    @Test
    public void validateDataTest() {
        Project project = new Project();
        project.setTitle("Wikipedia");
        project.setProjectResponsibleId(1);
        project.setOpenSpots(3);
        project.setStatus("En desarrollo");
        project.setDocumentInfoPath("/path/to/document");
        project.setLinkedOrganizationId(5);
        project.setCategory("Desarrollo web");
        project.setBeginningDate(Date.valueOf(LocalDate.of(2025, 5, 10)));
        project.setEndingDate(Date.valueOf(LocalDate.of(2026, 6, 15)));
        boolean[] flags = project.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestFail() {
        Project project = new Project();
        project.setTitle("");
        project.setStatus("");
        project.setCategory("");
        boolean[] flags = project.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataNotNullTestFail() {
        Project project = new Project();
        project.setTitle("");
        project.setOpenSpots(2);
        project.setStatus("");
        project.setProjectResponsibleId(1);
        project.setCategory("");
        project.setBeginningDate(new Date(0));
        project.setEndingDate(new Date(0));
        project.setLinkedOrganizationId(1);
        boolean[] flags = project.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
