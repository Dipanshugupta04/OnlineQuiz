package com.OnlineQuiz.OnlineQuiz.Controller;

import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Result;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ResultRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com", "https://majestic-kangaroo-33ba55.netlify.app","http://127.0.0.1:5502", "http://localhost:5502","https://heroic-sunburst-56c10d.netlify.app" ,"http://127.0.0.1:5504",
                "http://localhost:5504"})
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ResultRepository resultRepository;

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("files") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        
        User user = userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }
        
        user.setProfilePicture(file.getBytes());
        userRepository.save(user);
        
        return ResponseEntity.ok("Profile picture uploaded successfully");
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        
        User user = userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg") // Adjust based on your image type
                .body(user.getProfilePicture());
    }

// fetch the user achievements
    @GetMapping("/user/achievements")
public ResponseEntity<?> getUserAchievements(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    String email = authentication.getName(); // email used as username
    List<Result> results = resultRepository.findByParticipantEmail(email);

    List<Map<String, Object>> response = results.stream().map(result -> {
        Map<String, Object> map = new HashMap<>();
        map.put("quizTitle", result.getQuizTitle());
        map.put("score", result.getScore());
        map.put("marks", result.getMarks());
        map.put("submittedAt", result.getSubmittedAt());
        return map;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(response);
}


@GetMapping("/user/history")
public ResponseEntity<?> getUserQuizHistory(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    String email = authentication.getName();
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isEmpty()) {
        return ResponseEntity.badRequest().body("User not found");
    }

    User user = optionalUser.get();
    List<Exam> exams = examRepository.findByUserId(user.getId()); // Or based on result entity

    List<Map<String, Object>> response = exams.stream().map(exam -> {
        Map<String, Object> map = new HashMap<>();
        map.put("examName", exam.getExamName());
        map.put("startDate", exam.getStartDateTime());
        map.put("durationMinutes", exam.getDurationMinutes());
        map.put("lastdate", exam.getEndDateTime());
        map.put("description", exam.getExamDescription());
        return map;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(response);
}



@GetMapping("/user/monthly-average")
public ResponseEntity<?> getLastFiveMonthsAverageScore(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    String email = authentication.getName();
    List<Map<String, Object>> monthlyAverages = new ArrayList<>();

    // Current month
    YearMonth currentMonth = YearMonth.now();

    // Go back 5 full months (excluding current month)
    for (int i = 5; i >= 1; i--) {
        YearMonth targetMonth = currentMonth.minusMonths(i);
        LocalDateTime start = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime end = targetMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Result> results = resultRepository
            .findByParticipentEmailAndSubmittedAtBetween(email, start, end);

        double avg = results.isEmpty() ? 0 :
            results.stream().mapToDouble(Result::getScore).average().orElse(0);

        Map<String, Object> monthData = new HashMap<>();
        monthData.put("month", targetMonth.getMonth().toString()); // e.g., "FEBRUARY"
        monthData.put("year", targetMonth.getYear());
        monthData.put("averageScore", (int) Math.round(avg));
        monthData.put("count", results.size());

        monthlyAverages.add(monthData);
    }

    return ResponseEntity.ok(monthlyAverages);
}



}