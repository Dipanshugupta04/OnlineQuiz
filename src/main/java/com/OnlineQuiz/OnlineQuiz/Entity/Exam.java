package com.OnlineQuiz.OnlineQuiz.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    @Column(nullable = false)
    private LocalDate startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    @Column(nullable = false)
    private LocalDate endDateTime;

    @Column(nullable = false)
    private String examDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // Getters and setters...

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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    
    public LocalDate getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDate startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDate getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDate endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
