# GitHub Repositories API
This project is a Spring Boot application that consumes the GitHub API to list all non-fork repositories of a given GitHub user along with their branch names and last commit SHA. If the given GitHub username does not exist, it returns a 404 response with an appropriate message.

# Requirements
- Java 21
- Spring Boot 3.0+

# Usage
1. Clone the repository
2. Build the project using Maven:
   ```sh
   mvn clean install
3. To run the application, use the following command:
   ```sh
   mvn spring-boot:run

The application will start on `http://localhost:8080`.

# API Endpoints 
- Get Repositories
  ```sh
  http://localhost:8080/github/repository/{username}

Path parameters:
- `username` - GitHub username

Response:
- 200 OK - List of repositories with their branches and last commit SHA.
- 404 Not Found - User not found.

# Example
For a username that exists `kwiatkowskaaleksandra`:
```sh
http://localhost:8080/github/repository/kwiatkowskaaleksandra
```

Example Response:
```json
[
    {
        "name": "Biuro-Podrozy-Sky-Route-Travel",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "main",
                "commit": {
                    "sha": "bf0d9e7b2a1b27d66dbbe9788e9b8f6b4ac39db5"
                }
            }
        ]
    },
    {
        "name": "goTravelApp",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "master",
                "commit": {
                    "sha": "b04a3913f7d0a77dc62acd93e78098620e614d36"
                }
            }
        ]
    },
    {
        "name": "Kalkulator",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "master",
                "commit": {
                    "sha": "b41a0046e901cbbb90cd3a9a2df7016f568afedf"
                }
            }
        ]
    },
    {
        "name": "notebookApp",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "main",
                "commit": {
                    "sha": "98d101925af5948652016c9ba7dd4752aca4aaef"
                }
            }
        ]
    },
    {
        "name": "recruitment_task",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "master",
                "commit": {
                    "sha": "ad2d203106350989e475533e78eae006e7e5664f"
                }
            }
        ]
    },
    {
        "name": "recruitment_task_atipera",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "master",
                "commit": {
                    "sha": "af76387e0ba7b3ee49cc6a5cbc34315135d1cefe"
                }
            }
        ]
    },
    {
        "name": "SchematBlokowy",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "master",
                "commit": {
                    "sha": "f04ffca89021bfcb30efe97c575c5d3457d061d0"
                }
            }
        ]
    },
    {
        "name": "SklepWhisky",
        "ownerLogin": "kwiatkowskaaleksandra",
        "branches": [
            {
                "name": "master",
                "commit": {
                    "sha": "6e06c9b723abd520ff4c153a269e7c126f2ab907"
                }
            }
        ]
    }
]
```

For a username that does not exist `test-owner`:
```sh
http://localhost:8080/github/repository/test-owner
```

Example response:
```json
{
    "status": 404,
    "message": "User not found"
}
```

# Testing
This project includes unit tests that use Mockito to mock dependencies and verify the functionality of the application. To run the tests, use the following command:
```sh
mvn test
```
