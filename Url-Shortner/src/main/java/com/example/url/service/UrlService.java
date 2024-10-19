package com.example.url.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.url.entity.UrlEntity;
import com.example.url.urlrepository.UrlRepository;
import com.google.common.hash.Hashing;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;
    private final Map<String, UrlEntity> urlCache = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(UrlService.class.getName());

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        loadUrlCache(); // Load the existing URLs into cache from the database
    }

    private void loadUrlCache() {
        // Load existing URLs from the database into the cache
        urlRepository.findAll().forEach(urlEntity -> urlCache.put(urlEntity.getShortUrl(), urlEntity));
    }

    public String getOriginalUrl(String shortUrl) {
        UrlEntity urlEntity = urlCache.get(shortUrl);
        if (urlEntity == null) {
            Optional<UrlEntity> optionalUrlEntity = urlRepository.findByShortUrl(shortUrl);
            if (optionalUrlEntity.isPresent()) {
                urlEntity = optionalUrlEntity.get();
                urlCache.put(shortUrl, urlEntity);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found or expired");
            }
        }

        if (urlEntity.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
            urlCache.remove(shortUrl);
            urlRepository.delete(urlEntity);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL expired");
        }

        return urlEntity.getOriginalUrl();
    }

    public String shortenUrl(String originalUrl) {
        UrlEntity url = new UrlEntity();
        url.setOriginalUrl(originalUrl);

        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);
        calendar.add(Calendar.MONTH, 1);
        Timestamp expiresAt = new Timestamp(calendar.getTimeInMillis());

        url.setCreatedAt(createdAt);
        url.setExpiresAt(expiresAt);

        String shortUrl = generateShortUrl(originalUrl, System.currentTimeMillis());
        url.setShortUrl(shortUrl);

        url = urlRepository.save(url);
        urlCache.put(shortUrl, url);

        return shortUrl;
    }

    private String generateShortUrl(String originalUrl, long uniqueId) {
        String baseUrl = "http://localhost:8080/";
        String uniquePart = Hashing.sha256()
            .hashString(originalUrl + uniqueId, java.nio.charset.StandardCharsets.UTF_8)
            .toString();
        // Ensure that the unique part is of the required length
        int maxLength = 30 - baseUrl.length();
        if (uniquePart.length() > maxLength) {
            uniquePart = uniquePart.substring(0, maxLength);
        }
        return baseUrl + uniquePart;
    }

    public boolean updateShortUrl(String shortUrl, String newOriginalUrl) {
        Optional<UrlEntity> url = urlRepository.findByShortUrl(shortUrl);
        if (url.isPresent()) {
            UrlEntity existingUrl = url.get();
            existingUrl.setOriginalUrl(newOriginalUrl);
            urlRepository.save(existingUrl);
            urlCache.put(shortUrl, existingUrl);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateExpiry(String shortUrl, int daysToAdd) {
        Optional<UrlEntity> url = urlRepository.findByShortUrl(shortUrl);
        if (url.isPresent()) {
            UrlEntity existingUrl = url.get();
            Timestamp newExpiry = new Timestamp(existingUrl.getExpiresAt().getTime());
            newExpiry.setTime(newExpiry.getTime() + daysToAdd * 86400000L);
            existingUrl.setExpiresAt(newExpiry);
            urlRepository.save(existingUrl);
            urlCache.put(shortUrl, existingUrl);
            return true;
        } else {
            return false;
        }
    }
}
