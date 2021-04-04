package com.maxdev.coronatrackingapp.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class LocationStats {
    private String state;
    private String country;
    private int latestReportedCases;
    private int diffFromPrevDay;
}
