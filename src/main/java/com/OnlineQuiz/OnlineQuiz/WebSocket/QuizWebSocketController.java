package com.OnlineQuiz.OnlineQuiz.WebSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;
import com.OnlineQuiz.OnlineQuiz.Service.QuizService;

import java.util.Map;
import java.util.Optional;

@RestController
public class QuizWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private roomIdRepository roomRepository;

    @Autowired
    private QuizService quizService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendQuiz(@RequestBody Map<String, String> request) {
        String roomCode = request.get("roomCode");

        Optional<RoomId> roomOpt = roomRepository.findByRoomCode(roomCode);
        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Room not found"));
        }

        RoomId room = roomOpt.get();
        Quiz quiz = room.getQuiz();

        if (quiz == null) {
            return ResponseEntity.status(400).body(Map.of("status", "error", "message", "No quiz attached to room"));
        }

        room.setStatus("ACTIVE");
        roomRepository.save(room);

        // Send WebSocket message to all users subscribed to this room's topic
        messagingTemplate.convertAndSend("/topic/quiz/" + roomCode, Map.of(
                "status", "ACTIVE",
                "message", "Quiz is now live",
                "quiz", quizService.getQuestionsByRoomCode(roomCode)));

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Quiz delivered and WebSocket message sent",
                "roomCode", room.getRoomCode()));
    }
}
