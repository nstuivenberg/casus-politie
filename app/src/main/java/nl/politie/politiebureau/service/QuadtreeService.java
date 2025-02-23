package nl.politie.politiebureau.service;

import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.politiebureau.di.producer.QuadtreeProducer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuadtreeService {
    @Inject
    private QuadtreeProducer quadtreeProducer;

    @Inject
    private PolitiebureauDatabase politiebureauDatabase;

    public Set<Pair<BigDecimal, BigDecimal>> findBureausWithinRange(BigDecimal latitude, BigDecimal longitude, BigDecimal radius) {
        List<Map<String, Double>> result = quadtreeProducer
                .getQuadtree()
                .queryRange(latitude.doubleValue(), longitude.doubleValue(), radius.doubleValue());

        if(result.isEmpty()) return Collections.emptySet();

        return result.stream()
                .map(map -> Pair.of(BigDecimal.valueOf(map.get("lat")), BigDecimal.valueOf(map.get("lon"))))
                .collect(Collectors.toSet());
    }

    @PostConstruct
    private void populateQuadtree() {
        politiebureauDatabase.findAll().forEach(politiebureauEntity -> quadtreeProducer.getQuadtree()
                .insert(
                        politiebureauEntity.getLatitude().doubleValue(),
                        politiebureauEntity.getLongitude().doubleValue()
                ));
    }
}
