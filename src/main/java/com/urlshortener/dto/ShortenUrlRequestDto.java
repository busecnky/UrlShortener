package com.urlshortener.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShortenUrlRequestDto {
    @NotNull(message = "LongUrl cannot be null")
    @NotBlank(message = "LongUrl cannot be blank")
    private String longUrl;
    private String shortId;
    @Min(value = 0, message = "TTL time cannot be a negative number")
    private Long ttlInSeconds;


}
