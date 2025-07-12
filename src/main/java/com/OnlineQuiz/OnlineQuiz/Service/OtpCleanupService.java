package com.OnlineQuiz.OnlineQuiz.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.Entity.OtpVerification;
import com.OnlineQuiz.OnlineQuiz.Reposistory.OtpVerificationRepository;

@Service
public class OtpCleanupService {

    @Autowired
    private OtpVerificationRepository otpRepository;

    @Scheduled(fixedRate = 180000) // runs every 180 seconds
    public void deleteExpiredOtps() {
        List<OtpVerification> expiredOtps = otpRepository.findAll().stream()
                .filter(otp -> otp.getExpiryTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (!expiredOtps.isEmpty()) {
            System.out.println("deleted otp is"+ expiredOtps.get(0).getOtp());
            otpRepository.deleteAll(expiredOtps);
            System.out.println("Expired OTPs deleted: " + expiredOtps.size());
        }
    }
}
