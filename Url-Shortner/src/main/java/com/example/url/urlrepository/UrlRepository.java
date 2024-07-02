package com.example.url.urlrepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.url.entity.UrlEntity;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long>{
	Optional<UrlEntity> findByShortUrl(String shortUrl);
}
