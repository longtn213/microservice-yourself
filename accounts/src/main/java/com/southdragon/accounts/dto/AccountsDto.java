package com.southdragon.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Account", description = "Schema to hold Account information")
public class AccountsDto {

    @NotEmpty(message = "Account number can not be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account Number must be 10 digits")
    @Schema( description = "Account number of south dragon bank")
    private Long accountNumber;

    @NotEmpty(message = "AccountType can not be a null or empty")
    @Schema( description = "Account type of south dragon bank" , example = "SAVINGS")

    private String accountType;

    @NotEmpty(message = "BranchAddress can not be a null or empty")
    @Schema( description = "South Dragon Bank Branch Address")
    private String branchAddress;
}
