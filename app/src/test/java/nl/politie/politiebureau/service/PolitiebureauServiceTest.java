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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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
                new PolitiebureauEntity("Bureau 1", "Stad 1", new BigDecimal("52.3786"), new BigDecimal("6.6272")),
                new PolitiebureauEntity("Bureau 2", "Stad 2", new BigDecimal("51.2786"), new BigDecimal("6.7272"))
        );

        when(database.findAll()).thenReturn(politiebureauEntities);
        when(distanceCalculatorProducer.produceDistanceCalculator()).thenReturn(distanceCalculator);
    }

    @Test
    public void testFindBureausWithinRange() {
        BigDecimal latitude = new BigDecimal("52.0000");
        BigDecimal longitude = new BigDecimal("6.5000");
        BigDecimal radius = new BigDecimal("10.0");

        when(distanceCalculator.calculateDistance(any(), any(), any(), any()))
                .thenReturn(new BigDecimal("5.0"))
                .thenReturn(new BigDecimal("15.0"));

        List<PolitiebureauDTO> result = politiebureauService.findBureausWithinRange(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bureau 1", result.get(0).getName());
        assertEquals("Stad 1", result.get(0).getPlaats());
    }

    @Test
    public void testFindBureausWithinRangeFast() {
        BigDecimal latitude = new BigDecimal("52.0000");
        BigDecimal longitude = new BigDecimal("6.5000");
        BigDecimal radius = new BigDecimal("10.0");

        Set<Pair<BigDecimal, BigDecimal>> coordinateSet = new HashSet<>();
        coordinateSet.add(Pair.of(new BigDecimal("52.3786"), new BigDecimal("6.6272")));

        when(quadtreeService.findBureausWithinRange(latitude, longitude, radius)).thenReturn(coordinateSet);

        List<PolitiebureauDTO> result = politiebureauService.findBureausWithinRangeFast(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bureau 1", result.get(0).getName());
        assertEquals("Stad 1", result.get(0).getPlaats());
    }

    @Test
    public void testFindBureausWithinRangeFastEmpty() {
        BigDecimal latitude = new BigDecimal("52.0000");
        BigDecimal longitude = new BigDecimal("6.5000");
        BigDecimal radius = new BigDecimal("1.0");
        Set<Pair<BigDecimal, BigDecimal>> coordinateSet = Collections.emptySet();

        when(quadtreeService.findBureausWithinRange(latitude, longitude, radius)).thenReturn(coordinateSet);

        List<PolitiebureauDTO> result = politiebureauService.findBureausWithinRangeFast(latitude, longitude, radius);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
