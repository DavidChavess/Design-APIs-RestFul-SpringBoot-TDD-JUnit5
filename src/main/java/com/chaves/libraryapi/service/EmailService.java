package com.chaves.libraryapi.service;

import java.util.List;

public interface EmailService {
    void sendMails(List<String> emails, String message);
}
