package com.maxdev.coronatrackingapp.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;

@Service
public class CoronavirusDataService {
    private final static String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchCoronaData() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(CORONA_DATA_URL, String.class);
        System.out.println(response);


        assert response != null;
        StringReader csvReader = new StringReader(response);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {
            String state = record.get("Province/State");
            System.out.println(state);

        }

    }
}
