package com.example.recruitment_task.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an error response with status and message.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String message;

}