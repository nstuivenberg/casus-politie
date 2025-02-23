package nl.politie.politiebureau.service;

import nl.politie.politiebureau.di.producer.DistanceCalculatorProducer;
import nl.politie.politiebureau.domain.PolitiebureauEntity;
import nl.politie.politiebureau.dto.PolitiebureauDTO;
import nl.politie.politiebureau.repository.PolitiebureauDatabase;
import nl.politie.shared.distance.DistanceCalculator;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PolitiebureauServiceTest {

    @InjectMocks
    private PolitiebureauService politiebureauService;

    @Mock
    private PolitiebureauDatabase database;

    @Mock
    private DistanceCalculatorProducer distanceCalculatorProducer;

    @Mock
    private QuadtreeService quadtreeService;

    @Mock
    private DistanceCalculator distanceCalculator;

    @Before
    public void setUp() {
        List<PolitiebureauEntity> politiebureauEntities = Arrays.asList(
                new PolitiebureauEntity("Bureau 1", "Stad 1", 52.3786, 6.6272),
                new PolitiebureauEntity("Bureau 2", "Stad 2", 51.2786, 6.7272)
        );

        when(database.findAll()).thenReturn(politiebureauEntities);
        when(distanceCalculatorProducer.produceDistanceCalculator()).thenReturn(distanceCalculator);
    }

    @Test
    public void testFindBureausWithinRange() {
        double latitude = 52.0000;
        double longitude = 6.5000;
        double radius = 10.0;

        when(distanceCalculator.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0)
                .thenReturn(15.0);

        List<PolitiebureauDTO> result = politiebureauService.findBureausWithinRange(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bureau 1", result.get(0).getName());
        assertEquals("Stad 1", result.get(0).getPlaats());
    }

    @Test
    public void testFindBureausWithinRangeFast() {
        double latitude = 52.0000;
        double longitude = 6.5000;
        double radius = 10.0;

        Set<Pair<Double, Double>> coordinateSet = new HashSet<>();
        coordinateSet.add(Pair.of(52.3786, 6.6272));

        when(quadtreeService.findBureausWithinRange(latitude, longitude, radius)).thenReturn(coordinateSet);

        List<PolitiebureauDTO> result = politiebureauService.findBureausWithinRangeFast(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bureau 1", result.get(0).getName());
        assertEquals("Stad 1", result.get(0).getPlaats());
    }


    @Test
    public void testFindBureausWithinRangeFastEmpty() {
        double latitude = 52.0000;
        double longitude = 6.5000;
        double radius = 1.0;
        Set<Pair<Double, Double>> coordinateSet = Collections.emptySet();

        when(quadtreeService.findBureausWithinRange(latitude, longitude, radius)).thenReturn(coordinateSet);

        List<PolitiebureauDTO> result = politiebureauService.findBureausWithinRangeFast(latitude, longitude, radius);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
