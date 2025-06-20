package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.CorrectOption;

@Repository
public interface correctOptionRepository extends JpaRepository<CorrectOption, Long> {

    // ✅ This is used when checking selected answer is correct
    Optional<CorrectOption> findByQuestionIdAndOptionId(Long questionId, Long selectedOptionId);

    // ✅ This is the one used to fetch the correct answer for a question
    Optional<CorrectOption> findByQuestionId(Long questionId);
}
