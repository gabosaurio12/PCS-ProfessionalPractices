package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UniversityAffiliateTest {
    @Test
    public void isNullTest() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        Assertions.assertTrue(affiliate.isNull());
    }

    @Test
    public void isNullTestFail() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        affiliate.setName("Luke");
        affiliate.setFirstSurname("Skywalker");
        affiliate.setSecondSurname("Amidala");
        affiliate.setEmail("luke@gmail.com");
        affiliate.setUserName("luke");
        affiliate.setPassword("skywito");
        Assertions.assertFalse(affiliate.isNull());
    }

    @Test
    public void validateDataTest() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        affiliate.setName("Luke");
        affiliate.setFirstSurname("Skywalker");
        affiliate.setSecondSurname("Amidala");
        affiliate.setEmail("luke@gmail.com");
        affiliate.setUserName("luke");
        affiliate.setPassword("skywito");
        boolean[] flags = affiliate.validateData();
        Assertions.assertTrue(flags[flags.length - 1]);
    }

    @Test
    public void validateDataTestFail() {
        UniversityAffiliate affiliate = new UniversityAffiliate();
        boolean[] flags = affiliate.validateData();
        Assertions.assertFalse(flags[flags.length - 1]);
    }
}
