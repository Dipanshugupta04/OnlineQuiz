package com.OnlineQuiz.OnlineQuiz.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.OnlineQuiz.OnlineQuiz.DTO.AnswerDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuestionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.CorrectOption;
import com.OnlineQuiz.OnlineQuiz.Entity.Option;
import com.OnlineQuiz.OnlineQuiz.Entity.Question;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Reposistory.QuizRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody QuizRequestDTO quizDTO) {
        // Convert Quiz DTO to  Quiz Entity
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setUserName(quizDTO.getUserName());

        // Create Room ID
        String uniqueRoomCode = generateUniqueRoomCode();
        RoomId room = new RoomId();
        room.setRoomId(uniqueRoomCode);
        room.setQuiz(quiz);
        quiz.setRooms(room);

        // Convert Questions and Answers
        List<Question> questionList = new ArrayList<>();
        for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuiz(quiz);

            List<Option> answerList = new ArrayList<>();
            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Option option =new Option();
                CorrectOption correctOption=new CorrectOption();
                option.setOptionText(answerDTO.getAnswerText());
                correctOption.setCorrectOption(answerDTO.CorrectAnswer());
                option.setQuestion(question);
                answerList.add(option);
            }
            question.setOptions(answerList);
            questionList.add(question);
        }

        quiz.setQuestions(questionList);

        // Save Quiz (Cascade will save Questions, Answers, and RoomID)
        return quizRepository.save(quiz);
    }
     // Generate Unique Room Code
     private String generateUniqueRoomCode() {
        String newRoomCode;
        do {
            newRoomCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (roomIdRepository.existsByRoomCode(newRoomCode)); // Ensure uniqueness
        return newRoomCode;
    }
    
    
    

}
