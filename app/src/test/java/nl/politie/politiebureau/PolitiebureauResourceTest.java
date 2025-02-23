package nl.politie.politiebureau;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolitiebureauResourceTest {

    @Test
    void testValidateInput_ValidInput() {
        PolitiebureauResource resource = new PolitiebureauResource();
        String validInput = "52.379189";
        double result = resource.validateInput(validInput, "latitude");

        assertEquals(52.379189, result);
    }
}

