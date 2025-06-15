package com.OnlineQuiz.OnlineQuiz.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.*;
import com.OnlineQuiz.OnlineQuiz.Entity.*;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.questionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.quizRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizPreviewService {

    @Autowired
    private quizRepository quizRepository;
    @Autowired
    private roomIdRepository roomIdRepository;
    @Autowired
    private ExamRepository examRepository;

   public QuizPreviewResponseDTO getQuizPreview(String roomid) {
    // Find the room ID record
    RoomId rid = roomIdRepository.findByroomCode(roomid);
    if (rid == null) {
        return null; // Room doesn't exist
    }

    // Get the exam details
    Optional<Exam> examOptional = examRepository.findById(rid.getExam().getId());
    if (!examOptional.isPresent()) {
        return null; // Exam doesn't exist
    }
    Exam exam = examOptional.get();

    // Create the response DTO with exam details
    QuizPreviewResponseDTO response = new QuizPreviewResponseDTO();
    response.setTitle(exam.getExamName());
    response.setUserName(exam.getCreatedBy());
    response.setCreatedAt(exam.getStartDateTime());

    // Try to find quiz questions if they exist
    Quiz quiz = quizRepository.findByroomid(roomid);
    if (quiz != null) {
        response.setQuizId(quiz.getId());
        List<QuestionDTO> questionDTOs = quiz.getQuestions().stream()
                .map(this::convertToQuestionDTO)
                .collect(Collectors.toList());
        response.setQuestions(questionDTOs);
    } else {
        // No quiz exists yet, return empty question list
        response.setQuestions(new ArrayList<>());
    }

    return response;
}
    private QuestionDTO convertToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestionText(question.getQuestionText());

        List<AnswerDTO> answerDTOs = question.getOptions().stream()
                .map(option -> {
                    AnswerDTO answer = new AnswerDTO();
                    answer.setAnswerText(option.getOptionText());

                    // Check if this option is a correct answer
                    boolean isCorrect = question.getCorrectOptions().stream()
                            .anyMatch(co -> co.getOption().getId().equals(option.getId()));
                    answer.setCorrectAnswer(isCorrect);

                    return answer;
                })
                .collect(Collectors.toList());

        questionDTO.setAnswers(answerDTOs);
        return questionDTO;
    }

    @Transactional // Essential for delete operations
    public boolean deleteQuiz(String roomid) { // Changed return type to boolean for better API response
        Optional<Quiz> quizOptional = Optional.of(quizRepository.findByroomid(roomid));

        if (quizOptional.isPresent()) {
            Quiz quizToDelete = quizOptional.get();
            quizRepository.delete(quizToDelete); // This will cascade to delete Questions, Options, CorrectOptions
                                                 // due to your @OneToMany mappings in Quiz and Question entities.
            return true; // Quiz found and deleted
        }
        return false; // Quiz not found for the given roomid
    }

}