package com.example.service;

import com.example.mapper.PhotoMapper;
import com.example.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Photo Service layer
 */
@Service
@Transactional
public class PhotoService {

    private final PhotoMapper photoMapper;

    public PhotoService(PhotoMapper photoMapper) {
        this.photoMapper = photoMapper;
    }

    /**
     * Get all photos
     */
    @Transactional(readOnly = true)
    public List<Photo> getAllPhotos() {
        return photoMapper.findAll();
    }

    /**
     * Get photo by ID
     */
    @Transactional(readOnly = true)
    public Photo getPhoto(Long id) {
        return photoMapper.findById(id);
    }

    /**
     * Create new photo
     */
    public void createPhoto(Photo photo) {
        if (photo.getTitle() == null || photo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        photoMapper.insert(photo);
    }

    /**
     * Update existing photo
     */
    public void updatePhoto(Photo photo) {
        Photo existingPhoto = photoMapper.findById(photo.getId());
        if (existingPhoto == null) {
            throw new IllegalArgumentException("Photo not found with id: " + photo.getId());
        }
        photoMapper.update(photo);
    }

    /**
     * Delete photo by ID
     */
    public void deletePhoto(Long id) {
        Photo existingPhoto = photoMapper.findById(id);
        if (existingPhoto == null) {
            throw new IllegalArgumentException("Photo not found with id: " + id);
        }
        photoMapper.delete(id);
    }
}
