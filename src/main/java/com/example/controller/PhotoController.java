package com.example.controller;

import com.example.config.PhotoProperties;
import com.example.model.Photo;
import com.example.service.PhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Photo Controller for handling photo-related web requests
 */
@Controller
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoProperties photoProperties;

    public PhotoController(PhotoService photoService, PhotoProperties photoProperties) {
        this.photoService = photoService;
        this.photoProperties = photoProperties;
    }

    /**
     * List all photos
     * GET /photos
     */
    @GetMapping
    public String listPhotos(Model model) {
        model.addAttribute("photos", photoService.getAllPhotos());
        return "photos/list";
    }

    /**
     * View photo detail
     * GET /photos/{id}
     */
    @GetMapping("/{id}")
    public String viewPhoto(@PathVariable Long id, Model model) {
        Photo photo = photoService.getPhoto(id);
        if (photo == null) {
            return "redirect:/photos";
        }
        model.addAttribute("photo", photo);
        return "photos/detail";
    }

    /**
     * Show create photo form
     * GET /photos/new
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("photo", new Photo());
        return "photos/form";
    }

    /**
     * Create new photo
     * POST /photos
     */
    @PostMapping
    public String createPhoto(@ModelAttribute Photo photo,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        try {
            // Handle file upload
            if (file != null && !file.isEmpty()) {
                String imagePath = saveUploadedFile(file);
                photo.setImagePath(imagePath);
            }

            // Set timestamps
            LocalDateTime now = LocalDateTime.now();
            photo.setCreatedAt(now);
            photo.setUpdatedAt(now);

            photoService.createPhoto(photo);
            redirectAttributes.addFlashAttribute("message", "Photo created successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error creating photo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/photos";
    }

    /**
     * Show edit photo form
     * GET /photos/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Photo photo = photoService.getPhoto(id);
        if (photo == null) {
            return "redirect:/photos";
        }
        model.addAttribute("photo", photo);
        return "photos/form";
    }

    /**
     * Update photo
     * POST /photos/{id}
     */
    @PostMapping("/{id}")
    public String updatePhoto(@PathVariable Long id,
                            @ModelAttribute Photo photo,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        try {
            // Fetch existing photo
            Photo existingPhoto = photoService.getPhoto(id);
            if (existingPhoto == null) {
                redirectAttributes.addFlashAttribute("message", "Photo not found");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/photos";
            }

            // Update fields
            existingPhoto.setTitle(photo.getTitle());
            existingPhoto.setDescription(photo.getDescription());
            existingPhoto.setTags(photo.getTags());
            existingPhoto.setGenre(photo.getGenre());
            existingPhoto.setColor(photo.getColor());
            existingPhoto.setShotDate(photo.getShotDate());

            // Handle new file upload
            if (file != null && !file.isEmpty()) {
                String imagePath = saveUploadedFile(file);
                existingPhoto.setImagePath(imagePath);
            }

            // Update timestamp
            existingPhoto.setUpdatedAt(LocalDateTime.now());

            photoService.updatePhoto(existingPhoto);
            redirectAttributes.addFlashAttribute("message", "Photo updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/photos/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating photo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/photos";
        }
    }

    /**
     * Delete photo
     * POST /photos/{id}/delete
     */
    @PostMapping("/{id}/delete")
    public String deletePhoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            photoService.deletePhoto(id);
            redirectAttributes.addFlashAttribute("message", "Photo deleted successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting photo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/photos";
    }

    /**
     * Save uploaded file to disk
     * Returns browser-accessible path (/images/yyyy/MM/filename)
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        // Create date-based directory structure: yyyy/MM
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyy/MM"));

        Path uploadPath = Paths.get(photoProperties.getUploadDir(), yearMonth);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename: UUID + original extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath.toFile());

        // Return browser-accessible path
        return "/images/" + yearMonth + "/" + filename;
    }
}
