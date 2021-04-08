package com.github.webdevgopi.slash.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rating {
    private String id;
    private double score;
    private Customer by;
    private Artist about;
}
