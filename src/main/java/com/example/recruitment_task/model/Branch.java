package com.example.recruitment_task.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a GitHub repository branch.
 */
@Getter
@Setter
public class Branch {

    private String name;

    private Commit commit;

}
