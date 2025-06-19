package co.com.nequi.franchising.model.franchise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    @Test
    void testFranchiseCreation() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        assertNotNull(franchise);
        assertEquals(1L, franchise.getId());
        assertEquals("Test Franchise", franchise.getName());
    }

    @Test
    void testFranchiseEquality() {
        Franchise franchise1 = new Franchise();
        franchise1.setId(1L);
        franchise1.setName("Test Franchise");

        Franchise franchise2 = new Franchise();
        franchise2.setId(1L);
        franchise2.setName("Test Franchise");

        assertEquals(franchise1, franchise2);
    }

    @Test
    void testFranchiseInequality() {
        Franchise franchise1 = new Franchise();
        franchise1.setId(1L);
        franchise1.setName("Test Franchise");

        Franchise franchise2 = new Franchise();
        franchise2.setId(2L);
        franchise2.setName("Another Franchise");

        assertNotEquals(franchise1, franchise2);
    }

}