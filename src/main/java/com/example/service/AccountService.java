package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*Register users*/
    public Optional<Account> registerAccount(String username, String password) {

        if (accountRepository.findByUsername(username).isPresent()) {
            return Optional.empty(); // Username already exists
        }

        if (username == null || username.trim().isEmpty() || password == null || password.length() < 4) {
            return Optional.empty(); // Invalid input
        }

        Account newAccount = new Account(username, password);
        return Optional.of(accountRepository.save(newAccount));
    }

    /*Login Users*/
    public Optional<Account> loginUser(String username, String password){
        Optional<Account> account = accountRepository.findByUsername(username);

        if (!account.isPresent()) {
            return Optional.empty(); // User not found
        }

        if (!account.get().getPassword().equals(password)) {
            return Optional.empty(); // Incorrect password
        }

        return account; // Successful login
    }
}