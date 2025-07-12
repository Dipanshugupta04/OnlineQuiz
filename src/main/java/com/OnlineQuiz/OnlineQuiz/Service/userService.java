package com.OnlineQuiz.OnlineQuiz.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.EditProfileRequest;
import com.OnlineQuiz.OnlineQuiz.DTO.PasswordResetRequest;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;

@Service
public class userService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String updateProfile(String email, EditProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());

       
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("Password and confirm password do not match");
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        System.out.println(request.getcontactNo()+"===============");
        if(request.getcontactNo()!=null && !request.getcontactNo().isBlank()){
            user.setContactNo(request.getcontactNo());
        }
        userRepository.save(user);
        return "Profile updated successfully!";
    }

    public void updateProfiles(String email, PasswordResetRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        

       
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("Password and confirm password do not match");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setConfirmPassword(passwordEncoder.encode(request.getNewPassword()));
        }
       
        userRepository.save(user);
        
    }

    
}
