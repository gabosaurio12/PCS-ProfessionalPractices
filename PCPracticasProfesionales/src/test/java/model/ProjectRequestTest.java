package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProjectRequestTest {
    @Test
    public void validateDataTest() {
        ProjectRequest request = new ProjectRequest();
        request.setName("Estrella de la muerte");
        request.setStudentId(1);
        request.setDocumentPath("path/to/doc/pdf");
        boolean[] flags = request.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestFail() {
        ProjectRequest request = new ProjectRequest();
        boolean[] flags = request.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestOneNotBlankFail() {
        ProjectRequest request = new ProjectRequest();
        request.setName("Starkiller");
        boolean[] flags = request.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
