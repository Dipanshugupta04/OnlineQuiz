package com.OnlineQuiz.OnlineQuiz.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.OtpVerificationRequest;
import com.OnlineQuiz.OnlineQuiz.Entity.OtpVerification;
import com.OnlineQuiz.OnlineQuiz.Reposistory.OtpVerificationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Autowired
    private OtpVerificationRepository otpRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public String generateOtp() {
        String otp = RandomStringUtils.randomNumeric(6);
        return otp;
    }

    public ResponseEntity<?> sendOtpToEmail(String email, String otp) {
     try{
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Email OTP for verification");
        mail.setText("OTP IS :"+otp);
        javaMailSender.send(mail);

        return ResponseEntity.ok("OTP sent successfully");
    }
    catch(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error sending OTP: " + e.getMessage());
    }
    }
    public ResponseEntity<?> createAndSendOtp(String emails) {
        System.out.println("send email is :"+emails);
         String email = emails.replace("\"", "").replace("'", "").trim().toLowerCase();
        Optional<OtpVerification> existing = otpRepository.findByEmail(email);

        if (existing.isPresent() && existing.get().getExpiryTime().isAfter(LocalDateTime.now().minusSeconds(30))) {
            throw new IllegalStateException("Please wait before requesting OTP again.");
        }

        String otp = generateOtp();
        OtpVerification otpRecord=new OtpVerification();

        otpRecord.setEmail(email.trim().toLowerCase());
        otpRecord.setOtp(otp);
        otpRecord.setExpiryTime(LocalDateTime.now().plusMinutes(2));

        otpRepository.save(otpRecord);
        System.out.println(otpRecord.getOtp());
        sendOtpToEmail(email, otp);
        return ResponseEntity.ok("OTP Save successfully");
    }
    public ResponseEntity<Boolean> verifyOtp(OtpVerificationRequest request) {
        System.out.println("Verifying OTP for: " + request.getEmail());
        String email=request.getEmail();
        System.out.println("email is:"+email);
        
        Optional<OtpVerification> record = otpRepository.findByEmail(email);
        if (record.isEmpty()) {
            System.out.println("No record found for this email.");
            return ResponseEntity.ok(false);
        }
    
        OtpVerification otpRecord = record.get();
        System.out.println("Expected OTP: " + otpRecord.getOtp());
        System.out.println("Provided OTP: " + request.getOtp());
        System.out.println("Expiry: " + otpRecord.getExpiryTime() + ", Now: " + LocalDateTime.now());
    
        if (otpRecord.getOtp().trim().equals(request.getOtp().trim()) &&
            otpRecord.getExpiryTime().isAfter(LocalDateTime.now())) {
    
            otpRepository.delete(otpRecord); // Invalidate after use
            System.out.println("OTP verified successfully.");
            return ResponseEntity.ok(true);
        }
    
        System.out.println("OTP verification failed.");
        return ResponseEntity.ok(false);
    }
    
}
