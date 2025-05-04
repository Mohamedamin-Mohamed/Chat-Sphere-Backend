package com.ChatSphere.Backend.Controller;

import com.ChatSphere.Backend.Dto.UpdateProfileDto;
import com.ChatSphere.Backend.Dto.UserDto;
import com.ChatSphere.Backend.Services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@Slf4j
public class Profile {

    private final UserService userService;

    public Profile(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateProfile(@ModelAttribute @Valid UpdateProfileDto updateProfileDto,
                                                @RequestPart(value = "profilePictureDetails", required = false) MultipartFile multipartFile) {
        log.info("Updating profile for {}: ", updateProfileDto.getEmail());

        UserDto userDto = userService.updateProfile(updateProfileDto, multipartFile == null ? Optional.empty() : Optional.of(multipartFile));
        boolean profileUpdated = userDto != null;
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        response.put("message", profileUpdated ? "Profile has been updated successfully" : "Profile update failed");
        System.out.println(response);
        return new ResponseEntity<>(response, profileUpdated ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
