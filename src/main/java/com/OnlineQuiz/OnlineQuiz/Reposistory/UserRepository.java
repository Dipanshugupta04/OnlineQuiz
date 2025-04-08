package com.OnlineQuiz.OnlineQuiz.Reposistory;

import com.OnlineQuiz.OnlineQuiz.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // correct method name

}
