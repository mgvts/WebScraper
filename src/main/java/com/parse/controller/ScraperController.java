package com.parse.controller;

import com.parse.domain.ResponseDTO;
import com.parse.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = "/")
public class ScraperController {

    @Autowired
    ScraperService scraperService;

    @GetMapping(path = "/{u}")
    public Set<ResponseDTO> getVehicleByModel(@PathVariable String u) {
        System.out.println("ScraperController");
        return  scraperService.getVehicleByModel(u);
    }
}
