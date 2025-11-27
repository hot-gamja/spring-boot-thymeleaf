package com.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * Photo domain model
 * Represents a photo entry with metadata stored in the database
 */
@Data
public class Photo {

    private Long id;
    private String title;
    private String description;
    private String tags;
    private String genre;
    private String color;
    private LocalDate shotDate;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
