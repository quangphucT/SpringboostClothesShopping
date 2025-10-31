package com.example.demo.api.for_authentication;
import com.example.demo.model.for_account.*;
import com.example.demo.service.for_authen.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin("*")
@SecurityRequirement(name = "api")

public class AuthenticationAPI {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("sign-up")
    public ResponseEntity createNewAccount(@Valid  @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse newAccount = authenticationService.createNewAccount(signUpRequest);
        Map<String, Object> newAcc = new LinkedHashMap<>();
        newAcc.put("message", "Account created successfully");
        newAcc.put("data", newAccount);
        return ResponseEntity.ok(newAcc);
    }

    @PostMapping("verify-otp")
    public ResponseEntity verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) {
         authenticationService.verifyOtp(verifyOtpRequest);
           Map<String, Object> newAcc = new LinkedHashMap<>();
           newAcc.put("message", "OTP verification successfully");
           return ResponseEntity.ok(newAcc);
    }


    @PostMapping("resend-otp")
    public ResponseEntity resendOtp(@RequestBody ResendOtpRequest resendOtpRequest){
        authenticationService.resendOtp(resendOtpRequest);
        Map<String, Object> newAcc = new LinkedHashMap<>();
        newAcc.put("message", "OTP resend successfully");
        return ResponseEntity.ok(newAcc);
    }
    @PostMapping("sign-in")
    public ResponseEntity signIn(@RequestBody SignInRequest signInRequest) {
        SignInResponse signInResponse = authenticationService.signIn(signInRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Sign in successfully");
        response.put("data", signInResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("forgot-password")
    public ResponseEntity forgotPassword(@RequestBody ForgotPassRequest forgotPassRequest) {
        authenticationService.forgotPassword(forgotPassRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Password forgot successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("reset-password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
              authenticationService.resetPassword(resetPasswordRequest);
              Map<String, Object> response = new LinkedHashMap<>();
              response.put("message", "Password reset successfully");
              return ResponseEntity.ok(response);
    }

    @PostMapping("google")
    public ResponseEntity loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        Map<String, Object> response = authenticationService.loginWithGoogle(request.getIdToken());
        return ResponseEntity.ok(response);
    }


    @PostMapping("refresh-token")
    public ResponseEntity refreshToken(@CookieValue("refreshToken") String refreshToken) {
         Map<String, Object> response =  authenticationService.refreshToken(refreshToken);
         return ResponseEntity.ok(response);
    }


    @PostMapping("logout")
    public ResponseEntity logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        authenticationService.logoutOfSystem(refreshToken);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("message", "Logout successfully");
             return ResponseEntity.ok(res);
    }
}
