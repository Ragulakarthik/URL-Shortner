package com.example.url.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.url.entity.UrlEntity;
import com.example.url.urlrepository.UrlRepository;
import com.google.common.hash.Hashing;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;


@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;
    private final Map<String, UrlEntity> urlCache = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(UrlService.class.getName());
    private static final String CSV_FILE_PATH = "urls.csv";

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        loadCsvData();
    }

    private void loadCsvData() {
        try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            String[] values;
            boolean firstRow = true; // Flag to skip the header row
            while ((values = csvReader.readNext()) != null) {
                if (firstRow) {
                    firstRow = false; // Skip the header row
                    continue;
                }
                UrlEntity urlEntity = new UrlEntity();
                urlEntity.setId(Long.parseLong(values[0])); // Ensure that the CSV file has valid numeric IDs
                urlEntity.setOriginalUrl(values[1]);
                urlEntity.setShortUrl(values[2]);
                urlEntity.setCreatedAt(Timestamp.valueOf(values[3]));  // Convert the string to Timestamp
                urlEntity.setExpiresAt(Timestamp.valueOf(values[4]));
                urlCache.put(values[2], urlEntity);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Error while reading CSV file", e);
            throw new RuntimeException("Error reading CSV file", e); // Propagate exception with runtime exception
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Number format error while parsing CSV file", e);
            throw new RuntimeException("Error parsing number from CSV file", e); // Propagate exception with runtime exception
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Illegal argument error while parsing CSV file", e);
            throw new RuntimeException("Invalid data in CSV file", e); // Propagate exception with runtime exception
        } catch (CsvException e) {
            LOGGER.log(Level.SEVERE, "CSV Parsing Error", e);
            throw new RuntimeException("Error parsing CSV file", e); // Propagate exception with runtime exception
        }
    }


    private void saveToCsv() {
        try (FileWriter fileWriter = new FileWriter(CSV_FILE_PATH, false);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            // Write the header row
            csvWriter.writeNext(new String[]{"id", "original_url", "short_url", "created_at", "expires_at"});

            for (UrlEntity urlEntity : urlCache.values()) {
                csvWriter.writeNext(new String[]{
                    String.valueOf(urlEntity.getId()),
                    urlEntity.getOriginalUrl(),
                    urlEntity.getShortUrl(),
                    urlEntity.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),  // Format timestamp
                    urlEntity.getExpiresAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   // Format timestamp
                });
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Error while writing CSV file", e);
            throw new RuntimeException("Error writing CSV file", e); // Propagate exception with runtime exception
        }
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
            saveToCsv();  // Save changes to CSV if the URL is expired
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
        calendar.add(Calendar.MONTH, 10);
        Timestamp expiresAt = new Timestamp(calendar.getTimeInMillis());

        url.setCreatedAt(createdAt);
        url.setExpiresAt(expiresAt);

        String shortUrl = generateShortUrl(originalUrl, System.currentTimeMillis());
        url.setShortUrl(shortUrl);

        url = urlRepository.save(url);
        urlCache.put(shortUrl, url);
        saveToCsv();  // Save changes to CSV file

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
            saveToCsv();  // Save changes to CSV file
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
            saveToCsv();  // Save changes to CSV file
            return true;
        } else {
            return false;
        }
    }
}
