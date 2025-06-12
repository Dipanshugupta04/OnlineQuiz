package com.OnlineQuiz.OnlineQuiz.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PutMapping("/update-by-question/{roomCode}")
    public ResponseEntity<?> updateQuizByRoomCode(@PathVariable String roomCode, @RequestBody QuizRequestDTO quizDTO) {
        try {
            quizService.updateQuizByRoomCode(roomCode, quizDTO);
            return ResponseEntity.ok("Quiz updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
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
    //Update exma Data
    @PutMapping("/update-by-room/{roomCode}") // Using PUT for updates
    public ResponseEntity<?> updateExamByRoomCode(@PathVariable String roomCode, @RequestBody ExamDTO examDTO) {
        if (roomCode == null || roomCode.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Room code cannot be empty."));
        }
        if (examDTO == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Exam data cannot be empty."));
        }

        try {
            Exam updatedExam = examService.updateExam(roomCode, examDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Exam updated successfully.");
            response.put("examId", updatedExam.getId());
            response.put("examDescription", updatedExam.getExamDescription());
            response.put("startDateTime", updatedExam.getStartDateTime());
            response.put("endDateTime", updatedExam.getEndDateTime());

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            // Catches exceptions thrown by the service (e.g., NOT_FOUND)
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("status", "error", "message", e.getReason()));
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("status", "error", "message", "An unexpected error occurred: " + e.getMessage()));
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
        // Exam exam = room.getExam();
        // if (exam != null) {
        //     LocalDateTime now = LocalDateTime.now();
        //     if (now.isBefore(exam.getStartDateTime())) {
        //         return ResponseEntity.status(403).body(Map.of(
        //                 "status", "not_started",
        //                 "message", "Exam has not started yet."));
        //     }
        //     if (now.isAfter(exam.getEndDateTime())) {
        //         return ResponseEntity.status(403).body(Map.of(
        //                 "status", "expired",
        //                 "message", "Exam has already ended."));
        //     }
        // }

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