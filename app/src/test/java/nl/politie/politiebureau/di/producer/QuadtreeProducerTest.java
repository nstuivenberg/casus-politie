package nl.politie.politiebureau.di.producer;

import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.shared.distance.Quadtree;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuadtreeProducerTest {

    @Mock
    private PolitiebureauDatabase politiebureauDatabase;

    @InjectMocks
    private QuadtreeProducer quadtreeProducer;

    @Before
    public void setUp() {
        // Mock het gedrag van de politiebureauDatabase
        when(politiebureauDatabase.getMinimalLatitude()).thenReturn(51.2786);
        when(politiebureauDatabase.getMinimalLongitude()).thenReturn(6.6272);
        when(politiebureauDatabase.getMaximalLatitude()).thenReturn(52.3786);
        when(politiebureauDatabase.getMaximalLongitude()).thenReturn(6.7272);
    }

    @Test
    public void testInit() {
        quadtreeProducer.init();
        Quadtree quadtree = quadtreeProducer.getQuadtree();

        assertNotNull(quadtree);

        double expectedCenterLat = 51.8286;
        double expectedCenterLon = 6.6772;
        double expectedWidth = 0.1000;
        double expectedHeight = 1.1000;

        // Controleer of de waarden van de quadtree kloppen
        assertEquals(expectedCenterLat, quadtree.getLatitudeCenter(), 0.0001);
        assertEquals(expectedCenterLon, quadtree.getLongitudeCenter(), 0.0001);
        assertEquals(expectedWidth, quadtree.getWidth(), 0.0001);
        assertEquals(expectedHeight, quadtree.getHeight(), 0.0001);
    }
}