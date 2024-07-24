package com.example.recruitment_task.service;

import com.example.recruitment_task.model.dto.RepositoryDetailsResponse;

/**
 * Service interface for GitHub operations.
 */
public interface GithubService {

    /**
     * Retrieves a list of non-fork repositories for the specified username.
     *
     * @param username the GitHub username
     * @return a list of non-fork repositories with branch details
     */
    RepositoryDetailsResponse getNonForkRepositories(String username);

}
