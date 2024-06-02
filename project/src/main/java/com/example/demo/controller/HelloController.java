package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping("/{name}")
    public ResponseEntity<MessageDto> sayHello(@PathVariable String name) {
        MessageDto messageDto = new MessageDto("Hello, " + name);
        return ResponseEntity.ok(messageDto);
    }
}
