package com.OnlineQuiz.OnlineQuiz.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.DTO.ExamDTO;
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
import com.OnlineQuiz.OnlineQuiz.Service.ExamService;
import com.OnlineQuiz.OnlineQuiz.Service.QuizService;

@RestController

@RequestMapping("/quiz")
// Quiz Controller
public class QuizController {
    @Autowired
    private ExamService examService;
    // @Autowired
    // private SimpMessagingTemplate messagingTemplate;
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

    // Controller for create quiz
    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody QuizRequestDTO quizDTO) {
        return quizService.createQuiz(quizDTO);
    }

    // Controller for Generate Room id for quiz
    @PostMapping("/generate-room/{exam_Id}")
    public RoomId generateRoomId(@PathVariable Long exam_Id) {
        return quizService.generateRoomId(exam_Id);
    }

    // Controller for show the quiz question using roomcode
    @GetMapping("/questions/{roomCode}")
    public List<Question> getQuestionsByRoomCode(@PathVariable String roomCode) {
        return quizService.getQuestionsByRoomCode(roomCode);
    }

    // Controller for create exam details
     @PostMapping("/exam/create")
    public ResponseEntity<?> createExam(@RequestBody ExamDTO examDTO) {
        System.out.println(examDTO);
        try {
            Exam exam = examService.createExam(examDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("examId", exam.getId());
            response.put("startDateTime", exam.getStartDateTime());
            response.put("endDateTime", exam.getEndDateTime());
            response.put("examDescription", exam.getExamDescription());
            // response.put("roomid", exam.get)
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Controller for join room
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
            return ResponseEntity.status(404)
                    .body(Map.of("status", "error", "message", "User not found"));
        }
        // check status of the room is activated or not
        // if(roomid.status().equals("Active")){}

        RoomId room = roomOpt.get();
        User user = userOpt.get();

        // Check room status
        String status = room.getStatus();
        if ("EXPIRED".equalsIgnoreCase(status)) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "expired",
                    "message", "This quiz has expired and can no longer be joined.",
                    "roomCode", room.getRoomCode()));
        } else if (!"ACTIVE".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(Map.of(
                    "status", "waiting",
                    "message", "Quiz not yet delivered. Please wait.",
                    "roomCode", room.getRoomCode()));
        }

        // Check exam timing
        Exam exam = room.getExam();
        if (exam != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(exam.getStartDateTime())) {
                return ResponseEntity.status(403).body(Map.of(
                        "status", "not_started",
                        "message", "Exam has not started yet."));
            }
            if (now.isAfter(exam.getEndDateTime())) {
                return ResponseEntity.status(403).body(Map.of(
                        "status", "expired",
                        "message", "Exam has already ended."));
            }
        }

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

        response.put("quiz", quizService.getQuestionsByRoomCode(roomCode));
        // System.out.println(response);

        return ResponseEntity.ok(response);

    }

    // Controller for leave the quiz
    @PutMapping("/leave/{email}")
    public ResponseEntity<String> leaveRoom(@PathVariable String email) {
        System.out.println(email);
        Optional<Participant> userEmail = participantRepo.findByEmail(email);

        if (userEmail.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        Participant participant = userEmail.get();
        participantRepo.deleteById(participant.getId());

        return ResponseEntity.ok("Participant removed successfully");
    }

    // Controller for submit the user quiz answer response
    @PostMapping("/submit")
    public ResponseEntity<ResultDTO> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        ResultDTO result = quizService.evaluateSubmission(submission);
        return ResponseEntity.ok(result);
    }

    // Controller for testing
    @GetMapping("/home")
    public String homString() {
        return "this is home";
    }

}