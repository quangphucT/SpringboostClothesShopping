package com.example.demo.model.for_account;

import com.example.demo.entity.for_account.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    long id;
    String fullName;
    String email;
    String phone;
    Boolean isActive;
    Boolean isVerified;
    Role role;
    Date createdAt;
    Date updatedAt;
    String accessToken;
    String refreshToken;
    boolean requireSchoolLevel;
}
