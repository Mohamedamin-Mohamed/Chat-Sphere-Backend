package com.ChatSphere.Backend.Controllers;

import com.ChatSphere.Backend.Dto.OAuthSignUpRequestDto;
import com.ChatSphere.Backend.Dto.SignUpRequestDto;
import com.ChatSphere.Backend.Dto.UserDto;
import com.ChatSphere.Backend.Model.User;
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
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Object> signUpWithOauth(@RequestBody OAuthSignUpRequestDto oAuthSignUpRequest) throws ParseException {
        log.info("Received request for {} to sign up with oauth provider {}", oAuthSignUpRequest.getEmail(), oAuthSignUpRequest.getOauthProvider());
        if (oAuthSignUpRequest.getOauthProvider().equals("Apple")) {
            /*if this is true it means it not the first time using is signing in with Apple so apple won't return users
            email again on subsequent logins. In this case link the stored user oauth id with the request's oauth id. */
            if (oAuthSignUpRequest.getEmail() == null) {
                log.info("Linking {} apple account with oauth id", oAuthSignUpRequest.getName());
                User user = userService.findByOauthId(oAuthSignUpRequest.getOauthId());
                oAuthSignUpRequest.setEmail(user.getEmail());
                oAuthSignUpRequest.setName(user.getName());
            }
        }
        UserDto userDto = userService.signUpWithOauth(oAuthSignUpRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        response.put("message", "Account created Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
