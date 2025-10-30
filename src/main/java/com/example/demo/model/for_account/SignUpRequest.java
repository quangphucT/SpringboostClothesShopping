package com.example.demo.model.for_account;

import com.example.demo.entity.for_account.Gender;
import com.example.demo.entity.for_account.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    // thông tin cá nhân cơ bản
    @NotBlank(message = "Full name must not be blank!")
    String fullName;
    @NotBlank(message = "Email must not be blank!")
    @Email(message = "Email is invalid!")
    @Column(unique = true)
    String email;

    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Phone is invalid!")
    @Column(unique = true)
    String phone;
    @NotBlank(message = "Password must not be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    String password;
    Role role;
    Gender gender;
    Date dateOfBirth;

}
