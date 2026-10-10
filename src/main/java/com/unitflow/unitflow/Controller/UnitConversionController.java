
package com.unitflow.unitflow.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unitflow.unitflow.Service.CurrencyConversionService;
import com.unitflow.unitflow.Service.UnitConversionService;

@RestController
@RequestMapping("/api")
public class UnitConversionController {

    private final UnitConversionService service;
    private final CurrencyConversionService currencyService;

    public UnitConversionController(
        UnitConversionService service,
        CurrencyConversionService currencyService) {
    this.service = service;
    this.currencyService = currencyService;
}

    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam double value,
            @RequestParam String from,
            @RequestParam String to) {

        if (!Double.isFinite(value)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Value must be finite"));
        }

        try {
            double result = service.convert(value, from, to);

            if (!Double.isFinite(result)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid conversion result"));
            }

            return ResponseEntity.ok(Map.of(
                    "value", value,
                    "from", from,
                    "to", to,
                    "result", result
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/currency")
public ResponseEntity<?> convertCurrency(
        @RequestParam double amount,
        @RequestParam String from,
        @RequestParam String to) {

    try {
        double result = currencyService.convert(amount, from, to);

        return ResponseEntity.ok(Map.of(
                "amount", amount,
                "from", from.toUpperCase(),
                "to", to.toUpperCase(),
                "result", result
        ));

    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));

    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("error", e.getMessage()));
    }
}
}