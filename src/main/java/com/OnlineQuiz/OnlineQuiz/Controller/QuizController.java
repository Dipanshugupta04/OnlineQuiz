package com.OnlineQuiz.OnlineQuiz.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Question;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Service.QuizService;

@RestController
@RequestMapping("/quiz")
// Quiz Controller
public class QuizController {

    @Autowired
    private QuizService quizService;
@Autowired
private ExamRepository examRepository;
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

    @GetMapping("/home")
    public String homString() {
        return "this is home";
    }

}
