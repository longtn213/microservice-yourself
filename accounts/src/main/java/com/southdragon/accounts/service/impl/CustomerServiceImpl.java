package com.southdragon.accounts.service.impl;

import com.southdragon.accounts.dto.AccountsDto;
import com.southdragon.accounts.dto.CardsDto;
import com.southdragon.accounts.dto.CustomerDetailsDto;
import com.southdragon.accounts.dto.LoansDto;
import com.southdragon.accounts.entity.Accounts;
import com.southdragon.accounts.entity.Customer;
import com.southdragon.accounts.exception.ResourceNotFoundException;
import com.southdragon.accounts.mapper.AccountsMapper;
import com.southdragon.accounts.mapper.CustomerMapper;
import com.southdragon.accounts.repository.AccountsRepository;
import com.southdragon.accounts.repository.CustomerRepository;
import com.southdragon.accounts.service.ICustomersService;
import com.southdragon.accounts.service.client.CardsFeignClient;
import com.southdragon.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomersService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - mobileNumber Object
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber , String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customer", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId,mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId,mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
