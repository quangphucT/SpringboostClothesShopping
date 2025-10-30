package com.example.demo.entity.for_account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otp_verification")
public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String email;
    String otpCode;
    Date expiryTime;
    @Schema(defaultValue = "false")
    Boolean used = false;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;
}
