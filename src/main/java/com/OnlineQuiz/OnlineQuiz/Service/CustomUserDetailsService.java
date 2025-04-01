package com.OnlineQuiz.OnlineQuiz.Service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.Entity.User;

 // Adjust according to your package structure

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository userRepository;

    public CustomUserDetailsService(com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username) // Assuming users are identified by email
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return (UserDetails) user; // Ensure User implements UserDetails
    }
}
