package com.Project.Url_Shortener.Service;


import com.Project.Url_Shortener.DTO.UrlRequest;
import com.Project.Url_Shortener.DTO.UrlResponse;
import com.Project.Url_Shortener.Exceptions.BadRequestException;
import com.Project.Url_Shortener.Exceptions.ResourceNotFoundException;
import com.Project.Url_Shortener.Models.Url;
import com.Project.Url_Shortener.Repository.UrlRepository;
import com.Project.Url_Shortener.Util.Base62Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    public UrlResponse shortenUrl(UrlRequest request) {

        System.out.println("Alias: " + request.getCustomAlias());
        String shortCode;
        if (request.getCustomAlias() != null && !request.getCustomAlias().isEmpty()) {
            if (urlRepository.findByShortCode(request.getCustomAlias()).isPresent()) {
                throw new BadRequestException("Custom alias already taken");
            }
            shortCode = request.getCustomAlias();
        } else {
            Url temp = Url.builder()
                    .longUrl(request.getLongUrl())
                    .createdAt(LocalDateTime.now())
                    .expiryTime(getExpiry(request))
                    .clickCount(0L)
                    .build();
            temp = urlRepository.save(temp);
            shortCode = Base62Util.encode(temp.getId());
            temp.setShortCode(shortCode);
            urlRepository.save(temp);
            return buildResponse(temp);
        }
        Url url = Url.builder()
                .longUrl(request.getLongUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .expiryTime(getExpiry(request))
                .clickCount(0L)
                .build();

        urlRepository.save(url);

        return buildResponse(url);
    }

    private LocalDateTime getExpiry(UrlRequest request) {
        if (request.getExpiryTime() != null) {
            return request.getExpiryTime();
        }
        return LocalDateTime.now().plusDays(7);
    }

    private UrlResponse buildResponse(Url url) {
        return UrlResponse.builder()
                .shortUrl("http://localhost:8080/" + url.getShortCode())
                .longUrl(url.getLongUrl())
                .build();
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));

        if (url.getExpiryTime() != null &&
                url.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("URL expired");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getLongUrl();
    }

    @Override
    public Url getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));
    }
}
