
package com.example.clubhub.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.clubhub.model.University;

public interface UniversityRepository extends JpaRepository<University, UUID> {
    
    // Find universities by country ID
    List<University> findByCountry_CountryId(UUID countryId);
    
    // Find universities by country name
    @Query("SELECT u FROM University u WHERE u.country.countryName = :countryName")
    List<University> findByCountryName(@Param("countryName") String countryName);
    
    // Search universities by name (case-insensitive)
    List<University> findByUniversityNameContainingIgnoreCase(String universityName);
}