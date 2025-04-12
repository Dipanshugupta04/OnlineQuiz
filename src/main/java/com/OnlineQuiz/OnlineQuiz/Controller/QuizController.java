package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizSubmissionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.ResultDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Participant;
import com.OnlineQuiz.OnlineQuiz.Entity.Question;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ParticipantRepo;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;
import com.OnlineQuiz.OnlineQuiz.Service.QuizService;

@RestController
@RequestMapping("/quiz")
// Quiz Controller
public class QuizController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizService quizService;
    @Autowired
    private roomIdRepository roomRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ParticipantRepo participantRepo;

    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody QuizRequestDTO quizDTO) {
        return quizService.createQuiz(quizDTO);
    }

    @PostMapping("/generate-room/{quiz_Id}")
    public RoomId generateRoomId(@PathVariable Long quiz_Id) {
        return quizService.generateRoomId(quiz_Id);
    }

    @GetMapping("/questions/{roomCode}")
    public List<Question> getQuestionsByRoomCode(@PathVariable String roomCode) {
        return quizService.getQuestionsByRoomCode(roomCode);
    }

    @PostMapping("/exam/create")
    public Exam createExam(@RequestBody Exam exam) {
        return examRepository.save(exam);

    }

    @PostMapping("/join-room")
    public ResponseEntity<Map<String, Object>> joinQuiz(@RequestBody Map<String, String> request) {
        String roomCode = request.get("roomCode");
        String email = request.get("email");

        Optional<RoomId> roomOpt = roomRepository.findByRoomCode(roomCode);
        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(404)
            .body(Map.of("status", "error", "message", "Room Code Is Not Match"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "User not found"));
        }

        RoomId room = roomOpt.get();
        User user = userOpt.get();

        // Optional: prevent duplicate joining
        boolean alreadyJoined = room.getParticipants().stream()
                .anyMatch(p -> p.getParticipantName().equals(user.getName()));
        if (alreadyJoined) {
            return ResponseEntity
                    .ok(Map.of("status", "info", "message", "Already joined", "roomCode", room.getRoomCode()));
        }

        Participant participant = new Participant();
        participant.setParticipantName(user.getName());
        participant.setParticipantEmail(user.getEmail());
        participant.setRoom(room);

        participantRepo.save(participant);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Joined quiz successfully");
        response.put("roomCode", room.getRoomCode());
        response.put("participantName", user.getName());

        return ResponseEntity.ok(response);

    }

    @PostMapping("/submit")
    public ResponseEntity<ResultDTO> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        ResultDTO result = quizService.evaluateSubmission(submission);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/home")
    public String homString() {
        return "this is home";
    }

}
