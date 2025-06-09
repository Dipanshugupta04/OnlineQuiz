package com.OnlineQuiz.OnlineQuiz.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.ExamDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public Exam createExam(ExamDTO examDTO) {
        Exam exam = new Exam();
        exam.setExamName(examDTO.getExamName());
        exam.setCreatedBy(examDTO.getCreatedBy());
        exam.setDurationMinutes(examDTO.getDurationMinutes());
        exam.setExamDescription(examDTO.getExamDescription());
        
        // Convert LocalDate to LocalDateTime using start of day
        exam.setStartDateTime(LocalDateTime.of(examDTO.getStartDate(), LocalTime.MIN));
        exam.setEndDateTime(LocalDateTime.of(examDTO.getEndDate(), LocalTime.MAX));
        
        return examRepository.save(exam);
    }
}