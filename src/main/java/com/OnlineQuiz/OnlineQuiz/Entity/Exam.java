package com.OnlineQuiz.OnlineQuiz.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "exam_name", nullable = true)
    private String examName;
    
    @Column(name = "created_by", nullable = true)
    private String createdBy;
    
    @Column(name = "start_date_time", nullable = true)
    private LocalDateTime startDateTime;
    
    @Column(name = "end_date_time", nullable = true)
    private LocalDateTime endDateTime;
    
    @Column(name = "duration_minutes", nullable = true)
    private Integer durationMinutes;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public String getExamName() {
        return examName;
    }
    
    public void setExamName(String examName) {
        this.examName = examName;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
    
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
}
