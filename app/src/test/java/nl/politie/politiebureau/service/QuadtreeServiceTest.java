package nl.politie.politiebureau.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.tuple.Pair;
import nl.politie.politiebureau.di.producer.QuadtreeProducer;

import nl.politie.shared.distance.Quadtree;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class QuadtreeServiceTest {

    @InjectMocks
    private QuadtreeService quadtreeService;

    @Mock
    private QuadtreeProducer quadtreeProducer;

    @Mock
    private Quadtree quadtree;

    @Before
    public void setUp() {
        when(quadtreeProducer.getQuadtree()).thenReturn(quadtree);
    }

    @Test
    public void givenTwoBureausWithinRange_shouldReturnListWithPairCoordinates() {
        double latitude =52.0000;
        double longitude = 6.5000;
        double radius = 10.0;

        // Mock het gedrag van de quadtree queryRange
        List<Map<String, Double>> quadtreeResult = Arrays.asList(
                new HashMap<String, Double>() {{
                    put("lat", 52.3786);
                    put("lon", 6.6272);
                }},
                new HashMap<String, Double>() {{
                    put("lat", 51.2786);
                    put("lon", 6.7272);
                }}
        );
        when(quadtree.queryRange(anyDouble(), anyDouble(), anyDouble())).thenReturn(quadtreeResult);

        Set<Pair<Double, Double>> result = quadtreeService.findBureausWithinRange(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.contains(Pair.of(52.3786, 6.6272)));
        assertTrue(result.contains(Pair.of(51.2786, 6.7272)));
    }

    @Test
    public void givenNoResult_shouldReturnEmptyCollection() {
        double latitude = 52.0000;
        double longitude = 6.5000;
        double radius = 1.0;

        List<Map<String, Double>> quadtreeResult = Collections.emptyList();
        when(quadtree.queryRange(anyDouble(), anyDouble(), anyDouble())).thenReturn(quadtreeResult);

        Set<Pair<Double, Double>> result = quadtreeService.findBureausWithinRange(latitude, longitude, radius);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
