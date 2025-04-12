package com.OnlineQuiz.OnlineQuiz.Entity;

import jakarta.persistence.*;
//Entity for Paticipant
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String participantName;
    private String ParticipantEmail;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomId room;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getParticipantEmail() {
        return ParticipantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        ParticipantEmail = participantEmail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public RoomId getRoom() {
        return room;
    }

    public void setRoom(RoomId room) {
        this.room = room;
    }
}
