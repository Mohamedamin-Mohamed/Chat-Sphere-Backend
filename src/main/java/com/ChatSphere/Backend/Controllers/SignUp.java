package com.ChatSphere.Backend.Controllers;

import com.ChatSphere.Backend.Dto.OAuthSignUpRequestDto;
import com.ChatSphere.Backend.Dto.SignUpRequestDto;
import com.ChatSphere.Backend.Dto.UserDto;
import com.ChatSphere.Backend.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth/signup")
@RequiredArgsConstructor
@Slf4j
public class SignUp {

    private final UserService userService;

    @PostMapping("/email")
    public ResponseEntity<String> signUpWithEmail(@RequestBody SignUpRequestDto signUpRequest) throws ParseException {
        log.info("Received request for {} to sign up with email", signUpRequest.getEmail());
        UserDto userDto = userService.signUpWithEmail(signUpRequest);
        System.out.println("User dto is " + userDto);
        if (userDto != null) {
            return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to create account try again", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/oauth")
    public ResponseEntity<String> signUpWithOauth(@RequestBody OAuthSignUpRequestDto oAuthSignUpRequest) throws ParseException {
        log.info("Received request for {} to sign up with oauth provider {}", oAuthSignUpRequest.getEmail(), oAuthSignUpRequest.getOauthProvider());
        UserDto userDto = userService.signUpWithOauth(oAuthSignUpRequest);

        if (userDto != null) {
            return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to create account try again", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
