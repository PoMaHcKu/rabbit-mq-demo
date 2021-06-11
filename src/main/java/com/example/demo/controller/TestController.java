package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping()
    public ResponseEntity<String> giveFile(@RequestParam(required = false) boolean on) {
        return new ResponseEntity<>(String.valueOf(on), HttpStatus.OK);
    }
}