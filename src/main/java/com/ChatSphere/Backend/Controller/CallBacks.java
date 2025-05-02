package com.ChatSphere.Backend.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@Slf4j
public class CallBacks {
    @PostMapping("/github/callback")
    public void callback(@RequestParam String code) {
        log.info("Received request for github callback: {}", code);
    }
}
