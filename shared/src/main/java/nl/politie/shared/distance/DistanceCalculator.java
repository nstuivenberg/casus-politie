package nl.politie.shared.distance;

import nl.politie.shared.distance.config.Constants;

import java.math.BigDecimal;

public class DistanceCalculator {

    public BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());
        double lon1Rad = Math.toRadians(lon1.doubleValue());
        double lon2Rad = Math.toRadians(lon2.doubleValue());

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * Constants.EARTH_RADIUS;

        return BigDecimal.valueOf(distance);
    }
}
