package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OnlineQuiz.OnlineQuiz.Entity.Participant;

public interface ParticipantRepo  extends JpaRepository<Participant,Long>{

    
}
