package com.parse.service;

import com.parse.domain.ResponseDTO;

import java.util.Set;

public interface ScraperService {
    Set<ResponseDTO> getVehicleByModel(String vehicleModel);
}
