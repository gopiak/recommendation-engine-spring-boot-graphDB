package com.github.webdevgopi.slash.engine;

import com.github.webdevgopi.slash.models.Artist;
import com.github.webdevgopi.slash.models.Customer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://en.wikipedia.org/wiki/Slope_One
 * https://www.baeldung.com/java-collaborative-filtering-recommendations
 */
@Slf4j
public final class SlopeOne {
    private SlopeOne() { }

    private static final Map<Artist, Map<Artist, Double>> diff = new HashMap<>();
    private static final Map<Artist, Map<Artist, Integer>> freq = new HashMap<>();

    public static Map<Customer, HashMap<Artist, Double>> slopeOne(Map<Customer, HashMap<Artist, Double>> data, List<Artist> artists) {
        buildDifferencesMatrix(data);
        return predict(data, artists);
    }

    private static void buildDifferencesMatrix(Map<Customer, HashMap<Artist, Double>> data) {
        for (HashMap<Artist, Double> customer : data.values()) {
            for (Map.Entry<Artist, Double> artistEntry : customer.entrySet()) {
                if (!diff.containsKey(artistEntry.getKey())) {
                    diff.put(artistEntry.getKey(), new HashMap<>());
                    freq.put(artistEntry.getKey(), new HashMap<>());
                }

                for (Map.Entry<Artist, Double> artistEntryInner : customer.entrySet()) {
                    int oldCount = 0;

                    if (freq.get(artistEntry.getKey()).containsKey(artistEntryInner.getKey())) {
                        oldCount = freq.get(artistEntry.getKey()).get(artistEntryInner.getKey());
                    }

                    double oldDiff = 0.0;

                    if (diff.get(artistEntry.getKey()).containsKey(artistEntryInner.getKey())) {
                        oldDiff = diff.get(artistEntry.getKey()).get(artistEntryInner.getKey());
                    }

                    double observedDiff = artistEntry.getValue() - artistEntryInner.getValue();

                    freq.get(artistEntry.getKey()).put(artistEntryInner.getKey(), oldCount + 1);
                    diff.get(artistEntry.getKey()).put(artistEntryInner.getKey(), oldDiff + observedDiff);
                }
            }
        }

        for (Artist j : diff.keySet()) {
            for (Artist i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i);
                int count = freq.get(j).get(i);
                diff.get(j).put(i, oldValue / count);
            }
        }

        log.debug("diff={}", diff);
        log.debug("freq={}", freq);
        log.debug("data={}", data);
    }

    private static Map<Customer, HashMap<Artist, Double>> predict(Map<Customer, HashMap<Artist, Double>> data, List<Artist> artists) {
        Map<Customer, HashMap<Artist, Double>> outputData = new HashMap<>();
        HashMap<Artist, Double> uPred = new HashMap<>();
        HashMap<Artist, Integer> uFreq = new HashMap<>();

        for (Artist artistDiff : diff.keySet()) {
            uFreq.put(artistDiff, 0);
            uPred.put(artistDiff, 0.0);
        }

        for (Map.Entry<Customer, HashMap<Artist, Double>> customerMapEntry : data.entrySet()) {
            for (Artist artist : customerMapEntry.getValue().keySet()) {
                for (Artist artistDiff : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(artistDiff).get(artist) + customerMapEntry.getValue().get(artist);
                        double finalValue = predictedValue * freq.get(artistDiff).get(artist);
                        uPred.put(artistDiff, uPred.get(artistDiff) + finalValue);
                        uFreq.put(artistDiff, uFreq.get(artistDiff) + freq.get(artistDiff).get(artist));
                    } catch (NullPointerException e) {
                        log.warn("A null pointer error was encountered processing artist={} and artistDiff={}", artist, artistDiff);
                    }
                }
            }

            HashMap<Artist, Double> clean = new HashMap<>();

            for (Artist artist : uPred.keySet()) {
                if (uFreq.get(artist) > 0) {
                    clean.put(artist, uPred.get(artist) / uFreq.get(artist));
                }
            }

            for (Artist artist : artists) {
                if (customerMapEntry.getValue().containsKey(artist)) {
                    clean.put(artist, customerMapEntry.getValue().get(artist));
                } else if (!clean.containsKey(artist)) {
                    clean.put(artist, -1.0);
                }
            }

            outputData.put(customerMapEntry.getKey(), clean);
        }

        log.info("outputData={}", outputData);
        return outputData;
    }
}
