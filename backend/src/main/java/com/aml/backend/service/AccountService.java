package com.aml.backend.service;

import com.aml.backend.model.Account;
import com.aml.backend.model.User;
import com.aml.backend.repository.AccountRepository;
import com.aml.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Account saveAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        // Save the account first
        Account savedAccount = accountRepository.save(account);

        // Update the associated user if nationalId is provided
        if (account.getUser() != null && account.getUser().getNationalId() != null) {
            String nationalId = account.getUser().getNationalId();
            User user = userRepository.findByNationalId(nationalId);
            if (user != null) {
                user.setAccountNumber(savedAccount.getAccountNumber()); // Set after account is saved
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("User not found with nationalId: " + nationalId);
            }
        }

        return savedAccount;
    }

    public Account getAccountById(Long id) {
        return null; // Implement as needed
    }

    public void deleteAccount(Long id) {
        // Implement as needed
    }

    public List<Account> getAllAccounts() {
        return null; // Implement as needed
    }
}