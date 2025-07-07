package com.OnlineQuiz.OnlineQuiz.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.EditProfileRequest;
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
            user.setcontactNo(request.getcontactNo());
        }
        userRepository.save(user);
        return "Profile updated successfully!";
    }
}
