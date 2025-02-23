package nl.politie.politiebureau.service;

import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.politiebureau.di.producer.QuadtreeProducer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuadtreeService {
    @Inject
    private QuadtreeProducer quadtreeProducer;

    @Inject
    private PolitiebureauDatabase politiebureauDatabase;

    public Set<Pair<Double, Double>> findBureausWithinRange(double latitude, double longitude, double radius) {
        List<Map<String, Double>> result = quadtreeProducer
                .getQuadtree()
                .queryRange(latitude, longitude, radius);

        if(result.isEmpty()) return Collections.emptySet();

        return result.stream()
                .map(map -> Pair.of(map.get("lat"), map.get("lon")))
                .collect(Collectors.toSet());
    }

    @PostConstruct
    private void populateQuadtree() {
        politiebureauDatabase.findAll().forEach(politiebureauEntity -> quadtreeProducer.getQuadtree()
                .insert(
                        politiebureauEntity.getLatitude(),
                        politiebureauEntity.getLongitude()
                ));
    }
}
