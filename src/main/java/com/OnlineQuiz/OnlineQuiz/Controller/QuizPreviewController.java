package com.OnlineQuiz.OnlineQuiz.Controller;

import com.OnlineQuiz.OnlineQuiz.DTO.QuizPreviewResponseDTO;
import com.OnlineQuiz.OnlineQuiz.Service.QuizPreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5504",
                "http://localhost:5504","onlinequizwin.netlify.app","http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com" ,"http://127.0.0.1:5502", "http://localhost:5502","https://heroic-sunburst-56c10d.netlify.app" })
@RequestMapping("/api/quizzes")
public class QuizPreviewController {

    @Autowired
    private QuizPreviewService quizPreviewService;

    @GetMapping("/preview/{roomid}")
    public ResponseEntity<QuizPreviewResponseDTO> getQuizPreview(@PathVariable String roomid) {
        QuizPreviewResponseDTO response = quizPreviewService.getQuizPreview(roomid);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        System.out.println(response);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{roomid}")
    public ResponseEntity<String> deleteQuiz(@PathVariable String roomid) {
        boolean deleted = quizPreviewService.deleteQuiz(roomid);
        return ResponseEntity.ok("Exam and associated data deleted successfully for room code: " +roomid);
    }
}