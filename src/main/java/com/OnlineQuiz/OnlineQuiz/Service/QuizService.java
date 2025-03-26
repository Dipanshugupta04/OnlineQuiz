package com.OnlineQuiz.OnlineQuiz.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.OnlineQuiz.OnlineQuiz.DTO.AnswerDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuestionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.CorrectOption;
import com.OnlineQuiz.OnlineQuiz.Entity.Option;
import com.OnlineQuiz.OnlineQuiz.Entity.Question;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Reposistory.quizRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.questionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.optionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.correctOptionRepository;

@Service
public class QuizService {
    
    @Autowired
    private quizRepository quizRepository;

    @Autowired
    private roomIdRepository roomIdRepository;

    @Autowired
    private questionRepository questionRepository;

    @Autowired
    private optionRepository optionRepository;

    @Autowired
    private correctOptionRepository correctOptionRepository;

    @Transactional
    public Quiz createQuiz(QuizRequestDTO quizDTO) {
        // ✅ Step 1: Create and save the quiz
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setUserName(quizDTO.getUserName());
        quiz = quizRepository.save(quiz); // ✅ Save the quiz first to get an ID

        // ✅ Step 2: Process questions
        List<Question> questionList = new ArrayList<>();
        for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuiz(quiz);

            // ✅ Save question first before linking options
            question = questionRepository.save(question); 
            questionList.add(question);

            // ✅ Step 3: Process options
            List<Option> optionList = new ArrayList<>();
            List<CorrectOption> correctOptions = new ArrayList<>();

            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Option option = new Option();
                option.setOptionText(answerDTO.getAnswerText());
                option.setQuestion(question);
                optionList.add(option);
            }

            // ✅ Save options after linking them to question
            optionList = optionRepository.saveAll(optionList);
            question.setOptions(optionList);

            // ✅ Step 4: Process correct answers
            for (Option option : optionList) {
                for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                    if (answerDTO.getAnswerText().equals(option.getOptionText()) && answerDTO.isCorrectAnswer()) {
                        CorrectOption correctOption = new CorrectOption();
                        correctOption.setQuestion(question);
                        correctOption.setOption(option);
                        correctOptions.add(correctOption);
                    }
                }
            }

            correctOptionRepository.saveAll(correctOptions);
            question.setCorrectOptions(correctOptions);
        }

        quiz.setQuestions(questionList);
        quizRepository.save(quiz); // ✅ Save the quiz again with linked questions

        // ✅ Step 5: Generate and save Room ID
        String uniqueRoomCode = generateUniqueRoomCode();
        RoomId room = new RoomId();
        room.setRoomCode(uniqueRoomCode);
        room.setQuiz(quiz);
        roomIdRepository.save(room);

        return quiz;
    }

    private String generateUniqueRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
