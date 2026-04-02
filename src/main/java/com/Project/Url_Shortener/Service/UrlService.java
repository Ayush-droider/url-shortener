package com.Project.Url_Shortener.Service;


import com.Project.Url_Shortener.DTO.UrlRequest;
import com.Project.Url_Shortener.DTO.UrlResponse;
import com.Project.Url_Shortener.Models.Url;

public interface UrlService {
    UrlResponse shortenUrl(UrlRequest request);
    String getOriginalUrl(String shortCode);
    Url getStats(String shortCode);
}
