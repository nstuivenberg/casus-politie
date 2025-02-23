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

import java.math.BigDecimal;
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
        BigDecimal latitude = new BigDecimal("52.0000");
        BigDecimal longitude = new BigDecimal("6.5000");
        BigDecimal radius = new BigDecimal("10.0");

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

        Set<Pair<BigDecimal, BigDecimal>> result = quadtreeService.findBureausWithinRange(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.contains(Pair.of(new BigDecimal("52.3786"), new BigDecimal("6.6272"))));
        assertTrue(result.contains(Pair.of(new BigDecimal("51.2786"), new BigDecimal("6.7272"))));
    }

    @Test
    public void givenNoResult_shouldReturnEmptyCollection() {
        BigDecimal latitude = new BigDecimal("52.0000");
        BigDecimal longitude = new BigDecimal("6.5000");
        BigDecimal radius = new BigDecimal("1.0");

        List<Map<String, Double>> quadtreeResult = Collections.emptyList();
        when(quadtree.queryRange(anyDouble(), anyDouble(), anyDouble())).thenReturn(quadtreeResult);

        Set<Pair<BigDecimal, BigDecimal>> result = quadtreeService.findBureausWithinRange(latitude, longitude, radius);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
