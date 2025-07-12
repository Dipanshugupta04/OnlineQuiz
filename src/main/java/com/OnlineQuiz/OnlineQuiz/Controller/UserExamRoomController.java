package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5504",
                "http://localhost:5504","https://majestic-kangaroo-33ba55.netlify.app","http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com", "http://127.0.0.1:5502", "http://localhost:5502","https://heroic-sunburst-56c10d.netlify.app" })
@RequestMapping("/api/exam")
public class UserExamRoomController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private roomIdRepository roomIdRepository;

    @GetMapping("/by-user/{uniqueId}")
    public ResponseEntity<?> getExamsByUniqueId(@PathVariable String uniqueId) {
        Optional<User> userOpt = userRepository.findByUniqueId(uniqueId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with uniqueId: " + uniqueId);
        }

        User user = userOpt.get();
        List<Exam> exams = examRepository.findByUserId(user.getId());

        List<Map<String, Object>> result = new ArrayList<>();

        for (Exam exam : exams) {
            Map<String, Object> examData = new HashMap<>();
            examData.put("examId", exam.getId());
            examData.put("examName", exam.getExamName());
            examData.put("createdBy", exam.getCreatedBy());
            examData.put("duration", exam.getDurationMinutes());
            examData.put("startDate", exam.getStartDateTime());
            examData.put("endDate", exam.getEndDateTime());
            examData.put("description", exam.getExamDescription());

            Optional<RoomId> roomOpt = roomIdRepository.findByExamId(exam.getId());
            if (roomOpt.isPresent()) {
                RoomId room = roomOpt.get();
                examData.put("roomCode", room.getRoomCode());
                examData.put("roomStatus", room.getStatus());
            } else {
                examData.put("roomCode", null);
                examData.put("roomStatus", "Not created");
            }

            result.add(examData);
        }

        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/delete-by-room/{roomCode}")
    @Transactional 
    public ResponseEntity<String> deleteExamByRoomCode(@PathVariable String roomCode) {
        if (roomCode == null || roomCode.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Room code cannot be empty.");
        }
    
        try {
            // Step 1: Find the RoomId entity
            RoomId roomIdEntity = roomIdRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room. code not found: " + roomCode));
    
            // Step 2: Get the associated Exam safely
            Exam exam = roomIdEntity.getExam();
            if (exam != null && exam.getId() != null) {
                // Step 3: Delete Exam first
                examRepository.deleteById(exam.getId());
                
                System.out.println(exam);
            }
    
            // Step 4: Break relationship before deleting RoomId
            roomIdEntity.setExam(null);
            roomIdRepository.delete(roomIdEntity);
    
            return ResponseEntity.ok("Exam and associated data deleted successfully for room code: " + roomCode);
    
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            e.printStackTrace(); // better than just printing message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the exam: " + e.getMessage());
        }
    }
    
}

