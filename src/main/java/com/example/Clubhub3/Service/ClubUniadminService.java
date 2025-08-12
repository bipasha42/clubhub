package com.example.Clubhub3.Service;

import com.example.Clubhub3.dto.ClubUniadminDTO;
import com.example.Clubhub3.dto.ClubWithAdminUniadminDTO;
import com.example.Clubhub3.model.Club;
import com.example.Clubhub3.model.University;
import com.example.Clubhub3.model.User;
import com.example.Clubhub3.repo.ClubUniadminRepository;
import com.example.Clubhub3.repo.UniversityUniadminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClubUniadminService {

    @Autowired
    private ClubUniadminRepository clubRepository;

    @Autowired
    private UniversityUniadminRepository universityRepository;

    @Autowired
    private ClubAdminUniadminService clubAdminService;

    public List<ClubUniadminDTO> getAllClubs() {
        return clubRepository.findAllWithUniversityAndAdmin()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClubUniadminDTO getClubById(UUID id) {
        Club club = clubRepository.findByIdWithUniversityAndAdmin(id);
        return club != null ? convertToDTO(club) : null;
    }

    public Club getClubEntityById(UUID id) {
        return clubRepository.findById(id).orElse(null);
    }

    public List<ClubUniadminDTO> getClubsByUniversity(UUID universityId) {
        return clubRepository.findByUniversityId(universityId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClubUniadminDTO createClub(ClubUniadminDTO clubDTO) {
        University university = universityRepository.findById(clubDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found"));

        User admin = clubAdminService.getUserById(clubDTO.getAdminId());
        if (admin == null) {
            throw new RuntimeException("Club admin not found");
        }

        if (clubRepository.existsByNameAndUniversity(clubDTO.getName(), university)) {
            throw new RuntimeException("Club with this name already exists in the university");
        }

        Club club = new Club();
        club.setName(clubDTO.getName());
        club.setDescription(clubDTO.getDescription());
        club.setUniversity(university);
        club.setAdmin(admin);
        club.setLogoUrl(clubDTO.getLogoUrl());
        club.setBannerUrl(clubDTO.getBannerUrl());

        Club savedClub = clubRepository.save(club);
        return convertToDTO(savedClub);
    }

    public ClubUniadminDTO createClubWithAdmin(ClubWithAdminUniadminDTO clubWithAdminDTO) {
        // Create the club admin and get the entity back
        User clubAdmin = clubAdminService.createClubAdminEntity(clubWithAdminDTO.getClubAdmin());

        // Then create the club with the new admin
        University university = universityRepository.findById(clubWithAdminDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found"));

        if (clubRepository.existsByNameAndUniversity(clubWithAdminDTO.getClubName(), university)) {
            throw new RuntimeException("Club with this name already exists in the university");
        }

        Club club = new Club();
        club.setName(clubWithAdminDTO.getClubName());
        club.setDescription(clubWithAdminDTO.getClubDescription());
        club.setUniversity(university);
        club.setAdmin(clubAdmin);
        club.setLogoUrl(clubWithAdminDTO.getLogoUrl());
        club.setBannerUrl(clubWithAdminDTO.getBannerUrl());

        Club savedClub = clubRepository.save(club);
        return convertToDTO(savedClub);
    }
    public ClubUniadminDTO updateClub(UUID id, ClubUniadminDTO clubDTO) {
    Club club = clubRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Club not found"));

    University university = universityRepository.findById(clubDTO.getUniversityId())
            .orElseThrow(() -> new RuntimeException("University not found"));

    User admin = clubAdminService.getUserById(clubDTO.getAdminId());
    if (admin == null) {
        throw new RuntimeException("Club admin not found");
    }

    // Check uniqueness if either the name or the university changed
    boolean nameChanged = !club.getName().equals(clubDTO.getName());
    boolean universityChanged = club.getUniversity() == null
            || !club.getUniversity().getId().equals(clubDTO.getUniversityId());

    if ((nameChanged || universityChanged)
            && clubRepository.existsByNameAndUniversity(clubDTO.getName(), university)) {
        throw new RuntimeException("Club with this name already exists in the university");
    }

    club.setName(clubDTO.getName());
    club.setDescription(clubDTO.getDescription());
    club.setUniversity(university);
    club.setAdmin(admin);
    club.setLogoUrl(clubDTO.getLogoUrl());
    club.setBannerUrl(clubDTO.getBannerUrl());

    Club savedClub = clubRepository.save(club);
    return convertToDTO(savedClub);
}

    public void deleteClub(UUID id) {
        if (!clubRepository.existsById(id)) {
            throw new RuntimeException("Club not found");
        }
        clubRepository.deleteById(id);
    }

    private ClubUniadminDTO convertToDTO(Club club) {
        ClubUniadminDTO dto = new ClubUniadminDTO();
        dto.setId(club.getId());
        dto.setName(club.getName());
        dto.setDescription(club.getDescription());
        dto.setLogoUrl(club.getLogoUrl());
        dto.setBannerUrl(club.getBannerUrl());

        if (club.getUniversity() != null) {
            dto.setUniversityId(club.getUniversity().getId());
            dto.setUniversityName(club.getUniversity().getName());
        }

        if (club.getAdmin() != null) {
            dto.setAdminId(club.getAdmin().getId());
            dto.setAdminName(club.getAdmin().getFirstName() + " " + club.getAdmin().getLastName());
        }

        return dto;
    }
}