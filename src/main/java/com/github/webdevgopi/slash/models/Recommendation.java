package com.github.webdevgopi.slash.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recommendation {
    private Customer matchedCustomer;
    private HashMap<Artist, Double> recommendationsMap;
    private HashMap<Artist, Double> ratingsMap;
    private HashMap<Artist, Double> resultsMap;
}
