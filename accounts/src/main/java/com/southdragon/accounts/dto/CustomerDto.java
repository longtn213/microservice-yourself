package com.southdragon.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Customer", description = "Schema to hold Customer and Account information")
public class CustomerDto {

    @Schema(name = "Name of the customer", example = "South Dragon")
    @NotEmpty(message = "Name can not be a null or empty")
    @Size(min = 2, max = 30, message = "The length of the customer name should be between 2 and 30 characters")
    private String name;

    @Schema(name = "Email Address of the customer", example = "SouthDragon@gmail.com")
    @NotEmpty(message = "The email address can not be a null or empty")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(name = "Mobile Number of the customer", example = "0123454911")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(description = "Account details of the Customer")
    private AccountsDto accountsDto;
}
