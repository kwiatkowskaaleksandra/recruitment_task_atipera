package com.example.recruitment_task.controller;

import com.example.recruitment_task.model.Branch;
import com.example.recruitment_task.model.Commit;
import com.example.recruitment_task.model.RepositoryDetails;
import com.example.recruitment_task.model.dto.RepositoryDetailsResponse;
import com.example.recruitment_task.service.GithubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class GithubControllerTest {

    @Mock
    private GithubService githubService;

    @InjectMocks
    private GithubController githubController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        githubController = new GithubController(githubService);
        mockMvc = standaloneSetup(githubController).build();
    }

    @Test
    public void testGetRepos_success() throws Exception {
        Branch branch = new Branch();
        branch.setName("main");
        Commit commit = new Commit();
        commit.setSha("test-sha");
        branch.setCommit(commit);

        RepositoryDetails repoDetails = new RepositoryDetails("test-repo", "test-owner", Collections.singletonList(branch));

        when(githubService.getNonForkRepositories("test-user")).thenReturn(new RepositoryDetailsResponse(Collections.singletonList(repoDetails)));

        mockMvc.perform(get("/github/repository/test-user")
                        .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositoryDetailsList[0].name").value("test-repo"))
                .andExpect(jsonPath("$.repositoryDetailsList[0].ownerLogin").value("test-owner"))
                .andExpect(jsonPath("$.repositoryDetailsList[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$.repositoryDetailsList[0].branches[0].commit.sha").value("test-sha"));
    }

    @Test
    public void testGetRepos_userNotFound() throws Exception {
        when(githubService.getNonForkRepositories("invalid-user")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/github/repository/invalid-user")
                        .header("Accept", "application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    public void testGetRepos_otherHttpClientError() throws Exception {
        when(githubService.getNonForkRepositories("test-user")).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));

        mockMvc.perform(get("/github/repository/test-user")
                        .header("Accept", "application/json"))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Error fetching data from GitHub: 500 Internal Server Error"));
    }
}