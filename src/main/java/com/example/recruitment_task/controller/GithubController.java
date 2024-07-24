package com.example.recruitment_task.controller;

import com.example.recruitment_task.exception.dto.ErrorResponse;
import com.example.recruitment_task.exception.GithubException;
import com.example.recruitment_task.model.dto.RepositoryDetailsResponse;
import com.example.recruitment_task.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


/**
 * Controller for handling GitHub repository-related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/github")
public class GithubController {

    private final GithubService githubService;

    /**
     * Endpoint to get non-fork repositories for a given username.
     *
     * @param username the GitHub username
     * @return a list of non-fork repositories with branch details
     */
    @GetMapping("/repository/{username}")
    public ResponseEntity<?> getRepos(@PathVariable String username) {
        try {
            RepositoryDetailsResponse nonForkRepos = githubService.getNonForkRepositories(username);
            return ResponseEntity.ok(nonForkRepos);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GithubException("User not found");
            } else {
                throw new GithubException(HttpStatus.valueOf(e.getStatusCode().value()), "Error fetching data from GitHub: " + e.getMessage());
            }
        }
    }

    /**
     * Exception handler for GithubException.
     *
     * @param ex the GithubException
     * @return an error response with the status and message
     */
    @ExceptionHandler(GithubException.class)
    public ResponseEntity<ErrorResponse> handleGithubException(GithubException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }

}
