package com.example.recruitment_task.service.serviceImpl;

import com.example.recruitment_task.exception.GithubException;
import com.example.recruitment_task.model.Branch;
import com.example.recruitment_task.model.Repository;
import com.example.recruitment_task.model.RepositoryDetails;
import com.example.recruitment_task.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the GithubService interface.
 */
@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final RestTemplate restTemplate;

    @Override
    public List<RepositoryDetails> getNonForkRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";

        ResponseEntity<List<Repository>> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Repository>>() {}
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new GithubException("User not found");
        } catch (HttpClientErrorException e) {
            throw new GithubException("Error fetching data from GitHub: " + e.getMessage());
        }

        List<Repository> repositoryList = response.getBody();

        if (repositoryList == null) {
            throw new GithubException("Unexpected response from GitHub");
        }

        return repositoryList.stream()
                .filter(repo -> !repo.isFork())
                .map(this::getRepoDetails)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves detailed information about a repository, including branch details.
     *
     * @param repo the GitHub repository
     * @return detailed information about the repository
     */
    private RepositoryDetails getRepoDetails(Repository repo) {
        String url = "https://api.github.com/repos/" + repo.getOwner().getLogin() + "/" + repo.getName() + "/branches";

        ResponseEntity<List<Branch>> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Branch>>() {}
            );
        } catch (HttpClientErrorException e) {
            throw new GithubException("Error fetching branches from GitHub");
        }

        List<Branch> branches = response.getBody();
        return new RepositoryDetails(repo.getName(), repo.getOwner().getLogin(), branches);
    }
}
