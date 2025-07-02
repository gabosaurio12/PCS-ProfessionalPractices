package dao;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import businesslogic.projectresponsible.ProjectResponsibleDAOImplementation;
import model.LinkedOrganization;
import model.ProjectResponsible;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectResponsibleDAOTest {
    private static final Logger logger = LogManager.getLogger(ProjectResponsibleDAOTest.class);
    private static final ProjectResponsibleDAOImplementation dao = new ProjectResponsibleDAOImplementation();

    private static int organizationID;
    private static final int[] responsiblesID = new int[2];

    @BeforeAll
    public static void setUp() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("Marinela");
        organization.setDescription("Empresa de dulces");
        organization.setAddress("México DP: 10, CP: 29384");
        organization.setEmail("marinela@gmail.com");
        organization.setAlterContact("2287483901");

        organizationID = 0;
        boolean[] organizationFlags = organization.validateData();
        if (organizationFlags[organizationFlags.length - 1]) {
            try {
                LinkedOrganizationDAOImplementation organizationDAO = new LinkedOrganizationDAOImplementation();
                organizationID = organizationDAO.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }

        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setName("María Susana Artiaga");
        responsible.setEmail("marysu@gmail.com");
        responsible.setAlterContact("2254555558");
        responsible.setPeriod("202551");
        responsible.setLinkedOrganizationId(organizationID);
        responsiblesID[0] = 0;
        boolean[] responsibleFlags = responsible.validateData();
        if (responsibleFlags[responsibleFlags.length - 1]) {
            try {
                responsiblesID[0] = dao.registerProjectResponsible(responsible);
            } catch (SQLException e) {
                logger.error("Error al registrar responsable de proyecto: ", e);
            }
        }
    }

    @Order(1)
    @Test
    public void registerProjectResponsibleTest() {
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setName("Patricia Fernández");
        responsible.setEmail("paty@gmail.com");
        responsible.setAlterContact("2258425558");
        responsible.setPeriod("202551");
        responsible.setLinkedOrganizationId(organizationID);
        responsiblesID[1] = 0;
        boolean[] flags = responsible.validateData();
        if (flags[flags.length - 1]) {
            try {
                responsiblesID[1] = dao.registerProjectResponsible(responsible);
            } catch (SQLException e) {
                logger.error("Error al registrar responsable de proyecto: ", e);
            }
        }
        Assertions.assertNotEquals(0, responsiblesID[1]);
    }

    @Test
    public void registerProjectResponsibleTestFail() {
        ProjectResponsible responsible = new ProjectResponsible();
        responsible.setName("Patricia Fernández");
        responsible.setEmail("paty@@gmail.com");
        responsible.setAlterContact("");
        responsible.setPeriod("");
        responsible.setLinkedOrganizationId(organizationID);
        responsiblesID[1] = 0;
        boolean[] flags = responsible.validateData();
        if (flags[flags.length - 1]) {
            try {
                responsiblesID[1] = dao.registerProjectResponsible(responsible);
            } catch (SQLException e) {
                logger.error("Error al registrar responsable de proyecto: ", e);
            }
        }
        Assertions.assertEquals(0, responsiblesID[1]);
    }

    @Test
    public void getProjectResponsiblesTest() {
        String period = "202551";
        List<ProjectResponsible> responsibles = new ArrayList<>();
        try {
            responsibles = dao.getProjectResponsibles(period);
        } catch (SQLException e) {
            logger.error("Error al leer responsables", e);
        }
        Assertions.assertFalse(responsibles.isEmpty());
    }

    @Test
    public void getProjectResponsiblesTestFail() {
        String period = "300051";
        List<ProjectResponsible> responsibles = new ArrayList<>();
        try {
            responsibles = dao.getProjectResponsibles(period);
        } catch (SQLException e) {
            logger.error("Error al leer responsables", e);
        }
        Assertions.assertTrue(responsibles.isEmpty());
    }

    @Test
    public void getProjectResponsibleByIDTest() {
        ProjectResponsible responsible = new ProjectResponsible();
        int responsibleID = responsiblesID[0];
        try {
            responsible = dao.getProjectResponsibleByID(responsibleID);
        } catch (SQLException e) {
            logger.error("Error al leer responsable", e);
        }
        Assertions.assertFalse(responsible.isNull());
    }

    @Test
    public void getProjectResponsibleByIDTestFail() {
        ProjectResponsible responsible = new ProjectResponsible();
        int responsibleID = 0;
        try {
            responsible = dao.getProjectResponsibleByID(responsibleID);
        } catch (SQLException e) {
            logger.error("Error al leer responsable", e);
        }
        Assertions.assertTrue(responsible.isNull());
    }

    @Test
    public void updateProjectResponsibleTest() {
        ProjectResponsible responsible = new ProjectResponsible();
        try {
            responsible = dao.getProjectResponsibleByID(responsiblesID[0]);
        } catch (SQLException e) {
            logger.error("Error al leer responsable de proyecto", e);
        }
        int affectedRows = 0;
        if (!responsible.isNull()) {
            responsible.setEmail("sussy@gmail.com");
            boolean[] flags = responsible.validateData();
            if (flags[flags.length - 1]) {
                try {
                    affectedRows = dao.updateProjectResponsible(responsible);
                } catch (SQLException e) {
                    logger.error("Error al actualizar responsable de proyecto", e);
                }
            }
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateProjectResponsibleTestFail() {
        ProjectResponsible responsible = new ProjectResponsible();
        try {
            responsible = dao.getProjectResponsibleByID(responsiblesID[1]);
        } catch (SQLException e) {
            logger.error("Error al leer responsable de proyecto", e);
        }
        int affectedRows = 0;
        if (!responsible.isNull()) {
            responsible.setName("");
            responsible.setEmail("");
            boolean[] flags = responsible.validateData();
            if (flags[flags.length - 1]) {
                try {
                    affectedRows = dao.updateProjectResponsible(responsible);
                } catch (SQLException e) {
                    logger.error("Error al actualizar responsable de proyecto", e);
                }
            }
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Order(2)
    @Test
    public void dropProjectResponsibleByIDTest() {
        int affectedRows = 0;
        int responsibleID = responsiblesID[1];
        try {
            affectedRows = dao.dropProjectResponsibleByID(responsibleID);
        } catch (SQLException e) {
            logger.error("Error al eliminar responsable de proyecto", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropProjectResponsibleByIDTestFail() {
        int affectedRows = 0;
        int fakeResponsibleID = 0;
        try {
            affectedRows = dao.dropProjectResponsibleByID(fakeResponsibleID);
        } catch (SQLException e) {
            logger.error("Error al eliminar responsable de proyecto", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        try {
            int responsibleID = responsiblesID[0];
            dao.dropProjectResponsibleByID(responsibleID);
            LinkedOrganizationDAOImplementation organizationDAO = new LinkedOrganizationDAOImplementation();
            organizationDAO.dropLinkedOrganization(organizationID);
        } catch (SQLException e) {
            logger.error("Error al eliminar responsable de proyecto", e);
        }
    }

}
