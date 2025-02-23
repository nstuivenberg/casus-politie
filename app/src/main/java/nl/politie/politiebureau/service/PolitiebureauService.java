package nl.politie.politiebureau.service;

import nl.politie.politiebureau.di.producer.DistanceCalculatorProducer;
import nl.politie.politiebureau.dto.PolitiebureauDTO;
import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.politiebureau.domain.PolitiebureauEntity;
import org.apache.commons.lang3.tuple.Pair;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
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

    public List<PolitiebureauDTO> findBureausWithinRange(BigDecimal latitude, BigDecimal longitude, BigDecimal radius) {
        List<PolitiebureauEntity> politiebureauEntities = database.findAll();

        return politiebureauEntities.stream()
                .filter(politiebureauEntity ->
                        distanceCalculator.produceDistanceCalculator().calculateDistance(
                                latitude, longitude, politiebureauEntity.getLatitude(), politiebureauEntity.getLongitude()
                        )
                                .compareTo(radius) <= 0)
                .map(entity -> PolitiebureauDTO
                        .builder()
                        .name(entity.getName())
                        .plaats(entity.getPlaats())
                        .latitude(entity.getLatitude().toString())
                        .longitude(entity.getLongitude().toString())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PolitiebureauDTO> findBureausWithinRangeFast(BigDecimal latitude, BigDecimal longitude, BigDecimal radius) {
        Set<Pair<BigDecimal, BigDecimal>> coordinateSet = quadtreeService.findBureausWithinRange(
                latitude, longitude, radius
        );

        if (coordinateSet.isEmpty()) return Collections.emptyList();

        return database.findAll().stream()
                .filter(b -> coordinateSet.contains(Pair.of(b.getLatitude(), b.getLongitude())))
                .map(entity -> PolitiebureauDTO
                        .builder()
                        .name(entity.getName())
                        .plaats(entity.getPlaats())
                        .latitude(entity.getLatitude().toString())
                        .longitude(entity.getLongitude().toString())
                        .build())
                .collect(Collectors.toList());
    }
}
