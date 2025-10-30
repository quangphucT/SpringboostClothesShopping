package com.example.demo.repository.for_authentication;

import com.example.demo.entity.for_account.Account;
import com.example.demo.entity.for_account.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    OtpVerification findTopByAccountOrderByIdDesc(Account account);
}
