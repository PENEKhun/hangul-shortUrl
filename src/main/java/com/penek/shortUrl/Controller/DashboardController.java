package com.penek.shortUrl.Controller;

import com.penek.shortUrl.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;


    @GetMapping("/all/{hangul}")
    public ResponseEntity allDashboard(@PathVariable String hangul){
        return ResponseEntity.ok(dashboardService.analyLog(hangul));
    }

    @GetMapping("/country/{hangul}")
    public void countryDashboard(@PathVariable String hangul){

    }

    @GetMapping("/device/{hangul}")
    public void deviceDashboard(@PathVariable String hangul){

    }

    @GetMapping("/browser/{hangul}")
    public void browserDashboard(@PathVariable String hangul){

    }

    @GetMapping("/ime/{hangul}")
    public void timeDashboard(@PathVariable String hangul){

    }

}
