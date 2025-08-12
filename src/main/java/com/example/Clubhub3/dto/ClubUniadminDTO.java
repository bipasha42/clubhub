package com.example.Clubhub3.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ClubUniadminDTO {
    
    private UUID id;
    
    @NotBlank(message = "Club name is required")
    @Size(max = 200, message = "Club name must not exceed 200 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "University is required")
    private UUID universityId;
    
    @NotNull(message = "Admin is required")
    private UUID adminId;
    
    private String logoUrl;
    private String bannerUrl;
    
    // For display purposes
    private String universityName;
    private String adminName;
    
    // Constructors
    public ClubUniadminDTO() {}
    
    public ClubUniadminDTO(String name, String description, UUID universityId, UUID adminId) {
        this.name = name;
        this.description = description;
        this.universityId = universityId;
        this.adminId = adminId;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public UUID getUniversityId() { return universityId; }
    public void setUniversityId(UUID universityId) { this.universityId = universityId; }
    
    public UUID getAdminId() { return adminId; }
    public void setAdminId(UUID adminId) { this.adminId = adminId; }
    
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    
    public String getUniversityName() { return universityName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }
    
    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
}

