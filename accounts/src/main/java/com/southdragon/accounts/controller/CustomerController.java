package com.southdragon.accounts.controller;

import com.southdragon.accounts.dto.CustomerDetailsDto;
import com.southdragon.accounts.dto.ErrorResponseDto;
import com.southdragon.accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CURD REST APIs for Customers in SouthDragon",
        description = "CURD REST APIs in SouthDragon to CREATE, UPDATE, FETCH AND DELETE Customers details")
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ICustomersService customersService;

    @Operation(summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details inside SouthDragon bank")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Http Status OK"),
                    @ApiResponse(responseCode = "500",description = "Http Status Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader(name = "south-dragon-correlation-id") String correlationId,
            @RequestParam
                                                                   @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                   String mobileNumber){
        logger.debug("fetchCustomerDetails method start");
        CustomerDetailsDto customerDetailsDto = customersService.fetchCustomerDetails(mobileNumber, correlationId);
        logger.debug("fetchCustomerDetails method end");
        return ResponseEntity.ok(customerDetailsDto);
    }
}
