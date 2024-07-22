package com.example.recruitment_task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents detailed information about a GitHub repository, including branch details.
 */
@AllArgsConstructor
@Setter
@Getter
public class RepositoryDetails {

    private String name;

    private String ownerLogin;

    private List<Branch> branches;

}
