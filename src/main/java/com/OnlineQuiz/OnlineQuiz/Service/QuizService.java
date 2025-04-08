package com.OnlineQuiz.OnlineQuiz.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private quizRepository quizRepo;

    @Transactional
    public Quiz createQuiz(QuizRequestDTO quizDTO) {
        // Step 1: Create and save the quiz
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setUserName(quizDTO.getUserName());
        quiz = quizRepository.save(quiz); // ✅ Save the quiz first to get an ID

        // Step 2: Process questions
        List<Question> questionList = new ArrayList<>();
        for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuiz(quiz);

            // Save question first before linking options
            question = questionRepository.save(question);
            questionList.add(question);

            // Step 3: Process options
            List<Option> optionList = new ArrayList<>();
            List<CorrectOption> correctOptions = new ArrayList<>();

            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Option option = new Option();
                option.setOptionText(answerDTO.getAnswerText());
                option.setQuestion(question);
                optionList.add(option);
            }

            // Save options after linking them to question
            optionList = optionRepository.saveAll(optionList);
            question.setOptions(optionList);

            // Step 4: Process correct answers
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
        quizRepository.save(quiz); // Save the quiz again with linked questions

        // Step 5: Generate and save Room ID
        // String uniqueRoomCode = generateUniqueRoomCode();
        // RoomId room = new RoomId();
        // room.setRoomCode(uniqueRoomCode);
        // room.setQuiz(quiz);
        // roomIdRepository.save(room);

        return quiz;
    }

    // private String generateUniqueRoomCode() {
    //     return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    // }

//     public RoomId generateRoomId(Long quizId) {
// RoomId roomId =new RoomId();

//     Quiz quiz = quizRepository.findById(quizId)
//             .orElseThrow(() -> new RuntimeException("Quiz not found"));

//     String roomCode;
//     roomCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//     System.out.println(roomCode);
//      Optional<RoomId> rId=roomIdRepository.findByRoomCode(roomCode);
//     if(rId.isPresent()){

 
//     roomId.setRoomCode(roomCode);
//     roomId.setQuiz(quiz);
//     quiz.setRoomId(roomId);

//     quizRepository.save(quiz);
//     }
//     return roomId;
    
// }

// public List<Question> getQuestionsByRoomCode(String roomCode) {
//     RoomId  roomId =new RoomId();
//    Optional<RoomId> rid = roomIdRepository.findByRoomCode(roomCode);

//     if(!rid.equals(roomCode)){
//         throw new RuntimeException("Room ID not found!");
//     }
    
            
//     return roomId.getQuiz().getQuestions();
// }
public RoomId generateRoomId(Long quiz_Id) {
    Quiz quiz = quizRepository.findById(quiz_Id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

    String roomCode;
    do {
        roomCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        System.out.println(roomCode);
    } while (roomIdRepository.findByRoomCode(roomCode).isPresent());

    RoomId roomId = new RoomId();
    roomId.setRoomCode(roomCode);
    roomId.setQuiz(quiz);

    roomId = roomIdRepository.save(roomId);
    quiz.setRoomId(roomId);
    quizRepository.save(quiz);

    return roomId;
}

public List<Question> getQuestionsByRoomCode(String roomCode) {
    RoomId room = roomIdRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new RuntimeException("Room not found with code: " + roomCode));

    Quiz quiz = room.getQuiz();
    return quiz.getQuestions();
}

}
