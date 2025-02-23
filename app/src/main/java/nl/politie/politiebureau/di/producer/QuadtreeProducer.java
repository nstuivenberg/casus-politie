package nl.politie.politiebureau.di.producer;

import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.shared.distance.Quadtree;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

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
        double minLat = politiebureauDatabase.getMinimalLatitude();
        double minLon = politiebureauDatabase.getMinimalLongitude();
        double maxLat = politiebureauDatabase.getMaximalLatitude();
        double maxLon = politiebureauDatabase.getMaximalLongitude();

        double centerLatitude = (minLat + maxLat) / 2;
        double centerLongitude = (minLon + maxLon) / 2;

        double height = maxLat - minLat;
        double width = maxLon - minLon;

        this.quadtree = new Quadtree(centerLatitude, centerLongitude, width, height);
    }

    @Produces
    public Quadtree getQuadtree() {
        return quadtree;
    }
}
