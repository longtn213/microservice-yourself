package com.southdragon.accounts.controller;

import com.southdragon.accounts.constants.AccountsConstants;
import com.southdragon.accounts.dto.AccountContactInfoDto;
import com.southdragon.accounts.dto.CustomerDto;
import com.southdragon.accounts.dto.ErrorResponseDto;
import com.southdragon.accounts.dto.ResponseDto;
import com.southdragon.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "CURD REST APIs for Accounts in SouthDragon",
        description = "CURD REST APIs in SouthDragon to CREATE, UPDATE, FETCH AND DELETE accounts details")
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class AccountsController {

    private final IAccountsService accountsService;

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;

    private final AccountContactInfoDto accountContactInfoDto;

    @Operation(summary = "Create Account REST API",
            description = "REST API to create new Customer & Account inside SouthDragon bank")
    @ApiResponse(responseCode = "201",description = "Http Status CREATED")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccounts(@Valid @RequestBody CustomerDto customerDto){
        accountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
    }

    @Operation(summary = "Fetch Account REST API",
            description = "REST API to fetch Customer & Account inside SouthDragon bank")
    @ApiResponse(responseCode = "201",description = "Http Status OK")
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam
                                                                @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                String mobileNumber){
        CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);
        return ResponseEntity.ok(customerDto);
    }

    @Operation(summary = "Update Account REST API",
            description = "REST API to update Customer & Account inside SouthDragon bank")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Http Status OK"),
                    @ApiResponse(responseCode = "417",description = "Exception Failed"),
                    @ApiResponse(responseCode = "500",description = "Http Status Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountsDetail(@Valid @RequestBody CustomerDto customerDto){
        boolean isUpdated = accountsService.updateAccount(customerDto);
        if(isUpdated){
            return ResponseEntity.ok(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(summary = "Delete Account REST API",
            description = "REST API to delete new Customer & Account inside SouthDragon bank")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Http Status OK"),
                    @ApiResponse(responseCode = "417",description = "Exception Failed"),
                    @ApiResponse(responseCode = "500",
                            description = "Http Status Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountsDetail(@RequestParam
                                                                @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                String mobileNumber){
        boolean isDeleted = accountsService.deleteAccount(mobileNumber);
        if(isDeleted){
            return ResponseEntity.ok(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_DELETE ));
        }
    }

    @Operation(summary = "Get Build Information",
            description = "Get Build Information that is deployed into accounts microservice")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Http Status OK"),
                    @ApiResponse(responseCode = "417",description = "Exception Failed"),
                    @ApiResponse(responseCode = "500",description = "Http Status Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){
        return ResponseEntity.ok(buildVersion);
    }

    @Operation(summary = "Get Java version",
            description = "Get Java versions details that is installed into accounts microservice")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Http Status OK"),
                    @ApiResponse(responseCode = "417",description = "Exception Failed"),
                    @ApiResponse(responseCode = "500",description = "Http Status Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.ok(environment.getProperty("M2_HOME"));
    }

    @Operation(summary = "Get Contact Info",
            description = "Get Contact Info details that can be reached out in case of any issues")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Http Status OK"),
                    @ApiResponse(responseCode = "417",description = "Exception Failed"),
                    @ApiResponse(responseCode = "500",description = "Http Status Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<AccountContactInfoDto> getContactInfo(){
        return ResponseEntity.ok(accountContactInfoDto);
    }
}
