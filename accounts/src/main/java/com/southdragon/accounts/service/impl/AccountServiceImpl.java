package com.southdragon.accounts.service.impl;

import com.southdragon.accounts.constants.AccountsConstants;
import com.southdragon.accounts.dto.AccountsDto;
import com.southdragon.accounts.dto.CustomerDto;
import com.southdragon.accounts.entity.Accounts;
import com.southdragon.accounts.entity.Customer;
import com.southdragon.accounts.exception.CustomerAlreadyExistsException;
import com.southdragon.accounts.exception.ResourceNotFoundException;
import com.southdragon.accounts.mapper.AccountsMapper;
import com.southdragon.accounts.mapper.CustomerMapper;
import com.southdragon.accounts.repository.AccountsRepository;
import com.southdragon.accounts.repository.CustomerRepository;
import com.southdragon.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    /**
     * @param customerDto - customerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(new Customer(),customerDto);
        customerRepository
                .findByMobileNumber(customer.getMobileNumber())
                .ifPresent(c -> {
                    throw new CustomerAlreadyExistsException(
                            "Customer already registered with given mobile number " + customerDto.getMobileNumber()
                    );
                });
        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param mobileNumber - mobileNumber Object
     * @return CustomerDto
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customer", customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        return customerDto;
    }

    /**
     * @param customerDto - customerDto Object
     * @return boolean
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        if(customerDto.getAccountsDto() != null){
            Accounts accounts = accountsRepository.findById(customerDto.getAccountsDto().getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", customerDto.getAccountsDto().getAccountNumber().toString()));

            Accounts updatedAccount = AccountsMapper.mapToAccounts(accounts,customerDto.getAccountsDto());
            accountsRepository.save(updatedAccount);

            Customer customer = customerRepository.findById(updatedAccount.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", updatedAccount.getCustomerId().toString()));

            Customer updatedCustomer = CustomerMapper.mapToCustomer(customer,customerDto);
            customerRepository.save(updatedCustomer);

            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * create a new account for a customer
     *
     * @param customer - customer Object
     * @return Accounts
     */
    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());

        long randomAccountNumber = 100000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }
}
