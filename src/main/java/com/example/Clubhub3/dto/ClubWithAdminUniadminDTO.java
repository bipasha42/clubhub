package com.example.Clubhub3.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.UUID;

@Getter
@Setter

public class ClubWithAdminUniadminDTO {

    // Getters and Setters
    // Club fields
    @Setter
    @NotBlank(message = "Club name is required")
    @Size(max = 200, message = "Club name must not exceed 200 characters")
    private String clubName;
    
    @Setter
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String clubDescription;
    
    @NotNull(message = "University is required")
    private UUID universityId;
    
    @Setter
    private String logoUrl;
    @Setter
    private String bannerUrl;
    
    // Club Admin fields
    @Valid
    private ClubAdminUniadminDTO clubAdmin;
    
    // For display purposes
    @Setter
    private String universityName;
    
    // Constructors
    public ClubWithAdminUniadminDTO() {
        this.clubAdmin = new ClubAdminUniadminDTO();
    }

    public void setUniversityId(UUID universityId) {
        this.universityId = universityId;
        if (this.clubAdmin != null) {
            this.clubAdmin.setUniversityId(universityId);
        }
    }

    public void setClubAdmin(ClubAdminUniadminDTO clubAdmin) {
        this.clubAdmin = clubAdmin;
        if (clubAdmin != null && this.universityId != null) {
            clubAdmin.setUniversityId(this.universityId);
        }
    }

}

