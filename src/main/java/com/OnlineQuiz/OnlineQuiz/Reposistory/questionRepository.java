package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Question;

@Repository
public interface questionRepository extends JpaRepository<Question, Long> {

    List<Question> findByquiz_id(Long id);

}
