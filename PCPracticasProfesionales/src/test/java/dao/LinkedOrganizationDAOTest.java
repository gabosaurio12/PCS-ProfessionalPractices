package dao;

import businesslogic.linkedorganization.LinkedOrganizationDAOImplementation;
import model.LinkedOrganization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class LinkedOrganizationDAOTest {
    static LinkedOrganizationDAOImplementation dao = new LinkedOrganizationDAOImplementation();
    private static final Logger logger = LogManager.getLogger(LinkedOrganizationDAOTest.class);
    private static final int[] organizationsID = new int[2];

    @BeforeAll
    public static void setUp() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("Marinela");
        organization.setDescription("Empresa de dulces");
        organization.setAddress("México DP: 10, CP: 29384");
        organization.setEmail("marinela@gmail.com");
        organization.setAlterContact("2287483901");

        organizationsID[0] = 0;
        if (organization.validateData()[5]) {
            try {
                organizationsID[0] = dao.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }
    }

    @Test
    public void registerLinkedOrganizationTest() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("EcoModa");
        organization.setDescription("Empresa de Ropa");
        organization.setAddress("Colombia DP: 15, CP: 88888");
        organization.setEmail("ecomoda@gmail.com");
        organization.setAlterContact("2257748621");

        organizationsID[1] = 0;
        boolean[] flags = organization.validateData();
        if (flags[flags.length - 1]) {
            try {
                organizationsID[1] = dao.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }
        Assertions.assertNotEquals(0, organizationsID[1]);
    }

    @Test
    public void registerOrganizationEmptyDataTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("");
        organization.setDescription("");
        organization.setAddress("");
        organization.setEmail("");
        organization.setAlterContact("");

        int failOrganizationID = 0;
        boolean[] flags = organization.validateData();
        if (flags[flags.length - 1]) {
            try {
                failOrganizationID = dao.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }
        Assertions.assertEquals(0, failOrganizationID);
    }

    @Test
    public void registerOrganizationDoubleAtEmailTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("McCormick");
        organization.setDescription("Especias y mayonesas");
        organization.setAddress("USA DP: 16, CP: 45384");
        organization.setEmail("mccormick@@gmail.com");
        organization.setAlterContact("2288764536");

        int failOrganizationID = 0;
        boolean[] flags = organization.validateData();
        if (flags[flags.length - 1]) {
            try {
                failOrganizationID = dao.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }
        Assertions.assertEquals(0, failOrganizationID);
    }

    @Test
    public void registerOrganizationMissingAtEmailTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setName("McCormick");
        organization.setDescription("Especias y mayonesas");
        organization.setAddress("USA DP: 16, CP: 45384");
        organization.setEmail("mccormickgmail.com");
        organization.setAlterContact("2288764536");

        int failOrganizationID = 0;
        boolean[] flags = organization.validateData();
        if (flags[flags.length - 1]) {
            try {
                failOrganizationID = dao.registerLinkedOrganization(organization);
            } catch (SQLException e) {
                logger.error("Error al registrar organización vinculada", e);
            }
        }
        Assertions.assertEquals(0, failOrganizationID);
    }

    @Test
    public void registerOrganizationTestFailException() {
        LinkedOrganization organization = new LinkedOrganization();
        organization.setDescription("Especias y mayonesas");
        organization.setAddress("USA DP: 16, CP: 45384");
        organization.setEmail("mccormick@gmail.com");
        organization.setAlterContact("2288764536");

        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.registerLinkedOrganization(organization));
    }

    @Test
    public void getLinkedOrganizationsTest() {
        List<LinkedOrganization> organizations = new ArrayList<>();
        try {
            organizations = dao.getLinkedOrganizations();
        } catch (SQLException e) {
            logger.error("Error al leer organizaciones vinculadas", e);
        }
        Assertions.assertFalse(organizations.isEmpty());
    }

    @Test
    public void getLinkedOrganizationByIDTest() {
        LinkedOrganization organization = new LinkedOrganization();
        try {
            organization = dao.getLinkedOrganizationById(organizationsID[0]);
        } catch (SQLException e) {
            logger.error("Error al leer organización vinculada", e);
        }
        Assertions.assertFalse(organization.isNull());
    }

    @Test
    public void getLinkedOrganizationByIDTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        int fakeOrganizationID = 0;
        try {
            organization = dao.getLinkedOrganizationById(fakeOrganizationID);
        } catch (SQLException e) {
            logger.error("Error al leer organización vinculada", e);
        }
        Assertions.assertTrue(organization.isNull());
    }

    @Test
    public void getLinkedOrganizationNameByIDTest() {
        String organizationName = "";
        try {
            organizationName = dao.getLinkedOrganizationNameByID(organizationsID[0]);
        } catch (SQLException e) {
            logger.error("Error al leer nombre de la organización vinculada", e);
        }
        String expectedName = "Marinela";
        Assertions.assertEquals(expectedName, organizationName);
    }
    @Test
    public void getLinkedOrganizationNameByIDTestFail() {
        String organizationName = "";
        try {
            organizationName = dao.getLinkedOrganizationNameByID(0);
        } catch (SQLException e) {
            logger.error("Error al leer nombre de la organización vinculada", e);
        }
        String expectedName = "";
        Assertions.assertEquals(expectedName, organizationName);
    }

    @Test
    public void assignLinkedOrganizationSectionTest() {
        int affectedRows = 0;
        int sectionID = 1;
        try {
            affectedRows = dao.assignLinkedOrganizationSection(organizationsID[0], sectionID);
        } catch (SQLException e) {
            logger.error("Error al asignar sección a organización vinculada", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void assignLinkedOrganizationSectionTestFail() {
        int sectionID = 0;
        Assertions.assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> dao.assignLinkedOrganizationSection(organizationsID[0], sectionID));
    }

    @Test
    public void updateLinkedOrganizationByIDTest() {
        LinkedOrganization organization = new LinkedOrganization();
        try {
            organization = dao.getLinkedOrganizationById(organizationsID[0]);
            organization.setAlterContact("2222222201");
        } catch (SQLException e) {
            logger.error("Error al leer organización vinculada", e);
        }

        int affectedRows = 0;
        if (organization.validateData()[5]) {
            try {
                affectedRows = dao.updateLinkedOrganizationByID(organization);
            } catch (SQLException e) {
                logger.error("Error al actualizar organización vinculada", e);
            }
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void updateLinkedOrganizationByIDTestFail() {
        LinkedOrganization organization = new LinkedOrganization();
        try {
            organization = dao.getLinkedOrganizationById(organizationsID[0]);
            organization.setAlterContact("");
        } catch (SQLException e) {
            logger.error("Error al leer organización vinculada", e);
        }

        int affectedRows = 0;
        if (organization.validateData()[5]) {
            try {
                affectedRows = dao.updateLinkedOrganizationByID(organization);
            } catch (SQLException e) {
                logger.error("Error al actualizar organización vinculada", e);
            }
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @Test
    public void updateLinkedOrganizationByIDNullDataTestFailException() {
        try {
            LinkedOrganization organization = dao.getLinkedOrganizationById(organizationsID[0]);
            organization.setName(null);
            organization.setAddress(null);
            organization.setEmail(null);
            organization.setAlterContact(null);
            organization.setDescription(null);

            Assertions.assertThrows(
                    SQLIntegrityConstraintViolationException.class,
                    () -> dao.updateLinkedOrganizationByID(organization));
        } catch (SQLException e) {
            logger.error("Error al leer organización vinculada", e);
        }
    }

    @Test
    public void dropLinkedOrganizationTest() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropLinkedOrganization(organizationsID[1]);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización vinculada", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropLinkedOrganizationTestFail() {
        int affectedRows = 0;
        try {
            affectedRows = dao.dropLinkedOrganization(0);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización vinculada", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        try {
            dao.dropLinkedOrganization(organizationsID[0]);
        } catch (SQLException e) {
            logger.error("Error al eliminar organización vinculada", e);
        }
    }
}
