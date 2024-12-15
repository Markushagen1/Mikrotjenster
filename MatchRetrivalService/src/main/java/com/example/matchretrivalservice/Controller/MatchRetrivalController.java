package com.example.matchretrivalservice.Controller;

import com.example.matchretrivalservice.Model.UserProfile;
import com.example.matchretrivalservice.Service.MatchRetrivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchRetrivalController {

    private final MatchRetrivalService matchRetrivalService;

    @Autowired
    public MatchRetrivalController(MatchRetrivalService matchRetrivalService) {
        this.matchRetrivalService = matchRetrivalService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserProfile>> getMatches(@PathVariable Long userId) {
        List<UserProfile> matches = matchRetrivalService.getMatchesForUser(userId);
        return ResponseEntity.ok(matches);
    }

}


