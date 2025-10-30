package com.example.demo.repository.for_authentication;

import com.example.demo.entity.for_account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Account, Long> {
      Account findAccountByEmail(String email);
      Account findAccountById(Long id);
}
