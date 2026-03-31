package com.southdragon.accounts.service;

import com.southdragon.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {
    /**
     * $DESCRIPTION$
     *
     * @param mobileNumber - mobileNumber Object
     * @return CustomerDetailsDto
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
