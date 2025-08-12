package com.example.Clubhub3.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ClubWithAdminUniadminDTO {
    
    // Club fields
    @NotBlank(message = "Club name is required")
    @Size(max = 200, message = "Club name must not exceed 200 characters")
    private String clubName;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String clubDescription;
    
    @NotNull(message = "University is required")
    private UUID universityId;
    
    private String logoUrl;
    private String bannerUrl;
    
    // Club Admin fields
    @Valid
    private ClubAdminUniadminDTO clubAdmin;
    
    // For display purposes
    private String universityName;
    
    // Constructors
    public ClubWithAdminUniadminDTO() {
        this.clubAdmin = new ClubAdminUniadminDTO();
    }
    
    // Getters and Setters
    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }
    
    public String getClubDescription() { return clubDescription; }
    public void setClubDescription(String clubDescription) { this.clubDescription = clubDescription; }
    
    public UUID getUniversityId() { return universityId; }
    public void setUniversityId(UUID universityId) { 
        this.universityId = universityId;
        if (this.clubAdmin != null) {
            this.clubAdmin.setUniversityId(universityId);
        }
    }
    
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    
    public ClubAdminUniadminDTO getClubAdmin() { return clubAdmin; }
    public void setClubAdmin(ClubAdminUniadminDTO clubAdmin) { 
        this.clubAdmin = clubAdmin;
        if (clubAdmin != null && this.universityId != null) {
            clubAdmin.setUniversityId(this.universityId);
        }
    }
    
    public String getUniversityName() { return universityName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }
}

