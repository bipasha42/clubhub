package com.example.clubhub.controller;

import com.example.clubhub.model.Club;
import com.example.clubhub.model.University;
import com.example.clubhub.model.User;
import com.example.clubhub.model.Post;
import com.example.clubhub.repository.ClubRepository;
import com.example.clubhub.repository.UniversityRepository;
import com.example.clubhub.repository.UserRepository;
import com.example.clubhub.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin(origins = "*")
public class ClubController {
    
    private final ClubRepository clubRepository;
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public ClubController(ClubRepository clubRepository, 
                         UniversityRepository universityRepository,
                         UserRepository userRepository, 
                         PostRepository postRepository) {
        this.clubRepository = clubRepository;
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // GET all clubs
    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        try {
            List<Club> clubs = clubRepository.findAll();
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET club by ID
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable UUID id) {
        try {
            Optional<Club> club = clubRepository.findById(id);
            if (club.isPresent()) {
                return new ResponseEntity<>(club.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST create new club
    @PostMapping
    public ResponseEntity<Club> createClub(@Validated @RequestBody ClubRequest request) {
        try {
            Optional<University> university = universityRepository.findById(request.getUniversityId());
            if (!university.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Club club = new Club();
            club.setId(UUID.randomUUID());
            club.setName(request.getName());
            club.setDescription(request.getDescription());
            club.setCategory(request.getCategory());
            club.setContactEmail(request.getContactEmail());
            club.setIsApproved(false); // Default to not approved
            club.setCreatedAt(Timestamp.from(Instant.now()));
            club.setUpdatedAt(Timestamp.from(Instant.now()));
            club.setUniversity(university.get());

            Club savedClub = clubRepository.save(club);
            return new ResponseEntity<>(savedClub, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT update club
    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable UUID id, 
                                         @Validated @RequestBody ClubRequest request) {
        try {
            Optional<Club> optionalClub = clubRepository.findById(id);
            if (optionalClub.isPresent()) {
                Club club = optionalClub.get();
                club.setName(request.getName());
                club.setDescription(request.getDescription());
                club.setCategory(request.getCategory());
                club.setContactEmail(request.getContactEmail());
                club.setUpdatedAt(Timestamp.from(Instant.now()));

                if (request.getUniversityId() != null) {
                    Optional<University> university = universityRepository.findById(request.getUniversityId());
                    if (university.isPresent()) {
                        club.setUniversity(university.get());
                    }
                }

                Club updatedClub = clubRepository.save(club);
                return new ResponseEntity<>(updatedClub, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE club
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClub(@PathVariable UUID id) {
        try {
            Optional<Club> club = clubRepository.findById(id);
            if (club.isPresent()) {
                clubRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET club members
    @GetMapping("/{id}/members")
    public ResponseEntity<List<User>> getClubMembers(@PathVariable UUID id) {
        try {
            List<User> members = userRepository.findByClubId(id);
            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET club posts
    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getClubPosts(@PathVariable UUID id) {
        try {
            List<Post> posts = postRepository.findByClubId(id);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT approve/reject club
    @PutMapping("/{id}/approval")
    public ResponseEntity<Club> updateClubApproval(@PathVariable UUID id, 
                                                  @RequestParam Boolean approved) {
        try {
            Optional<Club> optionalClub = clubRepository.findById(id);
            if (optionalClub.isPresent()) {
                Club club = optionalClub.get();
                club.setIsApproved(approved);
                club.setUpdatedAt(Timestamp.from(Instant.now()));
                Club updatedClub = clubRepository.save(club);
                return new ResponseEntity<>(updatedClub, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Advanced search/filtering endpoint (Enhanced)
    @GetMapping("/search")
    public ResponseEntity<List<Club>> searchClubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) UUID universityId,
            @RequestParam(required = false) Boolean isApproved,
            @RequestParam(required = false) String universityName,
            @RequestParam(required = false) String countryName) {
        try {
            Specification<Club> spec = Specification.where(null);

            if (name != null && !name.trim().isEmpty()) {
                spec = spec.and((root, query, cb) -> 
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            
            if (category != null && !category.trim().isEmpty()) {
                spec = spec.and((root, query, cb) -> 
                    cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            }
            
            if (universityId != null) {
                spec = spec.and((root, query, cb) -> 
                    cb.equal(root.get("university").get("universityId"), universityId));
            }
            
            if (isApproved != null) {
                spec = spec.and((root, query, cb) -> 
                    cb.equal(root.get("isApproved"), isApproved));
            }
            
            if (universityName != null && !universityName.trim().isEmpty()) {
                spec = spec.and((root, query, cb) -> 
                    cb.like(cb.lower(root.get("university").get("universityName")), 
                           "%" + universityName.toLowerCase() + "%"));
            }
            
            if (countryName != null && !countryName.trim().isEmpty()) {
                spec = spec.and((root, query, cb) -> 
                    cb.like(cb.lower(root.get("university").get("country").get("countryName")), 
                           "%" + countryName.toLowerCase() + "%"));
            }

            List<Club> clubs = clubRepository.findAll(spec);
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET clubs by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Club>> getClubsByCategory(@PathVariable String category) {
        try {
            Specification<Club> spec = (root, query, cb) -> 
                cb.equal(cb.lower(root.get("category")), category.toLowerCase());
            List<Club> clubs = clubRepository.findAll(spec);
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET clubs by university
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<Club>> getClubsByUniversity(@PathVariable UUID universityId) {
        try {
            Specification<Club> spec = (root, query, cb) -> 
                cb.equal(root.get("university").get("universityId"), universityId);
            List<Club> clubs = clubRepository.findAll(spec);
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Inner class for request body
    public static class ClubRequest {
        private String name;
        private String description;
        private String category;
        private String contactEmail;
        private UUID universityId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getContactEmail() { return contactEmail; }
        public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
        public UUID getUniversityId() { return universityId; }
        public void setUniversityId(UUID universityId) { this.universityId = universityId; }
    }
}