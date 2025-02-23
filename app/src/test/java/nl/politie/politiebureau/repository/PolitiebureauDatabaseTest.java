package nl.politie.politiebureau.repository;

import nl.politie.politiebureau.domain.PolitiebureauEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class PolitiebureauDatabaseTest {

    @InjectMocks
    private PolitiebureauDatabase politiebureauDatabase;


    @Test
    public void givenFullDatabase_findAllShouldReturnAll() {
        List<PolitiebureauEntity> result = politiebureauDatabase.findAll();
        assertNotNull(result);
        assertEquals(229, result.size());
    }

    @Test
    public void givenFullDatabase_shouldReturnMinLat() {
        BigDecimal result = politiebureauDatabase.getMinimalLatitude();
        assertNotNull(result);
        assertEquals(new BigDecimal("50.8406"), result);
    }

    @Test
    public void givenFullDatabase_shouldReturnMaxLat() {
        BigDecimal result = politiebureauDatabase.getMaximalLatitude();
        assertNotNull(result);
        assertEquals(new BigDecimal("53.2167"), result);
    }

    @Test
    public void givenFullDatabase_shouldReturnMinLon() {
        BigDecimal result = politiebureauDatabase.getMinimalLongitude();
        assertNotNull(result);
        assertEquals(new BigDecimal("3.5908"), result);
    }

    @Test
    public void givenFullDatabase_shouldReturnMaxLon() {
        BigDecimal result = politiebureauDatabase.getMaximalLongitude();
        assertNotNull(result);
        assertEquals(new BigDecimal("6.9742"), result);
    }
}
