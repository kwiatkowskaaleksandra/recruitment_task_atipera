package com.example.recruitment_task.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a GitHub repository.
 */
@Getter
@Setter
public class Repository {

    private String name;

    private Owner owner;

    private boolean fork;

}
