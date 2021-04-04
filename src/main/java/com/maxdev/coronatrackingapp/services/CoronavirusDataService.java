package com.maxdev.coronatrackingapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class CoronavirusDataService {
    private final static String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct
    public void fetchCoronaData() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(CORONA_DATA_URL, String.class);
        System.out.println(response);
    }
}
