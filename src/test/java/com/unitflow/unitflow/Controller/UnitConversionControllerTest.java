
package com.unitflow.unitflow.Controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unitflow.unitflow.Service.CurrencyConversionService;
import com.unitflow.unitflow.Service.UnitConversionService;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(UnitConversionController.class)
class UnitConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UnitConversionService unitService;

    @MockitoBean
    private CurrencyConversionService currencyService;

    @Test
    void testSuccessfulConversion() throws Exception {
        when(unitService.convert(2.0, "km", "m"))
                .thenReturn(2000.0);

        mockMvc.perform(get("/api/convert")
                        .param("value", "2")
                        .param("from", "km")
                        .param("to", "m"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(2000.0));
    }

    @Test
    void testUnsupportedUnits() throws Exception {
        when(unitService.convert(10.0, "abc", "xyz"))
                .thenThrow(new IllegalArgumentException(
                        "Unsupported conversion"));

        mockMvc.perform(get("/api/convert")
                        .param("value", "10")
                        .param("from", "abc")
                        .param("to", "xyz"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testMissingParameter() throws Exception {
        mockMvc.perform(get("/api/convert")
                        .param("value", "10")
                        .param("from", "km"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidNumber() throws Exception {
        mockMvc.perform(get("/api/convert")
                        .param("value", "abc")
                        .param("from", "km")
                        .param("to", "m"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUnknownEndpoint() throws Exception {
        mockMvc.perform(get("/api/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCurrencyConversion() throws Exception {
        when(currencyService.convert(100.0, "USD", "INR"))
                .thenReturn(8500.0);

        mockMvc.perform(get("/api/currency")
                        .param("amount", "100")
                        .param("from", "USD")
                        .param("to", "INR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8500.0));
    }

    @Test
    void testCurrencyApiFailure() throws Exception {
        when(currencyService.convert(100.0, "USD", "INR"))
                .thenThrow(new IllegalStateException(
                        "Unable to retrieve exchange rates"));

        mockMvc.perform(get("/api/currency")
                        .param("amount", "100")
                        .param("from", "USD")
                        .param("to", "INR"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").exists());
    }
}