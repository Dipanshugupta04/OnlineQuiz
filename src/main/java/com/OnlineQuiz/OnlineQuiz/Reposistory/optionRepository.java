package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Option;

@Repository
public interface optionRepository extends JpaRepository<Option, Long> {

}
