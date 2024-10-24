package com.example.matchretrivalservice.Service;

import com.example.matchretrivalservice.Model.Match;
import com.example.matchretrivalservice.Model.UserProfile;
import com.example.matchretrivalservice.Repo.MatchRepo;
import com.example.matchretrivalservice.Repo.UserProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Service
public class MatchRetrivalService {
    private final MatchRepo matchRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public MatchRetrivalService(MatchRepo matchRepo, RestTemplateBuilder restTemplateBuilder) {
        this.matchRepo = matchRepo;
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<UserProfile> getMatchesForUser(Long userId) {
        List<Match> matches = matchRepo.findByUserId1OrUserId2(userId, userId);
        List<Long> matchedUserIds = new ArrayList<>();
        for (Match match : matches) {
            if (userId.equals(match.getUserId1())) {
                matchedUserIds.add(match.getUserId2());
            } else {
                matchedUserIds.add(match.getUserId1());
            }
        }

        String profileServiceUrl = "http://localhost:8080/api/profiles/by-ids";

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(matchedUserIds);

        ResponseEntity<List<UserProfile>> response = restTemplate.exchange(
                profileServiceUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<UserProfile>>() {}
        );

        return response.getBody();
    }
}




