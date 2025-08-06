package com.example.clubhub.controller;
import com.example.clubhub.model.University;
import com.example.clubhub.model.Country;
import com.example.clubhub.repository.UniversityRepository;
import com.example.clubhub.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/universities")
@CrossOrigin(origins = "*")
public class UniversityController {
    
    private final UniversityRepository universityRepository;
    private final CountryRepository countryRepository;
    
    @Autowired
    public UniversityController(UniversityRepository universityRepository, 
                              CountryRepository countryRepository) {
        this.universityRepository = universityRepository;
        this.countryRepository = countryRepository;
    }
    
    // GET all universities
    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        try {
            List<University> universities = universityRepository.findAll();
            return new ResponseEntity<>(universities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET universities by country
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<University>> getUniversitiesByCountry(@PathVariable UUID countryId) {
        try {
            List<University> universities = universityRepository.findByCountry_CountryId(countryId);
            return new ResponseEntity<>(universities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET university by ID
    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable UUID id) {
        try {
            Optional<University> university = universityRepository.findById(id);
            if (university.isPresent()) {
                return new ResponseEntity<>(university.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // POST create new university
    @PostMapping
    public ResponseEntity<University> createUniversity(@Validated @RequestBody UniversityRequest request) {
        try {
            Optional<Country> country = countryRepository.findById(request.getCountryId());
            if (!country.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            University university = new University();
            university.setUniversityName(request.getUniversityName());
            university.setCountry(country.get());
            
            University savedUniversity = universityRepository.save(university);
            return new ResponseEntity<>(savedUniversity, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // PUT update university
    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable UUID id, 
                                                      @Validated @RequestBody UniversityRequest request) {
        try {
            Optional<University> optionalUniversity = universityRepository.findById(id);
            if (optionalUniversity.isPresent()) {
                University university = optionalUniversity.get();
                university.setUniversityName(request.getUniversityName());
                
                if (request.getCountryId() != null) {
                    Optional<Country> country = countryRepository.findById(request.getCountryId());
                    if (country.isPresent()) {
                        university.setCountry(country.get());
                    }
                }
                
                University updatedUniversity = universityRepository.save(university);
                return new ResponseEntity<>(updatedUniversity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // DELETE university
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUniversity(@PathVariable UUID id) {
        try {
            Optional<University> university = universityRepository.findById(id);
            if (university.isPresent()) {
                universityRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Inner class for request body
    public static class UniversityRequest {
        private String universityName;
        private UUID countryId;
        
        public String getUniversityName() { return universityName; }
        public void setUniversityName(String universityName) { this.universityName = universityName; }
        public UUID getCountryId() { return countryId; }
        public void setCountryId(UUID countryId) { this.countryId = countryId; }
    }
}
