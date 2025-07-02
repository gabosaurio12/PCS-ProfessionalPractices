package dao;

import businesslogic.system.SystemDAOImplementation;
import model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemDAOTest {
    private static final SystemDAOImplementation dao = new SystemDAOImplementation();
    private static final Logger logger = LogManager.getLogger(SystemDAOTest.class);

    private static final int[] sectionsID = new int[2];

    @BeforeAll
    public static void setUp() {
        Section section = new Section();
        section.setNrc("43102");
        section.setPeriod("200001");

        sectionsID[0] = 0;
        boolean[] flags = section.validateData();
        if (flags[flags.length - 1]) {
            try {
                sectionsID[0] = dao.registerSection(section);
            } catch (SQLException e) {
                logger.error("Error al registrar sección", e);
            }
        }
    }

    @Order(1)
    @Test
    public void registerSectionTest() {
        Section section = new Section();
        section.setNrc("18174");
        section.setPeriod("200001");

        sectionsID[1] = 0;
        boolean[] flags = section.validateData();
        if (flags[flags.length - 1]) {
            try {
                sectionsID[1] = dao.registerSection(section);
            } catch (SQLException e) {
                logger.error("Error al registrar sección", e);
            }
        }

        Assertions.assertNotEquals(0, sectionsID[1]);
    }

    @Test
    public void registerSectionEmptyPeriodTestFail() {
        Section section = new Section();
        section.setNrc("43102");
        section.setPeriod("");

        sectionsID[1] = 0;
        boolean[] flags = section.validateData();
        if (flags[flags.length - 1]) {
            try {
                sectionsID[1] = dao.registerSection(section);
            } catch (SQLException e) {
                logger.error("Error al registrar sección", e);
            }
        }

        Assertions.assertEquals(0, sectionsID[1]);
    }

    @Test
    public void registerSectionEmptyNRCTestFail() {
        Section section = new Section();
        section.setNrc("");
        section.setPeriod("200001");

        sectionsID[1] = 0;
        boolean[] flags = section.validateData();
        if (flags[flags.length - 1]) {
            try {
                sectionsID[1] = dao.registerSection(section);
            } catch (SQLException e) {
                logger.error("Error al registrar sección", e);
            }
        }

        Assertions.assertEquals(0, sectionsID[1]);
    }

    @Test
    public void getSectionsTest() {
        List<Section> sections = new ArrayList<>();
        try {
            sections = dao.getSections();
        } catch (SQLException e) {
            logger.error("Error al leer secciones", e);
        }
        Assertions.assertFalse(sections.isEmpty());
    }

    @Test
    public void getSectionByIDTest() {
        Section section = new Section();
        int sectionID = sectionsID[0];
        try {
            section = dao.getSectionByID(sectionID);
        } catch (SQLException e) {
            logger.error("Error al leer sección", e);
        }
        Assertions.assertFalse(section.isNull());
    }

    @Test
    public void getCurrentSectionTest() {
        Section section = new Section();
        try {
            section = dao.getCurrentSection();
        } catch (SQLException e) {
            logger.error("Error al leer sección", e);
        }
        Assertions.assertFalse(section.isNull());
    }

    @Test
    public void getSectionByIDTestFail() {
        Section section = new Section();
        int sectionID = 0;
        try {
            section = dao.getSectionByID(sectionID);
        } catch (SQLException e) {
            logger.error("Error al leer sección", e);
        }
        Assertions.assertTrue(section.isNull());
    }

    @Test
    public void updateSectionByIDTest() {
        Section section = new Section();
        int sectionID = sectionsID[0];
        try {
            section = dao.getSectionByID(sectionID);
        } catch (SQLException e) {
            logger.error("Error al leer sección", e);
        }
        int affectedRows = 0;
        if (!section.isNull()) {
            section.setNrc("54123");
            boolean[] flags = section.validateData();
            if (flags[flags.length - 1]) {
                try {
                    affectedRows = dao.updateSectionByID(section);
                } catch (SQLException e) {
                    logger.error("Error al actualizar sección", e);
                }
            }
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Order(2)
    @Test
    public void dropSectionByIDTest() {
        int affectedRows = 0;
        int sectionID = sectionsID[1];
        try {
            affectedRows = dao.dropSectionByID(sectionID);
        } catch (SQLException e) {
            logger.error("Error al eliminar sección", e);
        }
        Assertions.assertNotEquals(0, affectedRows);
    }

    @Test
    public void dropSectionByIDTestFail() {
        int affectedRows = 0;
        int sectionID = 0;
        try {
            affectedRows = dao.dropSectionByID(sectionID);
        } catch (SQLException e) {
            logger.error("Error al eliminar sección", e);
        }
        Assertions.assertEquals(0, affectedRows);
    }

    @AfterAll
    public static void after() {
        int sectionID = sectionsID[0];
        try {
            dao.dropSectionByID(sectionID);
        } catch (SQLException e) {
            logger.error("Error al eliminar sección", e);
        }
    }
}
