package com.Project.Url_Shortener.Controller;


import com.Project.Url_Shortener.DTO.UrlRequest;
import com.Project.Url_Shortener.DTO.UrlResponse;
import com.Project.Url_Shortener.Models.Url;
import com.Project.Url_Shortener.Service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @GetMapping("/stats/{shortCode}")
    public Url stats(@PathVariable String shortCode) {
        return urlService.getStats(shortCode);
    }

    @PostMapping("/shorten")
    public UrlResponse shorten(@Valid @RequestBody UrlRequest request) {
        return urlService.shortenUrl(request);
    }
}
