package com.nationalbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApplicationController {

    private final RestTemplate restTemplate;

    @Autowired
    public ApplicationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String getANBank(){
        return "You are connected to National Bank";
    }

    @GetMapping("/call-external")
    public ResponseEntity<String> callExternalApi() {
        String externalUrl = "http://127.0.0.1:8000";

        // GET request
        ResponseEntity<String> response = restTemplate.getForEntity(externalUrl, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
