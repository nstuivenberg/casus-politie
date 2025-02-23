package nl.politie.politiebureau.service;

import nl.politie.politiebureau.di.producer.DistanceCalculatorProducer;
import nl.politie.politiebureau.dto.PolitiebureauDTO;
import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.politiebureau.domain.PolitiebureauEntity;
import org.apache.commons.lang3.tuple.Pair;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class PolitiebureauService {

    @Inject
    private PolitiebureauDatabase database;
    @Inject
    private DistanceCalculatorProducer distanceCalculator;
    @Inject
    private QuadtreeService quadtreeService;

    public List<PolitiebureauDTO> findBureausWithinRange(double latitude, double longitude, double radius) {
        List<PolitiebureauEntity> politiebureauEntities = database.findAll();

        return politiebureauEntities.stream()
                .filter(politiebureauEntity ->
                        distanceCalculator.produceDistanceCalculator().calculateDistance(
                                latitude, longitude, politiebureauEntity.getLatitude(), politiebureauEntity.getLongitude()
                        ) <= radius)
                .map(entity -> PolitiebureauDTO
                        .builder()
                        .name(entity.getName())
                        .plaats(entity.getPlaats())
                        .latitude(String.valueOf(entity.getLatitude()))
                        .longitude(String.valueOf(entity.getLongitude()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<PolitiebureauDTO> findBureausWithinRangeFast(double latitude, double longitude, double radius) {
        Set<Pair<Double, Double>> coordinateSet = quadtreeService.findBureausWithinRange(
                latitude, longitude, radius
        );

        if (coordinateSet.isEmpty()) return Collections.emptyList();

        return database.findAll().stream()
                .filter(b -> coordinateSet.contains(Pair.of(b.getLatitude(), b.getLongitude())))
                .map(entity -> PolitiebureauDTO
                        .builder()
                        .name(entity.getName())
                        .plaats(entity.getPlaats())
                        .latitude(String.valueOf(entity.getLatitude()))
                        .longitude(String.valueOf(entity.getLongitude()))
                        .build())
                .collect(Collectors.toList());
    }
}
