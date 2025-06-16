package com.OnlineQuiz.OnlineQuiz.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

//Entity for Paticipant
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String participantName;
    private String email;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private RoomId room;

    // Getters and Setters
    public Long getId() {
        return id;
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


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}
