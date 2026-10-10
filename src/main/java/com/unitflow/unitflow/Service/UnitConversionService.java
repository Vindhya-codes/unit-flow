
package com.unitflow.unitflow.Service;

import org.springframework.stereotype.Service;

@Service
public class UnitConversionService {

    public double convert(double value, String from, String to) {

        from = from.toLowerCase();
        to = to.toLowerCase();

        if (from.equals(to)) {
            return value;
        }

        // Length conversions: convert to meters first
        double meters;

        switch (from) {
            case "km":
                meters = value * 1000;
                break;
            case "m":
                meters = value;
                break;
            case "cm":
                meters = value / 100;
                break;
            case "mi":
                meters = value * 1609.344;
                break;
            default:
                meters = Double.NaN;
        }

        if (!Double.isNaN(meters)) {
            switch (to) {
                case "km":
                    return meters / 1000;
                case "m":
                    return meters;
                case "cm":
                    return meters * 100;
                case "mi":
                    return meters / 1609.344;
            }
        }

        // Weight conversions: convert to kilograms first
        double kilograms;

        switch (from) {
            case "kg":
                kilograms = value;
                break;
            case "g":
                kilograms = value / 1000;
                break;
            case "lb":
                kilograms = value * 0.45359237;
                break;
            default:
                kilograms = Double.NaN;
        }

        if (!Double.isNaN(kilograms)) {
            switch (to) {
                case "kg":
                    return kilograms;
                case "g":
                    return kilograms * 1000;
                case "lb":
                    return kilograms / 0.45359237;
            }
        }

        // Temperature conversions: convert to Celsius first
        double celsius;

        switch (from) {
            case "c":
                celsius = value;
                break;
            case "f":
                celsius = (value - 32) * 5 / 9;
                break;
            case "k":
                celsius = value - 273.15;
                break;
            default:
                celsius = Double.NaN;
        }

        if (!Double.isNaN(celsius)) {
            switch (to) {
                case "c":
                    return celsius;
                case "f":
                    return (celsius * 9 / 5) + 32;
                case "k":
                    return celsius + 273.15;
            }
        }

        throw new IllegalArgumentException(
            "Unsupported conversion: " + from + " to " + to
        );
    }
}