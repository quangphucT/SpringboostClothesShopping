package com.example.demo.entity.for_account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

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

    @Column(unique = true)
    String firebaseUid;


    // quản lý trạng thái
    @Schema(defaultValue = "true")
    Boolean isActive = true;
    @Schema(defaultValue = "false")
    Boolean isVerified = false;

    // Phân quyền
    @Enumerated(EnumType.STRING)
    Role role;




    // Audit fields
    Date createdAt;
    Date updatedAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
            @JsonIgnore
    List<OtpVerification> otpVerifications;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
            @JsonIgnore
    List<RefreshToken> refreshTokens;






    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    CustomerProfile customerProfile;










    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
