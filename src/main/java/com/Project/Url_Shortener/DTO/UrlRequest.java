package com.Project.Url_Shortener.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UrlRequest {

    @NotBlank(message = "URL cannot be empty")
    private String longUrl;

    private String customAlias;

    private LocalDateTime expiryTime;
}