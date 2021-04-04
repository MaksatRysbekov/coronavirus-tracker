package com.maxdev.coronatrackingapp.services;

import com.maxdev.coronatrackingapp.models.LocationStats;
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
    private int latestReportedCases;
    private int prevDayCases;

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public List<LocationStats> fetchCoronaData() throws IOException {
        List<LocationStats> newStats = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(CORONA_DATA_URL, String.class);

        assert response != null;
        StringReader csvReader = new StringReader(response);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {
            latestReportedCases = Integer.parseInt(record.get(record.size() - 1));
            prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            LocationStats stats = LocationStats.builder()
                                        .state(record.get("Province/State"))
                                        .country(record.get("Country/Region"))
                                        .latestReportedCases(latestReportedCases)
                                        .diffFromPrevDay(latestReportedCases - prevDayCases)
                                        .build();
            newStats.add(stats);
            log.info("Received data: {}", stats);
        }

        return newStats;

    }
}
