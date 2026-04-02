package com.southdragon.accounts.service.client;

import com.southdragon.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cards", fallback = CardsFallBack.class)
public interface CardsFeignClient {

    @GetMapping(value = "/api/fetch", consumes = "application/json")
    ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader(name = "south-dragon-correlation-id") String correlationId, @RequestParam String mobileNumber);

}
