package com.czerniecka.coronavirustracker;

import com.czerniecka.coronavirustracker.model.LocationStats;
import com.czerniecka.coronavirustracker.model.RecoveryStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String RECOVERED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();
    private List<RecoveryStats> allRecoveredStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {

        return allStats;
    }


    public List<LocationStats> findByCountry(@RequestParam("searchCountry") String searchCountry) {

        return allStats.stream()
                .filter(record -> record.getCountry().equalsIgnoreCase(searchCountry))
                .collect(Collectors.toList());

    }

    public List<RecoveryStats> getAllRecoveredStats() {

        return allRecoveredStats;

    }

    //Updating data once a day at 00:30:00 London time
    @PostConstruct
    @Scheduled(cron = "0 30 0 * * *",zone = "Europe/London")
    public void fetchVirusData() throws IOException, InterruptedException {

        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPreviousDay(latestCases - previousDayCases);
            newStats.add(locationStat);
        }



        this.allStats = newStats;

    }


    //Updating data once a day at 00:30:00 London time
    @PostConstruct
    @Scheduled(cron = "0 30 0 * * *",zone = "Europe/London")
    public void fetchRecoveredFromVirusData() throws IOException, InterruptedException {

        List<RecoveryStats> newRecoveredStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECOVERED_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());


        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            RecoveryStats recoveryStats = new RecoveryStats();
            recoveryStats.setState(record.get("Province/State"));
            recoveryStats.setCountry(record.get("Country/Region"));
            int latestRecoveredCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayRecoveredCases = Integer.parseInt(record.get(record.size() - 2));
            recoveryStats.setRecoveryStats(latestRecoveredCases);
            recoveryStats.setDiffFromPreviousDay(latestRecoveredCases - previousDayRecoveredCases);
            newRecoveredStats.add(recoveryStats);
        }


        this.allRecoveredStats = newRecoveredStats;

    }

}
