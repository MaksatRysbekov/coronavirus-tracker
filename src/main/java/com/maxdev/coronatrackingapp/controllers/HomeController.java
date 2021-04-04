package com.maxdev.coronatrackingapp.controllers;

import com.maxdev.coronatrackingapp.models.LocationStats;
import com.maxdev.coronatrackingapp.services.CoronavirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CoronavirusDataService coronavirusDataService;

    @GetMapping("/")
    public String home(Model model) throws IOException {
        List<LocationStats> statsList = coronavirusDataService.fetchCoronaData();
        model.addAttribute("coronaStats", statsList);
        model.addAttribute("totalCases", statsList.stream().mapToInt(LocationStats::getLatestReportedCases).sum());
        model.addAttribute("totalNewCases", statsList.stream().mapToInt(LocationStats::getDiffFromPrevDay).sum());
        return "home";
    }

}
