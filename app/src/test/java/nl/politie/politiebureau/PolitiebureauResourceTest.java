package nl.politie.politiebureau;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PolitiebureauResourceTest {

    @Test
    void testValidateInput_ValidInput() {
        PolitiebureauResource resource = new PolitiebureauResource();
        String validInput = "52.379189";
        BigDecimal result = resource.validateInput(validInput, "latitude");

        assertNotNull(result);
        assertEquals(new BigDecimal("52.379189"), result);
    }
}

