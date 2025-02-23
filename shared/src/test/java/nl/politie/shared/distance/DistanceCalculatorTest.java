package nl.politie.shared.distance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class DistanceCalculatorTest {

    @InjectMocks
    private DistanceCalculator distanceCalculator;


    @Test
    public void givenIdenticalCoordinates_distanceShouldBeZero() {
        BigDecimal lat = new BigDecimal("52.103543");
        BigDecimal lon = new BigDecimal("5.089719");

        assertEquals(new BigDecimal("0.0"), distanceCalculator.calculateDistance(lat, lon, lat, lon));

        assertTrue(true);
    }

    @Test
    public void givenTwoCoordinatesFiveKmApart_shouldReturnFive() {
        BigDecimal lat1 = new BigDecimal("52");
        BigDecimal lon1 = new BigDecimal("4");
        BigDecimal lat2 = new BigDecimal("52.0449661");
        BigDecimal lon2 = new BigDecimal("4");

        assertEquals(new BigDecimal("5.0"), distanceCalculator.calculateDistance(lat1, lon1, lat2, lon2).setScale(1, RoundingMode.HALF_UP));
    }
}
