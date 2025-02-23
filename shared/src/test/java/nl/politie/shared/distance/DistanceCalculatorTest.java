package nl.politie.shared.distance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class DistanceCalculatorTest {

    @InjectMocks
    private DistanceCalculator distanceCalculator;


    @Test
    public void givenIdenticalCoordinates_distanceShouldBeZero() {
        double lat = 52.103543;
        double lon = 5.089719;

        assertEquals(0.0, distanceCalculator.calculateDistance(lat, lon, lat, lon), 0.001);

        assertTrue(true);
    }

    @Test
    public void givenTwoCoordinatesFiveKmApart_shouldReturnFive() {
        double lat1 = 52;
        double lon1 = 4;
        double lat2 = 52.044966;
        double lon2 = 4;

        assertEquals(5.0, distanceCalculator.calculateDistance(lat1, lon1, lat2, lon2), 0.001);
    }
}
