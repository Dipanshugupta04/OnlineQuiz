package com.OnlineQuiz.OnlineQuiz.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.ExamDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public Exam createExam(ExamDTO examDTO) {
        Exam exam = new Exam();
        exam.setExamName(examDTO.getExamName());
        exam.setCreatedBy(examDTO.getCreatedBy());
        exam.setDurationMinutes(examDTO.getDurationMinutes());
        exam.setExamDescription(examDTO.getExamDescription());
    
        User user = userRepository.findByUniqueId(examDTO.getUniqueId())
            .orElseThrow(() -> new RuntimeException("User not found with given unique ID"));
    
        exam.setUser(user);
    
        exam.setStartDateTime(LocalDateTime.of(examDTO.getStartDate(), LocalTime.MIN));
        exam.setEndDateTime(LocalDateTime.of(examDTO.getEndDate(), LocalTime.MAX));
    
        return examRepository.save(exam);
    }
    
}