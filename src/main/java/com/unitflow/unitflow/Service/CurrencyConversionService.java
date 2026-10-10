
package com.unitflow.unitflow.Service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CurrencyConversionService {

    private final RestClient restClient = RestClient.create();

    public double convert(double amount, String from, String to) {

        from = from.toUpperCase();
        to = to.toUpperCase();

        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Amount must be finite");
        }

        if (from.equals(to)) {
            return amount;
        }

        String url = "https://api.frankfurter.dev/v2/rate/"
                + from + "/" + to;

        try {
            Map<?, ?> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Map.class);

            if (response == null || !(response.get("rate") instanceof Number)) {
                throw new IllegalArgumentException(
                        "Exchange rate is unavailable"
                );
            }

            double rate = ((Number) response.get("rate")).doubleValue();

            if (!Double.isFinite(rate) || rate <= 0) {
                throw new IllegalArgumentException(
                        "Invalid exchange rate received"
                );
            }

            return amount * rate;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to retrieve exchange rates. Try again later."
            );
        }
    }
}