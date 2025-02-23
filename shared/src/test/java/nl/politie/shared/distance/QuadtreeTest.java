package nl.politie.shared.distance;

import static org.junit.Assert.*;

import nl.politie.shared.distance.config.Constants;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class QuadtreeTest {

    private Quadtree quadtree;

    @Before
    public void setUp() {
        quadtree = new Quadtree(0.0, 0.0, 10.0, 10.0);
    }

    @Test
    public void givenCoordinateWithinBounds_shouldBeAdded() {
        assertTrue(quadtree.insert(5.0, 5.0));
    }

    @Test
    public void givenCoordinateOutOfBound_shouldNotBeAdded() {
        assertFalse(quadtree.insert(20.0, 20.0));
    }


    @Test
    public void givenNoCoordinates_shouldReturnEmpty() {
        // Test voor een zoekopdracht zonder punten in de Quadtree
        List<Map<String, Double>> result = quadtree.queryRange(0.0, 0.0, 1.0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenFourCoordinatesWithinBoundAndRange_shouldBeReturned() {
        // Voeg een paar punten toe en test de queryRange functie
        assertTrue(quadtree.insert(0.0, 0.0));
        assertTrue(quadtree.insert(0.01, 0.0));
        assertTrue(quadtree.insert(0.02, 0.0));
        assertTrue(quadtree.insert(0.03, 0.0));


        List<Map<String, Double>> result = quadtree.queryRange(0.0, 0.0, 5.0);
        assertEquals(4, result.size()); // Alle 4 punten moeten gevonden worden
    }

    @Test
    public void givenTwoCoordinatesWithinRange_shouldBeReturned() {
        // Voeg een paar punten toe en test of de radius werkt
        quadtree.insert(1.0, 1.0);
        quadtree.insert(5.0, 5.0);

        List<Map<String, Double>> result = quadtree.queryRange(0.0, 0.0, Constants.EARTH_RADIUS);
        assertEquals(2, result.size()); // Beide punten moeten gevonden worden
    }

    @Test
    public void givenCoordinateOutsideBounds_containsShouldReturnFalse() {
        // Test of de Quadtree correct controleert of een punt binnen de grenzen ligt
        assertTrue(quadtree.contains(5.0, 5.0));
        assertFalse(quadtree.contains(20.0, 20.0));
    }

    @Test
    public void givenAFifthCoordinate_divideShouldHappenAndCoordinateWillBeAdded() {
        // Testen of de Quadtree correct subdivideert bij het bereiken van de capaciteit
        quadtree.insert(1.0, 1.0);
        quadtree.insert(2.0, 2.0);
        quadtree.insert(3.0, 3.0);
        quadtree.insert(4.0, 4.0);

        // Na 4 punten zou de Quadtree zich moeten subdivideren
        assertTrue(quadtree.insert(5.0, 5.0)); // Testen van de subdivisie
    }

    @Test
    public void givenCoordinateOutOfRange_shouldNotAdd() {
        // Test of de Quadtree intersecties correct herkent
        assertTrue(quadtree.intersects(0.0, 0.0, 5.0)); // Binnen de grenzen
        assertFalse(quadtree.intersects(20.0, 20.0, 5.0)); // Buiten de grenzen
    }

    @Test
    public void givenTwoCoordinates_distanceShouldBePositive() {
        // Test de Haversine-functie voor het berekenen van afstanden
        double lat1 = 0.0;
        double lon1 = 0.0;
        double lat2 = 1.0;
        double lon2 = 1.0;

        double result = quadtree.haversine(lat1, lon1, lat2, lon2);
        assertTrue(result > 0); // De afstand moet positief zijn
    }

    @Test
    public void givenInsertWithHalfOutOfBounds_shouldReturnInbounds() {
        // Voeg veel punten toe en zorg ervoor dat de subdivisie en query correct werken
        for (int i = 1; i < 21; i++) {
            quadtree.insert(i, i);
        }

        List<Map<String, Double>> result = quadtree.queryRange(0.0, 0.0, Constants.EARTH_RADIUS);
        assertEquals(10, result.size());
    }
}
