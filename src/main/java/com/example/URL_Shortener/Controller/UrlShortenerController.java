package com.example.URL_Shortener.Controller;

import com.example.URL_Shortener.DTO.ShortenRequest;
import com.example.URL_Shortener.Service.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
public class UrlShortenerController {

    private final UrlShortenerService shortenerService;

    public UrlShortenerController(UrlShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }
    @PostMapping("/")
    public ResponseEntity<String> shortenUrl(@RequestBody ShortenRequest request ){
            // Hand off the business logic to our service layer
            String shortCode = shortenerService.createShortUrl(request);

            // Construct the final URL to give back to the client
            String fullShortenedUrl = "http://localhost:8080/" + shortCode;
            return ResponseEntity.ok(fullShortenedUrl);
        }
        @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl= shortenerService.getOriginalUrl(shortCode);
            return ResponseEntity.status(HttpStatus.FOUND) // 302 Found Redirection
                    .location(URI.create(originalUrl))
                    .build();
    }


}
