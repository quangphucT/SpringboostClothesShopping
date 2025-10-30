package com.example.demo.service.for_authen;

import com.example.demo.entity.for_account.Account;
import com.example.demo.entity.for_account.CustomerProfile;
import com.example.demo.entity.for_account.RefreshToken;
import com.example.demo.entity.for_account.Role;
import com.example.demo.entity.for_order.Cart;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.TokenExpiredException;
import com.example.demo.exception.TokenRevokedException;
import com.example.demo.model.for_account.*;
import com.example.demo.repository.for_authentication.AuthenticationRepository;
import com.example.demo.repository.for_customer.CustomerRepository;
import com.example.demo.repository.for_authentication.RefreshTokenRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    OtpService otpService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;



    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    CustomerRepository customerRepository;

    public SignUpResponse createNewAccount(SignUpRequest signUpRequest) {
        Account account = new Account();
        try {
            account.setFullName(signUpRequest.getFullName());
            account.setEmail(signUpRequest.getEmail());
            account.setPhone(signUpRequest.getPhone());
            account.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            account.setRole(signUpRequest.getRole());
            account.setCreatedAt(new Date());
            account.setUpdatedAt(new Date());
           if("CUSTOMER".equalsIgnoreCase(String.valueOf(account.getRole()))) {
               CustomerProfile customerProfile = new CustomerProfile();
               customerProfile.setGender(signUpRequest.getGender());
               customerProfile.setDateOfBirth(signUpRequest.getDateOfBirth());
               customerProfile.setCreatedAt(new Date());
               customerProfile.setUpdatedAt(new Date());
               customerProfile.setAccount(account);
               account.setCustomerProfile(customerProfile);
               Cart cart = new Cart();
               cart.setCreatedAt(new Date());
               cart.setCustomerProfile(account.getCustomerProfile());
               customerProfile.setCart(cart);
           }
           Account newAccount = authenticationRepository.save(account);

           // tạo otp
            String otpCode = otpService.generateOtp();
            long expiryMillis = System.currentTimeMillis() + (60 * 1000);
            // lưu OTP vào DB
            otpService.saveOtp(newAccount, otpCode);

            // send mail
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setReceiver(newAccount);
            emailDetails.setSubject("Your OTP Code for Account Verification");
            emailDetails.setOtpCode(otpCode);
            emailDetails.setExpiryTime(new Date(expiryMillis));
            emailService.sendOtpMail(emailDetails);
           return modelMapper.map(newAccount, SignUpResponse.class);

        } catch (Exception e) {
            if(e.getMessage().contains(account.getEmail())) {
                throw new DuplicationException("Email already exists");
            }else{
                throw new DuplicationException("Phone already exists");
            }
        }
    }
    public void verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        Account account = authenticationRepository.findAccountByEmail(verifyOtpRequest.getEmail());

        if (account == null) {
            throw new NotFoundException("Account not found with email: " + verifyOtpRequest.getEmail());
        }

        // validate OTP - ở đây nếu OTP sai, hết hạn... thì ném exception riêng
        otpService.validateOtp(account, verifyOtpRequest.getOtpInput());
    }
    public void resendOtp(ResendOtpRequest resendOtpRequest) {
        Account account  = authenticationRepository.findAccountByEmail(resendOtpRequest.getEmail());
        if (account == null) {
            throw new NotFoundException("Account not found with email: " + resendOtpRequest.getEmail());
        }
        otpService.resendOtp(account);
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            Account account = (Account) authentication.getPrincipal();

            String accessToken = tokenService.generateAccessToken(account);
            String refreshToken = tokenService.generateRefreshToken(account);

            SignInResponse  signInResponse = modelMapper.map(account, SignInResponse.class);

            signInResponse.setAccessToken(accessToken);
            signInResponse.setRefreshToken(refreshToken);
            return signInResponse;
        } catch (Exception e) {
            throw  new EntityNotFoundException("Email or Password is incorrect");
        }
    }

    public void forgotPassword(ForgotPassRequest forgotPassRequest) {
        Account account = authenticationRepository.findAccountByEmail(forgotPassRequest.getEmail());
        try {
            String accessToken = tokenService.generateAccessToken(account);
            EmailForgotPasswordDetails emailForgotPasswordDetails = new EmailForgotPasswordDetails();
            emailForgotPasswordDetails.setReceiver(account);
            emailForgotPasswordDetails.setSubject("Forgot Password");
            emailForgotPasswordDetails.setBody("http://localhost:8080/swagger-ui/index.html?token=" + accessToken);
             emailService.sendLinkResetPasswordPageMail(emailForgotPasswordDetails);
        } catch (Exception e) {
            throw new NotFoundException("Account not found with email: " + forgotPassRequest.getEmail());
        }
    }
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account account = getCurrentAccount();
        try {
            account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
            authenticationRepository.save(account);
        } catch (Exception e) {
            throw new NotFoundException("Account not found");
        }
    }

    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return account;
    }


   @Transactional
    public Map<String, Object> loginWithGoogle(String idToken) {
        try {
            // 1. Verify Firebase ID Token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String uid = decodedToken.getUid();

            // 2. Kiểm tra user trong DB
            Account account = authenticationRepository.findAccountByEmail(email);

            if (account == null) {
                // 3. Nếu chưa có thì tạo mới
                account = new Account();
                account.setEmail(email);
                account.setFullName(name);
                account.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                account.setFirebaseUid(uid);
                account.setIsVerified(true);
                account.setCreatedAt(new Date());
                account.setUpdatedAt(new Date());
                account.setRole(Role.CUSTOMER);
                authenticationRepository.save(account);

                // 3.1. Tạo learner profile tương ứng
                CustomerProfile profile = new CustomerProfile();
                profile.setAccount(account);   // set quan hệ 1-1
                customerRepository.save(profile);

            } else {
                if (account.getFirebaseUid() == null) {
                    // Liên kết UID Firebase vào account hiện tại
                    account.setFirebaseUid(uid);
                    authenticationRepository.save(account);

                }


            }
            String accessToken = tokenService.generateAccessToken(account);
            String refreshToken = tokenService.generateRefreshToken(account);


            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "message", "Login successfully"
            );

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid Firebase ID token", e);
        }
    }



    public Map<String, Object> refreshToken(String refreshToken) {


        // 1. Kiểm tra token có tồn tại trong DB không
        RefreshToken tokenInDb = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh token not found!"));

        // 2. Kiểm tra token có bị thu hồi hoặc hết hạn không
        if (tokenInDb.isRevoked()) {
            throw new TokenRevokedException("Token revoked");
        }
        if (tokenInDb.getExpiredAt().before(new Date())){
            throw new  TokenExpiredException("Token expired");
        }
        // 3. Lấy Account từ refreshToken (tuỳ theo cách bạn ánh xạ)
        Account acc = tokenInDb.getAccount(); // nếu có quan hệ @ManyToOne giữa RefreshToken và Account

        // 4. Tạo accessToken mới
        String newAccessToken = tokenService.generateAccessToken(acc);

        // 5. Trả về accessToken mới + refreshToken cũ
        Map<String, Object> response = new HashMap<>();

        response.put("accessToken", newAccessToken);
        response.put("refreshToken", refreshToken);
        return response;
    }

    public void logoutOfSystem(String req) {
            RefreshToken refreshTokenInDB = tokenService.verifyRefreshToken(req);
            refreshTokenInDB.setRevoked(true);
            refreshTokenRepository.save(refreshTokenInDB);

    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authenticationRepository.findAccountByEmail(email);
    }
}
