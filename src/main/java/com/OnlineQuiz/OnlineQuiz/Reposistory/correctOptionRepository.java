package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.CorrectOption;
@Repository
public interface correctOptionRepository  extends JpaRepository<CorrectOption,Long>{

    Optional<CorrectOption> findByQuestionIdAndOptionId(Long questionId, Long selectedOptionId);

}
