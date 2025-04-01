package com.OnlineQuiz.OnlineQuiz.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Service.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;



    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody QuizRequestDTO quizDTO) {
    return quizService.createQuiz(quizDTO );
    }
    
    
    @GetMapping("/home")
    public String homString() {
       return "this is home";
    }
    

}
