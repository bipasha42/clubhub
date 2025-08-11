package com.example.Clubhub3.Service;
import com.example.Clubhub3.model.University;
import com.example.Clubhub3.repo.UniversityUniadminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UniversityUniadminService {
    
    @Autowired
    private UniversityUniadminRepository universityRepository;
    
    public List<University> getAllUniversities() {
        return universityRepository.findAllWithCountry();
    }
    
    public University getUniversityById(UUID id) {
        return universityRepository.findByIdWithCountry(id);
    }
}


