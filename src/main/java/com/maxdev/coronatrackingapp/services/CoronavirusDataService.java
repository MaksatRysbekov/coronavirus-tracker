package com.maxdev.coronatrackingapp.services;

import com.maxdev.coronatrackingapp.models.LocationStats;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CoronavirusDataService {
    private final static String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchCoronaData() throws IOException {
        List<LocationStats> newStats = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(CORONA_DATA_URL, String.class);

        assert response != null;
        StringReader csvReader = new StringReader(response);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {
            LocationStats stats = LocationStats.builder()
                                        .state(record.get("Province/State"))
                                        .country(record.get("Country/Region"))
                                        .latestReportedCases(Integer.parseInt(record.get(record.size() - 1)))
                                        .build();
            newStats.add(stats);
            log.info("Received data: {}", stats);
        }

        allStats = newStats;

    }
}
