package com.czerniecka.coronavirustracker.controller;

import com.czerniecka.coronavirustracker.CoronaVirusDataService;
import com.czerniecka.coronavirustracker.model.LocationStats;
import com.czerniecka.coronavirustracker.model.RecoveryStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping
public class HomeController {


    CoronaVirusDataService coronaVirusDataService;

    @Autowired
    public HomeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    //Create home view with aggregate numbers & full data table
    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        List<RecoveryStats> allRecoveredStats = coronaVirusDataService.getAllRecoveredStats();
        int totalReportedCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();
        int totalRecoveredCases = allRecoveredStats.stream().mapToInt(RecoveryStats::getRecoveryStats).sum();
        int totalNewRecoveredCases = allRecoveredStats.stream().mapToInt(RecoveryStats::getDiffFromPreviousDay).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);
        model.addAttribute("totalNewRecoveredCases", totalNewRecoveredCases);


        return "home";
    }

}
