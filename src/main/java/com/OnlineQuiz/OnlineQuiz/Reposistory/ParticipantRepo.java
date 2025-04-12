package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Participant;
@Repository
public interface ParticipantRepo  extends JpaRepository<Participant,Long>{

    Optional<Participant> findByEmail(String email);



    
}
