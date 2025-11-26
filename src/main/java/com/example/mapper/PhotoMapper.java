package com.example.mapper;

import com.example.model.Photo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * MyBatis mapper interface for Photo entity
 */
@Mapper
public interface PhotoMapper {

    /**
     * Find all photos ordered by id descending
     */
    List<Photo> findAll();

    /**
     * Find a photo by id
     */
    Photo findById(Long id);

    /**
     * Insert a new photo
     */
    void insert(Photo photo);

    /**
     * Update an existing photo
     */
    void update(Photo photo);

    /**
     * Delete a photo by id
     */
    void delete(Long id);
}
