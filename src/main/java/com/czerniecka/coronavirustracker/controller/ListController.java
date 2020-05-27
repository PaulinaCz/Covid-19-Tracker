package com.czerniecka.coronavirustracker.controller;

import com.czerniecka.coronavirustracker.CoronaVirusDataService;
import com.czerniecka.coronavirustracker.model.LocationStats;
import com.czerniecka.coronavirustracker.model.RecoveryStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
@RequestMapping
public class ListController {


    CoronaVirusDataService coronaVirusDataService;

    @Autowired
    public ListController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    //Create home view with aggregate numbers & filtered data table
    @GetMapping(value = "/searchCountry")
    public String list(@RequestParam String searchCountry, Model model ) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        List<LocationStats> filteredStats = coronaVirusDataService.findByCountry(searchCountry);
        List<RecoveryStats> allRecoveredStats = coronaVirusDataService.getAllRecoveredStats();

        int totalReportedCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();
        int totalRecoveredCases = allRecoveredStats.stream().mapToInt(RecoveryStats::getRecoveryStats).sum();
        int totalNewRecoveredCases = allRecoveredStats.stream().mapToInt(RecoveryStats::getDiffFromPreviousDay).sum();
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);
        model.addAttribute("totalNewRecoveredCases", totalNewRecoveredCases);
        if(searchCountry.equals("")) {
            model.addAttribute("locationStats", allStats);
            return "home";
        }

        model.addAttribute("filteredStats", filteredStats);
        return "list";

    }

}
