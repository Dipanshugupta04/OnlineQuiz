package com.OnlineQuiz.OnlineQuiz.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name", nullable = false)
    private String examName;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @PrePersist
    @PreUpdate
    public void validateDates() {
        // First ensure all required fields are set
        if (startDateTime == null) {
            throw new IllegalStateException("Start date/time cannot be null");
        }
        
        if (durationMinutes == null || durationMinutes <= 0) {
            throw new IllegalStateException("Duration must be a positive number");
        }
        
        // Calculate endDateTime if not provided
        if (endDateTime == null) {
            this.endDateTime = startDateTime.plusMinutes(durationMinutes);
        }
        // If both are provided, ensure consistency
        else if (!endDateTime.equals(startDateTime.plusMinutes(durationMinutes))) {
            // Adjust duration to match the provided dates
            this.durationMinutes = (int) java.time.Duration.between(startDateTime, endDateTime).toMinutes();
        }
        
        // Final validation
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalStateException("End date/time cannot be before start date/time");
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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