package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.OtpVerification;
@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification,Long> {

    Optional<OtpVerification> findByEmail(String email);

    
} 