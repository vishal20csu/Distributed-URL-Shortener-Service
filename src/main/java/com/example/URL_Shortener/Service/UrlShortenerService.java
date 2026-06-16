package com.example.URL_Shortener.Service;


import com.example.URL_Shortener.DTO.ShortenRequest;
import com.example.URL_Shortener.Entities.ShortUrl;
import com.example.URL_Shortener.Repository.ShortUrlRepository;
import com.example.URL_Shortener.Utilities.Base62Encoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlShortenerService {
    private ShortUrlRepository repository;
    public UrlShortenerService(ShortUrlRepository repository){
        this.repository=repository;
    }
    public String createShortUrl(ShortenRequest request){

        ShortUrl url= new ShortUrl();
        url.setLongUrl(request.originalUrl());
        if (request.duration() != null) {
            url.setExpiresAt(LocalDateTime.now().plusMinutes(request.duration()));
        }
        ShortUrl savedUrl= repository.save(url);
        String shortCode= Base62Encoder.encode(savedUrl.getId());
        savedUrl.setShortUrl(shortCode);
        repository.save(savedUrl);

        return shortCode;

    }
    public String getOriginalUrl(String shortCode) {
        ShortUrl entity= repository.findByShortUrl(shortCode)
                .orElseThrow(()->new RuntimeException("Shorten url code not found or invalid!!"));
        if(entity.getExpiresAt()!=null && LocalDateTime.now().isAfter(entity.getExpiresAt()) ){
            throw new RuntimeException("The link is expired ");
        }
        return entity.getLongUrl();
    }
}

