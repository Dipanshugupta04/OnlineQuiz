package com.OnlineQuiz.OnlineQuiz.Reposistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
@Repository
public interface roomIdRepository  extends JpaRepository<RoomId ,Long>{

    static boolean existsByRoomCode(String newRoomCode) {
       
        throw new UnsupportedOperationException("Unimplemented method 'existsByRoomCode'");
    }

}
