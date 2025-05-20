package com.project.healthy_life_was.healthy_life.dto.auth.response;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;

@Getter
public class FindInfoResponseDto {
    private final String recipient;
    private final String username;
    private final String status;
    private final String token;

    public FindInfoResponseDto(MimeMessage message, String username, String token) throws MessagingException {
        this.recipient = message.getAllRecipients()[0].toString();
        this.username = username;
        this.token = token;
        this.status = "sent";
    }
}