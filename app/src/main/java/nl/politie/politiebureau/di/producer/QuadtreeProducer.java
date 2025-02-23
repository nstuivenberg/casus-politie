package nl.politie.politiebureau.di.producer;

import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.shared.distance.Quadtree;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ApplicationScoped
public class QuadtreeProducer {

    @Inject
    private PolitiebureauDatabase politiebureauDatabase;

    private Quadtree quadtree;


    /**
     * <p>Initialiseert de Quadtree door het minimale en maximale geografische coördinaten
     * (latitude en longitude) van de politiebureaus op te halen en het centrum, de breedte
     * en de hoogte van het te dekken gebied te berekenen.
     * </p>
     * <p>
     * Deze methode wordt uitgevoerd na de constructie van het object, zoals gespecificeerd
     * door de {@link PostConstruct} annotatie. De waarden voor het centrum, de breedte en
     * de hoogte worden berekend op basis van de minimale en maximale latitude en longitude,
     * waarna een nieuwe Quadtree wordt aangemaakt.
     * </p>
     * <p>
     * De berekeningen zijn als volgt:
     * </p>
     * <ul>
     *   <li>
     *      Het centrum van het gebied wordt bepaald door het gemiddelde van de minimale en
     *      maximale waarden voor zowel latitude als longitude.
     *   </li>
     *   <li>
     *      De breedte en hoogte worden berekend als het verschil tussen de maximale en minimale
     *      longitude en latitude.
     *   </li>
     * </ul>
     * Deze waarden worden gebruikt om een nieuwe {@link Quadtree} te initialiseren.
     */
    @PostConstruct
    public void init() {
        BigDecimal minLat = politiebureauDatabase.getMinimalLatitude();
        BigDecimal minLon = politiebureauDatabase.getMinimalLongitude();
        BigDecimal maxLat = politiebureauDatabase.getMaximalLatitude();
        BigDecimal maxLon = politiebureauDatabase.getMaximalLongitude();

        BigDecimal centerLatitude = (minLat.add(maxLat)).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        BigDecimal centerLongitude = (minLon.add(maxLon)).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

        BigDecimal height = maxLat.subtract(minLat);
        BigDecimal width = maxLon.subtract(minLon);

        this.quadtree = new Quadtree(centerLatitude.doubleValue(), centerLongitude.doubleValue(), width.doubleValue(), height.doubleValue());
    }

    @Produces
    public Quadtree getQuadtree() {
        return quadtree;
    }
}
