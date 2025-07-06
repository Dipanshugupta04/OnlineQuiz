package com.OnlineQuiz.OnlineQuiz.Reposistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;

@Repository
public interface roomIdRepository extends JpaRepository<RoomId, Long> {

    Optional<RoomId> findByRoomCode(String roomCode);

    Optional<RoomId> findByExamId(Long id);

    RoomId findByroomCode(String roomid);

    

    

}
