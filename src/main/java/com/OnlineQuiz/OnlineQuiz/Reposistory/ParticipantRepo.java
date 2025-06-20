package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.Participant;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;

@Repository
public interface ParticipantRepo extends JpaRepository<Participant, Long> {
  

    // Option 1: Query by RoomId object
    List<Participant> findByRoom(RoomId room);

    // Option 2: Query by room code using explicit JPQL
    @Query("SELECT p FROM Participant p WHERE p.room.roomCode = :roomCode")
    List<Participant> findByRoomCode(@Param("roomCode") String roomCode);

    // Option 3: If you specifically need Map results
    @Query("SELECT new map(p.id as id, p.participantName as name, p.email as email) " +
           "FROM Participant p WHERE p.room.roomCode = :roomCode")
    List<Map<String, Object>> findByroom_id(@Param("roomCode") String roomCode);

    boolean existsByEmailAndRoom(String email, RoomId room);

    Optional<Participant> findByEmail(String participantEmail);
}