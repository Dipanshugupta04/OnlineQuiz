package com.OnlineQuiz.OnlineQuiz.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;
@Service
public class participantService {

    @Autowired
    private roomIdRepository roomIdRepository;

  public int countByRoomCode(String roomCode) {
    Optional<RoomId> room = roomIdRepository.findByRoomCode(roomCode);
    return room.get().getParticipants().size();
}

}
