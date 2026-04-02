package com.Project.Url_Shortener.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlResponse {
    private String shortUrl;
    private String longUrl;
}
