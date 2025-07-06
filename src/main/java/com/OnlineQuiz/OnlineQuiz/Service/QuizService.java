package com.OnlineQuiz.OnlineQuiz.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.OnlineQuiz.OnlineQuiz.DTO.AnswerDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.AnswerSubmissionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuestionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizRequestDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.QuizSubmissionDTO;
import com.OnlineQuiz.OnlineQuiz.DTO.ResultDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.CorrectOption;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.Option;
import com.OnlineQuiz.OnlineQuiz.Entity.Question;
import com.OnlineQuiz.OnlineQuiz.Entity.Quiz;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Reposistory.quizRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.questionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.optionRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ParticipantRepo;
import com.OnlineQuiz.OnlineQuiz.Reposistory.correctOptionRepository;

@Service
public class QuizService {

    @Autowired
    private quizRepository quizRepository;

    @Autowired
    private roomIdRepository roomIdRepository;
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private questionRepository questionRepository;

    @Autowired
    private optionRepository optionRepository;

    @Autowired
    private correctOptionRepository correctOptionRepository;
    @Autowired
    private ParticipantRepo participantRepo;

    @Transactional
    public Quiz createQuiz(QuizRequestDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setUserName(quizDTO.getUserName());
        quiz.setRoomid(quizDTO.getRoomid());

        quiz = quizRepository.save(quiz);

        List<Question> questionList = new ArrayList<>();

        for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuiz(quiz);

            question = questionRepository.save(question);

            List<Option> savedOptions = new ArrayList<>();

            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Option option = new Option();
                option.setOptionText(answerDTO.getAnswerText());
                option.setQuestion(question);

                option = optionRepository.save(option);
                savedOptions.add(option);
            }

            question.setOptions(savedOptions);

            List<CorrectOption> correctOptions = new ArrayList<>();

            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                if (answerDTO.isCorrectAnswer()) {
                    for (Option option : savedOptions) {
                        if (option.getOptionText().equalsIgnoreCase(answerDTO.getAnswerText())) {
                            CorrectOption correctOption = new CorrectOption();
                            correctOption.setQuestion(question);
                            correctOption.setOption(option);

                            correctOption = correctOptionRepository.save(correctOption);
                            correctOptions.add(correctOption);
                            break;
                        }
                    }
                }
            }

            question.setCorrectOptions(correctOptions);
            question = questionRepository.save(question);
            questionList.add(question);
        }

        quiz.setQuestions(questionList);
        quiz = quizRepository.save(quiz);

        return quiz;
    }

    public RoomId generateRoomId(Long exam_Id) {
        Exam exam = examRepository.findById(exam_Id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        String roomCode;
        do {
            roomCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (roomIdRepository.findByRoomCode(roomCode).isPresent());

        RoomId roomId = new RoomId();
        roomId.setRoomCode(roomCode);
        roomId.setExam(exam);
        roomId.setStatus("WA");

        roomId = roomIdRepository.save(roomId);
        // quiz.setRoomId(roomId);
        examRepository.save(exam);

        return roomId;
    }

    // After submission to evaluate the marks
    public ResultDTO evaluateSubmission(QuizSubmissionDTO submission) {
        int total = submission.getAnswers().size();
        int correct = 0;

        for (AnswerSubmissionDTO answer : submission.getAnswers()) {
            Optional<CorrectOption> correctOption = correctOptionRepository
                    .findByQuestionIdAndOptionId(answer.getQuestionId(), answer.getSelectedOptionId());

            if (correctOption.isPresent())
                correct++;
        }

        double percentage = (correct * 100.0) / total;

        return new ResultDTO(total, correct, percentage);
    }

    public Map<String, Object> getQuestionsByRoomCode(String roomCode) {
        // First find the RoomId by its code
        RoomId room = roomIdRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with code: " + roomCode));
        
        // Get the quiz for this room
        Quiz quiz = quizRepository.findByroomid(roomCode);
            
        // Get questions for the quiz
        List<Question> questionList = questionRepository.findByquiz_id(quiz.getId());

       
        
        // Transform questions to the required format
        List<Map<String, Object>> questions = questionList.stream()
            .map(question -> {
                Map<String, Object> questionMap = new HashMap<>();
                questionMap.put("id", question.getId());
                questionMap.put("text", question.getQuestionText());
               
                
                List<Map<String, String>> options = question.getOptions().stream()
                    .map(option -> {
                        Map<String, String> optionMap = new HashMap<>();
                        optionMap.put("id", option.getId().toString());
                        optionMap.put("text", option.getOptionText());
                        return optionMap;
                    })
                    .collect(Collectors.toList());
                    
                questionMap.put("options", options);
                return questionMap;
            })
            .collect(Collectors.toList());
    
        // Get participants for this room
        List<Map<String, Object>> participants = participantRepo.findByroom_id(roomCode);
        int participantCount = participants.size();
        System.out.println(participantCount);
        // Build the final response
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("title", quiz.getTitle());
        quizData.put("questions", questions);
        quizData.put("participants", participantCount);
        
        return quizData;
    }

    //Update question

    public void updateQuizByRoomCode(String roomCode, QuizRequestDTO quizDTO) {
        // Find the quiz by room code
        Quiz quiz = quizRepository.findByroomid(roomCode);
        if (quiz == null) {
            throw new RuntimeException("Quiz not found for room code: " + roomCode);
        }
    
        // Clear old questions
        quiz.getQuestions().clear();
    
        List<Question> updatedQuestions = new ArrayList<>();
    
        for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
            Question question;
    
            // Update existing question or create new one
            if (questionDTO.getId() != null) {
                question = questionRepository.findById(questionDTO.getId().longValue())
                        .orElse(new Question());
            } else {
                question = new Question();
            }
    
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuiz(quiz);
    
            // Create and link options
            List<Option> optionList = new ArrayList<>();
            List<CorrectOption> correctOptions = new ArrayList<>();
    
            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Option option = new Option();
                option.setOptionText(answerDTO.getAnswerText());
                option.setQuestion(question);  // Set bi-directional link
                optionList.add(option);
    
                // Create CorrectOption if this answer is correct
                if (answerDTO.isCorrectAnswer()) {
                    CorrectOption correctOption = new CorrectOption();
                    correctOption.setOption(option);
                    correctOption.setQuestion(question);
                    correctOptions.add(correctOption);
                }
            }
    
            question.setOptions(optionList);
            question.setCorrectOptions(correctOptions);
            updatedQuestions.add(question);
        }
    
        // Add all updated questions to quiz
        quiz.getQuestions().addAll(updatedQuestions);
    
        // Save the updated quiz (will cascade and save questions, options, correct options)
        quizRepository.save(quiz);
    }
    

}

