package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.User;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByUserId(Long id);

  

   



    

}
