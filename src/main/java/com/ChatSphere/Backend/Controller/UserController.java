package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.SearchRequest;
import com.ChatSphere.Backend.Dto.UserSearchDTO;
import com.ChatSphere.Backend.Dto.UserStatsDto;
import com.ChatSphere.Backend.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users/")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String requesterEmail, @RequestParam String query) {
        log.info("Searching for users with the query {} requested by {}.", query, requesterEmail);
        List<UserSearchDTO> userSearchDtoList = userService.searchUsers(new SearchRequest(requesterEmail, query));
        return ResponseEntity.ok(userSearchDtoList);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats(@RequestParam String email) {
        log.info("Received request to get stats for user with email {}.", email);
        UserStatsDto userStatsDto = userService.getUserStats(email);
        return ResponseEntity.ok(userStatsDto);
    }
}
