package nl.politie.politiebureau.repository;

import nl.politie.politiebureau.domain.PolitiebureauEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

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
        double result = politiebureauDatabase.getMinimalLatitude();
        assertEquals(50.8406, result, 0.01);
    }

    @Test
    public void givenFullDatabase_shouldReturnMaxLat() {
        double result = politiebureauDatabase.getMaximalLatitude();
        assertEquals(53.2167, result, 0.01);
    }

    @Test
    public void givenFullDatabase_shouldReturnMinLon() {
        double result = politiebureauDatabase.getMinimalLongitude();
        assertEquals(3.5908, result, 0.01);
    }

    @Test
    public void givenFullDatabase_shouldReturnMaxLon() {
        double result = politiebureauDatabase.getMaximalLongitude();
        assertEquals(6.9742, result, 0.01);
    }
}
