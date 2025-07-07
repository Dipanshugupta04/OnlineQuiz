package com.OnlineQuiz.OnlineQuiz.Controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.OnlineQuiz.OnlineQuiz.DTO.AnswerSubmissionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.ExamDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizSubmissionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.ResultDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.CorrectOption;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Option;
import com.OnlineQuiz.OnlineQuiz.Entity.Participant;
import com.OnlineQuiz.OnlineQuiz.Entity.Question;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.Result;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Exception.ResourceNotFoundException;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ParticipantRepo;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ResultRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.correctOptionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.questionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.quizRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;
import com.OnlineQuiz.OnlineQuiz.Service.ExamService;
import com.OnlineQuiz.OnlineQuiz.Service.QuizService;
import com.OnlineQuiz.OnlineQuiz.Service.participantService;

import io.jsonwebtoken.Jwts;

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
    @Autowired
    private questionRepository questionRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private quizRepository quizRepository;
    @Autowired
    private correctOptionRepository correctOptionRepository;
    @Autowired
    private participantService participantService;
    @Autowired
    private roomIdRepository roomIdRepository;

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
        return (List<Question>) quizService.getQuestionsByRoomCode(roomCode);
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

    // Update exma Data
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
        try {
            String roomCode = request.get("roomCode");
            String email = request.get("email");

            // Input validation
            if (roomCode == null || email == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", "error", "message", "Room code and email are required"));
            }

            // Find room and user
            RoomId room = roomRepository.findByRoomCode(roomCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with code: " + roomCode));

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            // Check if already joined
            boolean alreadyJoined = participantRepo.existsByEmailAndRoom(email, room);

            if (alreadyJoined) {
                return ResponseEntity.ok(Map.of(
                        "status", "info",
                        "message", "Already joined/You have completed your exam?",
                        "roomCode", room.getRoomCode()));
            }

            // Create and save new participant
            Participant participant = new Participant();
            participant.setParticipantName(user.getName());
            participant.setEmail(email);
            participant.setRoom(room);
            participantRepo.save(participant);

            // Get quiz data from service
            Map<String, Object> quizData = quizService.getQuestionsByRoomCode(roomCode);
            System.out.println("exam Start date is ------" + room.getExam().getStartDateTime());

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Joined quiz successfully");
            response.put("roomCode", roomCode);
            response.put("participantName", user.getName());
            response.put("quiz", quizData);
            response.put("startdate", room.getExam().getStartDateTime());
            response.put("durationTime", room.getExam().getDurationMinutes());

            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An error occurred while joining the quiz"));
        }
    }

    // Controller for leave the quiz
    @DeleteMapping("/leave/{roomCode}")
    public ResponseEntity<String> leaveRoom(@PathVariable String roomCode) {
        // 1. Find the RoomId entity using roomCode
        Optional<RoomId> optionalRoom = roomIdRepository.findByRoomCode(roomCode);
        if (optionalRoom.isEmpty()) {
            return ResponseEntity.status(404).body("Room not found");
        }

        RoomId room = optionalRoom.get();

        // 2. Find participants in this room
        List<Participant> participants = participantRepo.findByRoom(room);
        if (participants.isEmpty()) {
            return ResponseEntity.status(404).body("No participants found for this room");
        }

        // 3. Delete first participant (or apply custom logic)
        Participant participant = participants.get(0);
        participantRepo.delete(participant);

        return ResponseEntity.ok("Participant removed successfully: " + participant.getParticipantName());
    }

    // Controller for testing
    @GetMapping("/home")
    public String homString() {
        return "this is home";
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        if (submission.getRoomCode() == null || submission.getAnswers() == null || submission.getParticipantEmail() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Room code, answers, and user email are required"));
        }
    
        String roomCode = submission.getRoomCode();
    
        try {
            // 1. Find Quiz by Room Code
            Quiz quiz = quizRepository.findByroomid(roomCode);
            if (quiz == null) {
                throw new ResourceNotFoundException("Quiz not found for room code: " + roomCode);
            }
    
            // 2. Get Questions of the Quiz
            List<Question> questions = questionRepository.findByquiz_id(quiz.getId());
    
            // 3. Initialize Scoring
            int score = 0;
            Map<Long, Boolean> results = new HashMap<>();
    
            // 4. Process Each Question
            for (Question question : questions) {
                AnswerSubmissionDTO answer = submission.getAnswers().stream()
                        .filter(a -> a.getQuestionId().equals(question.getId()))
                        .findFirst()
                        .orElse(null);
    
                if (answer != null) {
                    Optional<CorrectOption> correctOptionOpt = correctOptionRepository
                            .findByQuestionId(question.getId());
    
                    boolean isCorrect = correctOptionOpt.isPresent() &&
                            correctOptionOpt.get().getOption().getId().equals(answer.getSelectedOptionId());
    
                    results.put(question.getId(), isCorrect);
                    if (isCorrect) score++;
                }
            }
    
            // 5. Get Participant by Email from Room
            Optional<RoomId> roomIdOptional = roomIdRepository.findByRoomCode(roomCode);
            if (roomIdOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Room not found for room code: " + roomCode));
            }
    
            RoomId room = roomIdOptional.get();
            Participant participant = room.getParticipants().stream()
                    .filter(p -> p.getEmail().equalsIgnoreCase(submission.getParticipantEmail()))
                    .findFirst()
                    .orElse(null);
    
            if (participant == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Participant not found with this email in the room"));
            }
    
            // 6. Save Result
            Result result = new Result();
            result.setParticipentEmail(participant.getEmail());
            result.setParticipant(participant);
            result.setQuizTitle(quiz.getTitle());
            result.setScore(score);
            result.setSubmittedAt(LocalDateTime.now());
    
            resultRepository.save(result);
    
            // 7. Return success response
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "score", score,
                    "totalQuestions", questions.size(),
                    "results", results,
                    "message", "Quiz submitted and result saved!"));
    
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "An error occurred while processing your submission"));
        }
    }
    

    // get participent

    @GetMapping("/participants/count/{roomCode}")
    public ResponseEntity<?> getParticipantCount(@PathVariable String roomCode) {
        int count = participantService.countByRoomCode(roomCode);
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }

}