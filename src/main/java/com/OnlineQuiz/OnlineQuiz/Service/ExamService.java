package com.OnlineQuiz.OnlineQuiz.Service;

import com.OnlineQuiz.OnlineQuiz.DTO.ExamDTO;
import com.OnlineQuiz.OnlineQuiz.Entity.Exam;
import com.OnlineQuiz.OnlineQuiz.Entity.RoomId;
import com.OnlineQuiz.OnlineQuiz.Entity.User;
import com.OnlineQuiz.OnlineQuiz.Reposistory.ExamRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.UserRepository;
import com.OnlineQuiz.OnlineQuiz.Reposistory.roomIdRepository;

import jakarta.transaction.Transactional;

import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private roomIdRepository roomIdRepository;

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
    
        exam.setStartDateTime(examDTO.getStartDate());
        exam.setEndDateTime(examDTO.getEndDate());
    
        return examRepository.save(exam);
    }


    //update exam Data
    @Transactional // Essential for database write operations
    public Exam updateExam(String roomCode, ExamDTO examDTO) {
        // 1. Find the RoomId entity by roomCode
        RoomId roomIdEntity = roomIdRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room code not found: " + roomCode));

        // 2. Get the associated Exam entity
        Exam examToUpdate = roomIdEntity.getExam();

        if (examToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Exam associated with room code: " + roomCode);
        }

        // 3. Apply updates from the ExamDTO to the existing Exam entity
        if (examDTO.getExamDescription() != null) {
            examToUpdate.setExamDescription(examDTO.getExamDescription());
        }
        if (examDTO.getStartDate() != null) {
            examToUpdate.setStartDateTime(examDTO.getStartDate());
        }
        if (examDTO.getEndDate() != null) {
            examToUpdate.setEndDateTime(examDTO.getEndDate());
        }

        if(examDTO.getExamName()!=null){
            examToUpdate.setExamName(examDTO.getExamName());
        }
        if(examDTO.getDurationMinutes()!=null){
            examToUpdate.setDurationMinutes(examDTO.getDurationMinutes());
        }
        if(examDTO.getCreatedBy()!=null){
            examToUpdate.setCreatedBy(examDTO.getCreatedBy());
            }

        
        return examRepository.save(examToUpdate);
    }
    
}