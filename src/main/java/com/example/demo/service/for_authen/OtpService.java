package com.example.demo.service.for_authen;

import com.example.demo.entity.for_account.Account;
import com.example.demo.entity.for_account.OtpVerification;
import com.example.demo.model.for_account.EmailDetails;
import com.example.demo.repository.for_authentication.AuthenticationRepository;
import com.example.demo.repository.for_authentication.OtpVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    OtpVerificationRepository  otpVerificationRepository;
     @Autowired
     AuthenticationRepository authenticationRepository;
     @Autowired
     EmailService emailService;
    private final Random random = new Random();
    // Hàm tạo OTP
    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // 6 chữ số
        return String.valueOf(otp);
    }

    // Lưu OTP vào DB
    public void saveOtp(Account account, String otp) {
        OtpVerification entity = new OtpVerification();
        entity.setOtpCode(otp);
        entity.setExpiryTime(new Date(System.currentTimeMillis() + 1 * 60 * 1000));
        entity.setEmail(account.getEmail());
        entity.setAccount(account);
        entity.setUsed(false);
        otpVerificationRepository.save(entity);
    }
    // Hàm xác thực OTP
    public boolean validateOtp(Account account, String otpInput) {

        // Lấy OTP mới nhất của account
        OtpVerification otp = otpVerificationRepository.findTopByAccountOrderByIdDesc(account);

        if (otp == null) {
            throw new RuntimeException("Không tìm thấy OTP nào, vui lòng thử lại");
        }

        // Kiểm tra đã dùng chưa
        if (otp.getUsed()) {
            throw new RuntimeException("OTP này đã được sử dụng");
        }

        // Kiểm tra thời hạn
        if (otp.getExpiryTime().before(new Date())) {
            throw new RuntimeException("OTP đã hết hạn");
        }

        // Kiểm tra đúng mã
        if (!otp.getOtpCode().equals(otpInput)) {
            throw new RuntimeException("OTP không chính xác");
        }

        // Nếu hợp lệ → đánh dấu đã dùng
        otp.setUsed(true);
        otpVerificationRepository.save(otp);
        account.setIsVerified(true);
        authenticationRepository.save(account);
        return true;
    }
    // resend
    public void resendOtp(Account account) {
        // Sinh OTP mới
        String otpCode = generateOtp();
        Date expiryTime = new Date(System.currentTimeMillis() + 1 * 60 * 1000);

        // Lưu OTP mới
        OtpVerification otp = new OtpVerification();
        otp.setEmail(account.getEmail());
        otp.setOtpCode(otpCode);
        otp.setExpiryTime(expiryTime);
        otp.setUsed(false);
        otp.setAccount(account);
        otpVerificationRepository.save(otp);

        // Gửi email OTP
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setReceiver(account);
        emailDetails.setSubject("Your new OTP Code for Account Verification");
        emailDetails.setOtpCode(otpCode);
        emailDetails.setExpiryTime(expiryTime);

        emailService.sendOtpMail(emailDetails);
    }
}
