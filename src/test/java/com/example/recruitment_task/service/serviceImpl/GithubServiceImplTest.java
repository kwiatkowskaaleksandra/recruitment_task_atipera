package com.example.recruitment_task.service.serviceImpl;

import com.example.recruitment_task.exception.GithubException;
import com.example.recruitment_task.model.*;
import com.example.recruitment_task.model.dto.RepositoryDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class GithubServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubServiceImpl githubService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNonForkRepositories_success() {
        Repository repo = new Repository();
        repo.setName("test-repo");
        repo.setFork(false);
        Owner owner = new Owner();
        owner.setLogin("test-owner");
        repo.setOwner(owner);

        Branch branch = new Branch();
        branch.setName("main");
        Commit commit = new Commit();
        commit.setSha("test-sha");
        branch.setCommit(commit);

        when(restTemplate.exchange(
                eq("https://api.github.com/users/test-user/repos"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(Collections.singletonList(repo)));

        when(restTemplate.exchange(
                eq("https://api.github.com/repos/test-owner/test-repo/branches"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(Collections.singletonList(branch)));

        RepositoryDetailsResponse result = githubService.getNonForkRepositories("test-user");

        assertNotNull(result);
        assertEquals(1, result.getRepositoryDetailsList().size());
        assertEquals("test-repo", result.getRepositoryDetailsList().get(0).getName());
        assertEquals("test-owner", result.getRepositoryDetailsList().get(0).getOwnerLogin());
        assertEquals(1, result.getRepositoryDetailsList().get(0).getBranches().size());
        assertEquals("main", result.getRepositoryDetailsList().get(0).getBranches().get(0).getName());
        assertEquals("test-sha", result.getRepositoryDetailsList().get(0).getBranches().get(0).getCommit().getSha());
    }

    @Test
    public void testGetNonForkRepositories_userNotFound() {
        when(restTemplate.exchange(
                eq("https://api.github.com/users/invalid-user/repos"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(HttpClientErrorException.NotFound.class);

        GithubException exception = assertThrows(GithubException.class, () -> githubService.getNonForkRepositories("invalid-user"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testGetNonForkRepositories_unexpectedResponse() {
        when(restTemplate.exchange(
                eq("https://api.github.com/users/test-user/repos"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(null));

        GithubException exception = assertThrows(GithubException.class, () -> githubService.getNonForkRepositories("test-user"));
        assertEquals("Unexpected response from GitHub", exception.getMessage());
    }

    @Test
    public void testGetNonForkRepositories_errorFetchingBranches() {
        Repository repo = new Repository();
        repo.setName("test-repo");
        repo.setFork(false);
        Owner owner = new Owner();
        owner.setLogin("test-owner");
        repo.setOwner(owner);

        when(restTemplate.exchange(
                eq("https://api.github.com/users/test-user/repos"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(Collections.singletonList(repo)));

        when(restTemplate.exchange(
                eq("https://api.github.com/repos/test-owner/test-repo/branches"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(HttpClientErrorException.class);

        GithubException exception = assertThrows(GithubException.class, () -> githubService.getNonForkRepositories("test-user"));
        assertEquals("Error fetching branches from GitHub", exception.getMessage());
    }

    @Test
    public void testGetNonForkRepositories_otherHttpClientError() {
        when(restTemplate.exchange(
                eq("https://api.github.com/users/test-user/repos"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));

        GithubException exception = assertThrows(GithubException.class, () -> githubService.getNonForkRepositories("test-user"));
        assertEquals("Error fetching data from GitHub: 500 Internal Server Error", exception.getMessage());
    }

}