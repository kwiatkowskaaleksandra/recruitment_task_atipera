package com.example.recruitment_task.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for GitHub-related errors.
 */
public class GithubException extends RuntimeException {

    private final int status;

    /**
     * Constructs a GithubException with a 404 status and the specified message.
     *
     * @param message the detail message
     */
    public GithubException (String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND.value();
    }

    /**
     * Constructs a GithubException with the specified status and message.
     *
     * @param status the HTTP status
     * @param message the detail message
     */
    public GithubException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }

    /**
     * Returns the HTTP status of this exception.
     *
     * @return the HTTP status
     */
    public int getStatus() {
        return status;
    }

}
