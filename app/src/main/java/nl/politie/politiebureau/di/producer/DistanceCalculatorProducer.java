package nl.politie.politiebureau.di.producer;

import nl.politie.shared.distance.DistanceCalculator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class DistanceCalculatorProducer {
    @Produces
    public DistanceCalculator produceDistanceCalculator() {
        return new DistanceCalculator();
    }
}
