package com.uit.DoAnTotNghiepBE.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stream_id", nullable = false)
    private Stream stream;

    @Column(nullable = false)
    private Instant timestamp;

    // getter, setter, constructor

    public ChatMessage(Long id, String content, User user, Stream stream, Instant timestamp) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.stream = stream;
        this.timestamp = timestamp;
    }

    public ChatMessage() {

    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Stream getStream() {
        return stream;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
