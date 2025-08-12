package com.example.Clubhub3.Service;

import com.example.Clubhub3.dto.ClubAdminUniadminDTO;
import com.example.Clubhub3.model.University;
import com.example.Clubhub3.model.User;
import com.example.Clubhub3.model.UserRole;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import com.example.Clubhub3.repo.UniversityUniadminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClubAdminUniadminService {
    
    @Autowired
    private ClubAdminUniadminRepository clubAdminRepository;
    
    @Autowired
    private UniversityUniadminRepository universityRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<ClubAdminUniadminDTO> getAllClubAdmins() {
        return clubAdminRepository.findByRoleWithUniversity(UserRole.CLUB_ADMIN)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ClubAdminUniadminDTO getClubAdminById(UUID id) {
        User user = clubAdminRepository.findByIdWithUniversity(id);
        return user != null && user.getRole() == UserRole.CLUB_ADMIN ? convertToDTO(user) : null;
    }
    
    public User getUserById(UUID id) {
        return clubAdminRepository.findById(id).orElse(null);
    }
    
    public List<ClubAdminUniadminDTO> getClubAdminsByUniversity(UUID universityId) {
        University university = universityRepository.findById(universityId).orElse(null);
        if (university == null) return List.of();
        
        return clubAdminRepository.findByRoleAndUniversity(UserRole.CLUB_ADMIN, university)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Keep this one if you want a DTO back
    public ClubAdminUniadminDTO createClubAdmin(ClubAdminUniadminDTO clubAdminDTO) {
        User savedUser = createAndSaveClubAdmin(clubAdminDTO);
        return convertToDTO(savedUser);
    }

    // Renamed to avoid duplicate signature (returns entity)
    public User createClubAdminEntity(ClubAdminUniadminDTO clubAdminDTO) {
        return createAndSaveClubAdmin(clubAdminDTO);
    }

    private User createAndSaveClubAdmin(ClubAdminUniadminDTO clubAdminDTO) {
        if (clubAdminRepository.existsByEmail(clubAdminDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        University university = universityRepository.findById(clubAdminDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found"));
        
        User user = new User();
        user.setEmail(clubAdminDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(clubAdminDTO.getPassword()));
        user.setFirstName(clubAdminDTO.getFirstName());
        user.setLastName(clubAdminDTO.getLastName());
        user.setRole(UserRole.CLUB_ADMIN);
        user.setUniversity(university);
        user.setBio(clubAdminDTO.getBio());
        
        return clubAdminRepository.save(user);
    }
    
    public ClubAdminUniadminDTO updateClubAdmin(UUID id, ClubAdminUniadminDTO clubAdminDTO) {
        User user = clubAdminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club admin not found"));
        
        if (user.getRole() != UserRole.CLUB_ADMIN) {
            throw new RuntimeException("User is not a club admin");
        }
        
        if (!user.getEmail().equals(clubAdminDTO.getEmail()) && 
            clubAdminRepository.existsByEmail(clubAdminDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        University university = universityRepository.findById(clubAdminDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found"));
        
        user.setEmail(clubAdminDTO.getEmail());
        if (clubAdminDTO.getPassword() != null && !clubAdminDTO.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(clubAdminDTO.getPassword()));
        }
        user.setFirstName(clubAdminDTO.getFirstName());
        user.setLastName(clubAdminDTO.getLastName());
        user.setUniversity(university);
        user.setBio(clubAdminDTO.getBio());
        
        User savedUser = clubAdminRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public void deleteClubAdmin(UUID id) {
        User user = clubAdminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club admin not found"));
        
        if (user.getRole() != UserRole.CLUB_ADMIN) {
            throw new RuntimeException("User is not a club admin");
        }
        
        if (user.getAdminClubs() != null && !user.getAdminClubs().isEmpty()) {
            throw new RuntimeException("Cannot delete club admin who is managing clubs");
        }
        
        clubAdminRepository.deleteById(id);
    }
    
    public List<User> getAvailableClubAdmins(UUID universityId) {
        University university = universityRepository.findById(universityId).orElse(null);
        if (university == null) return List.of();
        
        return clubAdminRepository.findByRoleAndUniversity(UserRole.CLUB_ADMIN, university);
    }
    
    private ClubAdminUniadminDTO convertToDTO(User user) {
        ClubAdminUniadminDTO dto = new ClubAdminUniadminDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        
        if (user.getUniversity() != null) {
            dto.setUniversityId(user.getUniversity().getId());
            dto.setUniversityName(user.getUniversity().getName());
        }
        
        return dto;
    }
}