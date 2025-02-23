package nl.politie.shared.distance;

import lombok.Getter;
import nl.politie.shared.distance.config.Constants;

import java.util.*;



/**
 * Een Quadtree datastructuur voor efficiënte geografische zoekopdrachten.
 * Deze implementatie verdeelt een gebied in vier kwadranten en slaat punten (politiebureaus) op.
 */
@Getter
public class Quadtree {
    private final double latitudeCenter;
    private final double longitudeCenter;

    private final double width;
    private final double height;
    private static final int CAPACITY = 4;
    private final List<Map<String, Double>> coordinates;
    private boolean divided = false;
    private Quadtree northeast;
    private Quadtree northwest;
    private Quadtree southeast;
    private Quadtree southwest;

    /**
     * Maakt een nieuwe Quadtree aan.
     * @param lat De y-coördinaat van het midden van de regio.
     * @param lon De x-coördinaat van het midden van de regio.
     * @param width De breedte van de regio. Verschil in latitude
     * @param height De hoogte van de regio. Verschil in longitude
     */
    public Quadtree(double lat, double lon, double width, double height) {
        this.latitudeCenter = lat;
        this.longitudeCenter = lon;

        this.width = width;
        this.height = height;

        this.coordinates = new ArrayList<>();
    }

    /**
     * Voegt een politiebureau toe aan de Quadtree.
     * @param lat De latitude van het politiebureau dat toegevoegd moet worden.
     * @param lon De longitude van het politiebureau dat toegevoegd moet worden.
     * @return True als het toevoegen gelukt is, anders false.
     */
    public boolean insert(double lat, double lon) {
        if (!contains(lat, lon)) return false;

        if(coordinates.size() < CAPACITY) {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", lat);
            point.put("lon", lon);
            coordinates.add(point);
            return true;
        }

        if (!divided) subdivide();

        return (northeast.insert(lat, lon) || northwest.insert(lat, lon) ||
                southeast.insert(lat, lon) || southwest.insert(lat, lon));
    }

    /**
     * Verdeelt de huidige Quadtree in vier sub-kwadranten.
     */
    private void subdivide() {
        double hw = width / 2;
        double hh = height / 2;

        northeast = new Quadtree(latitudeCenter + hh, longitudeCenter + hw, hw, hh);
        northwest = new Quadtree(latitudeCenter + hh, longitudeCenter - hw, hw, hh);
        southeast = new Quadtree(latitudeCenter - hh, longitudeCenter + hw, hw, hh);
        southwest = new Quadtree(latitudeCenter - hh, longitudeCenter - hw, hw, hh);

        divided = true;
    }

    /**
     * Controleert of een gegeven punt binnen de grenzen van deze Quadtree ligt.
     *
     * @param lat De latitude van het punt.
     * @param lon De longitude van het punt.
     * @return True als het punt binnen de grenzen ligt, anders false.
     */
    boolean contains(double lat, double lon) {
        // 1. Bereken de minimale en maximale breedtegraden (latitude)
        double minLat = latitudeCenter - height;
        double maxLat = latitudeCenter + height;

        // 2. Bereken de minimale en maximale lengtegraden (longitude)
        double minLon = longitudeCenter - width;
        double maxLon = longitudeCenter + width;

        // 3. Controleer of het punt binnen deze grenzen ligt
        return (lat >= minLat && lat <= maxLat) && (lon >= minLon && lon <= maxLon);
    }

    /**
     * Zoekt alle politiebureaus binnen een bepaalde straal rond een punt.
     * @param lat De latitude van het zoekpunt.
     * @param lon De longitude van het zoekpunt.
     * @param radius De zoekradius in kilometers.
     * @return Een lijst van politiebureaus binnen de opgegeven straal.
     */
    public List<Map<String, Double>> queryRange(double lat, double lon, double radius) {
        List<Map<String, Double>>  foundCoordinates = new ArrayList<>();
        if (!intersects(lat, lon, radius)) return foundCoordinates;

        for (Map<String, Double> point : coordinates) {
            double d = haversine(lat, lon, point.get("lat"), point.get("lon"));
            if (d <= radius) foundCoordinates.add(point);
        }

        if (divided) {
            foundCoordinates.addAll(northeast.queryRange(lat, lon, radius));
            foundCoordinates.addAll(northwest.queryRange(lat, lon, radius));
            foundCoordinates.addAll(southeast.queryRange(lat, lon, radius));
            foundCoordinates.addAll(southwest.queryRange(lat, lon, radius));
        }

        return foundCoordinates;
    }

    /**
     * Controleert of de zoekcirkel de grenzen van deze Quadtree snijdt.
     * @param lat De latitude van het zoekpunt.
     * @param lon De longitude van het zoekpunt.
     * @param radius De zoekradius in kilometers.
     * @return True als er overlap is, anders false.
     */
    boolean intersects(double lat, double lon, double radius) {
        return !(lat + radius < latitudeCenter - height ||
                lat - radius > latitudeCenter + height ||
                lon + radius < longitudeCenter - width ||
                lon - radius > longitudeCenter + width);
    }

    /**
     * Berekening van de afstand tussen twee geografische punten met behulp van de Haversine-formule.
     * @param lat1 De latitude van het eerste punt.
     * @param lon1 De longitude van het eerste punt.
     * @param lat2 De latitude van het tweede punt.
     * @param lon2 De longitude van het tweede punt.
     * @return De afstand tussen de punten in kilometers.
     */
    double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * Constants.EARTH_RADIUS * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }


}
