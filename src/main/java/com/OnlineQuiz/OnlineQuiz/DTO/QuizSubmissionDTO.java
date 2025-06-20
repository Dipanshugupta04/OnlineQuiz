package com.OnlineQuiz.OnlineQuiz.DTO;

import java.util.List;

public class QuizSubmissionDTO {
    private String roomCode;
    private String ParticipantEmail;
    private List<AnswerSubmissionDTO> answers;
    // Getters and Setters

    public QuizSubmissionDTO(String roomCode, String ParticipantEmail, List<AnswerSubmissionDTO> answers) {
        this.roomCode = roomCode;
        this.ParticipantEmail = ParticipantEmail;
        this.answers = answers;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public List<AnswerSubmissionDTO> getAnswers() {
        return answers;
    }

    public String getParticipantEmail() {
        return ParticipantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        ParticipantEmail = participantEmail;
    }

    public void setAnswers(List<AnswerSubmissionDTO> answers) {
        this.answers = answers;
    }

}
