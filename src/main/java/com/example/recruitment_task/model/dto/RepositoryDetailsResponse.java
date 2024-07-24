package com.example.recruitment_task.model.dto;

import com.example.recruitment_task.model.RepositoryDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Response containing a list of repository details.
 */
@AllArgsConstructor
@Getter
public class RepositoryDetailsResponse {

    private List<RepositoryDetails> repositoryDetailsList;

}
