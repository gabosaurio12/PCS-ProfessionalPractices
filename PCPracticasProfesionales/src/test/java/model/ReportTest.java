package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

public class ReportTest {
    @Test
    public void validateDataTest() {
        Report report = new Report();
        report.setReportPath("/path/to/report/pdf");
        report.setHours(100);
        report.setType("80 horas");
        report.setDate(Date.valueOf(LocalDate.of(2025, 7, 1)));
        report.setPeriod("202551");
        report.setStudentId(1);
        boolean[] flags = report.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataNullTestFail() {
        Report report = new Report();
        boolean[] flags = report.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }

    @Test
    public void validateDataBlankTestFail() {
        Report report = new Report();
        report.setReportPath("");
        report.setHours(0);
        report.setType("");
        report.setDate(new Date(0));
        report.setPeriod("");
        report.setStudentId(0);
        boolean[] flags = report.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
