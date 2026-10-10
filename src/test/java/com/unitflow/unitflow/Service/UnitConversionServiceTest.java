
package com.unitflow.unitflow.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class UnitConversionServiceTest {

    UnitConversionService service = new UnitConversionService();

    @Test
    void testKilometersToMeters() {
        assertEquals(2000.0, service.convert(2, "km", "m"), 0.001);
    }

    @Test
    void testGramsToKilograms() {
        assertEquals(0.5, service.convert(500, "g", "kg"), 0.001);
    }

    @Test
    void testCelsiusToFahrenheit() {
        assertEquals(32.0, service.convert(0, "c", "f"), 0.001);
    }

    @Test
    void testCelsiusToKelvin() {
        assertEquals(373.15, service.convert(100, "c", "k"), 0.001);
    }

    @Test
    void testSameUnitConversion() {
        assertEquals(5.0, service.convert(5, "km", "km"), 0.001);
    }

    @Test
    void testUnsupportedUnits() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.convert(10, "abc", "xyz")
        );
    }

    public UnitConversionService getService() {
        return service;
    }

    public void setService(UnitConversionService service) {
        this.service = service;
    }
}