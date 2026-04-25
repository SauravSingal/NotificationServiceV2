package org.example.notificationservicev2.exception;

public class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String email) {
            super("Email already exists: " + email);
        }
}
