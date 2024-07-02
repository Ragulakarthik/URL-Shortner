package com.example.url.controller;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.url.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UrlController {
	@Autowired
	private UrlService urlService;
	
	@PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String originalUrl) {
        String shortUrl = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortUrl);
	}
	
	@PostMapping("/update")
    public ResponseEntity<Boolean> updateShortUrl(@RequestParam String shortUrl, @RequestParam String newOriginalUrl) {
        boolean success = urlService.updateShortUrl(shortUrl, newOriginalUrl);
        return ResponseEntity.ok(success);
    }
	
	@GetMapping("/destination")
    public ResponseEntity<String> getOriginalUrl(@RequestParam String shortUrl) {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.ok(originalUrl);
    }
	
	@PostMapping("/updateExpiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        boolean success = urlService.updateExpiry(shortUrl, daysToAdd);
        return ResponseEntity.ok(success);
    }
	
	@GetMapping("/{shortenString}")
	public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString) {
		try {
            // Reconstruct the short URL from the path variable
            String shortUrl = "http://localhost:8080/" + shortenString;

            // Get the full URL from the service
            String fullUrl = urlService.getOriginalUrl(shortUrl);

            // Redirect to the full URL
            response.sendRedirect(fullUrl);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }
}
