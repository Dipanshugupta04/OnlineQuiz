package com.OnlineQuiz.OnlineQuiz.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String examName;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (startDateTime == null) {
            throw new IllegalStateException("Start date/time cannot be null");
        }
        if (endDateTime == null) {
            throw new IllegalStateException("End date/time cannot be null");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalStateException("End date/time must be after start date/time");
        }
        if (durationMinutes == null || durationMinutes <= 0) {
            throw new IllegalStateException("Duration must be a positive number");
        }
    }
}