package com.southdragon.accounts.service;

import com.southdragon.accounts.dto.CustomerDto;

public interface IAccountsService {
    /**
     * Create an account for a customer
     *
     * @param customerDto - customerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     * fetch account details for a customer
     *
     * @param mobileNumber - mobileNumber Object
     * @return CustomerDto
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     * update account details for a customer
     *
     * @param customerDto - customerDto Object
     * @return boolean
     */
    boolean updateAccount(CustomerDto customerDto);


    /**
     * $DESCRIPTION$
     *
     * @param mobileNumber - mobileNumber Object
     * @return boolean
     */
    boolean deleteAccount(String mobileNumber);
}
