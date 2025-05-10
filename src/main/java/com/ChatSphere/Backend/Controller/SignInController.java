package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.PasswordResetDto;
import com.ChatSphere.Backend.Dto.SignInDto;
import com.ChatSphere.Backend.Dto.UpdatePasswordDto;
import com.ChatSphere.Backend.Dto.UserDto;
import com.ChatSphere.Backend.Services.CodeGeneratorService;
import com.ChatSphere.Backend.Services.EmailService;
import com.ChatSphere.Backend.Services.RedisService;
import com.ChatSphere.Backend.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class SignInController {
    private final UserService userService;
    private final CodeGeneratorService codeGeneratorService;
    private final EmailService emailService;
    private final RedisService redisService;

    @PostMapping("signin/email")
    public ResponseEntity<Object> signInWithEmail(@RequestBody SignInDto signInDto) {
        log.info("Received request for {} to sign in", signInDto.getEmail());
        UserDto userDto = userService.signInWithEmail(signInDto);
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        response.put("message", "Login Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        log.info("Received request to verify code {} for {}", code, email);
        String cacheVerificationCode = redisService.getVerificationCodeFromCache(email);

        if (cacheVerificationCode == null) {
            return new ResponseEntity<>("Code expired, request a new one", HttpStatus.GONE);
        }

        if (cacheVerificationCode.equals(code)) {
            return new ResponseEntity<>("Verification code verified, reset password", HttpStatus.OK);
        }

        return new ResponseEntity<>("Wrong verification code", HttpStatus.CONFLICT);
    }


    @GetMapping("/email_lookup/generate_code")
    public ResponseEntity<String> generateCode(@RequestParam String email) {
        log.info("Received request to generate code for: {}", email);

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
        String code = codeGeneratorService.generateCode();


        if (!emailService.sendVerificationEmail(email, code)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code");
        }

        if (!redisService.addVerificationCodeToCache(email, code)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store code in cache");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Verification code sent successfully");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> handlePassword(@RequestBody Object passwordDto) {
        log.info("Received request to handle password {}", passwordDto);

        // Convert the generic Object to a Map to check for properties
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map passwordMap = mapper.convertValue(passwordDto, Map.class);

            // Check if it's PasswordResetDto based on its properties
            if (passwordMap.containsKey("email") && passwordMap.containsKey("password")) {
                PasswordResetDto passwordResetDto = mapper.convertValue(passwordDto, PasswordResetDto.class);
                log.info("Resetting password for {}", passwordResetDto.getEmail());
                boolean passwordChanged = userService.resetPassword(passwordResetDto);

                if (passwordChanged) {
                    return new ResponseEntity<>("Password reset successfully", HttpStatus.CREATED);
                }
                return new ResponseEntity<>("Error resetting password", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check if it's UpdatePasswordDto based on its properties
            else if (passwordMap.containsKey("currentPassword") && passwordMap.containsKey("newPassword")) {
                UpdatePasswordDto updatePasswordDto = mapper.convertValue(passwordDto, UpdatePasswordDto.class);
                log.info("Received request to update password for {}", updatePasswordDto.getEmail());
                boolean passwordChanged = userService.resetPassword(updatePasswordDto);

                if (passwordChanged) {
                    return new ResponseEntity<>("Password updated successfully", HttpStatus.CREATED);
                }
                return new ResponseEntity<>("Incorrect password provided", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.error("Error converting request payload", e);
            return new ResponseEntity<>("Invalid request format", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Hey there all is fine";
    }
}
