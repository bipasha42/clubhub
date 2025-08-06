package com.example.clubhub.controller;

import com.example.clubhub.model.Club;
import com.example.clubhub.model.Country;
import com.example.clubhub.model.University;
import com.example.clubhub.repository.ClubRepository;
import com.example.clubhub.repository.CountryRepository;
import com.example.clubhub.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Controller
public class DashboardController {

    private final CountryRepository countryRepository;
    private final UniversityRepository universityRepository;
    private final ClubRepository clubRepository;

    @Autowired
    public DashboardController(CountryRepository countryRepository,
                              UniversityRepository universityRepository,
                              ClubRepository clubRepository) {
        this.countryRepository = countryRepository;
        this.universityRepository = universityRepository;
        this.clubRepository = clubRepository;
    }

    // Show dashboard
    @GetMapping({"/dashboard", "/crud"})
    public String dashboard(Model model,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) UUID universityId,
                            @RequestParam(required = false) Boolean isApproved,
                            @RequestParam(required = false) String countryName) {

        // For search
        List<Club> searchResults = new ArrayList<>();
        if (name != null || category != null || universityId != null || isApproved != null || countryName != null) {
            Specification<Club> spec = Specification.where(null);

            if (name != null && !name.isEmpty())
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            if (category != null && !category.isEmpty())
                spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            if (universityId != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.get("university").get("universityId"), universityId));
            if (isApproved != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.get("isApproved"), isApproved));
            if (countryName != null && !countryName.isEmpty())
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("university").get("country").get("countryName")), "%" + countryName.toLowerCase() + "%"));

            searchResults = clubRepository.findAll(spec);
        }

        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("universities", universityRepository.findAll());
        model.addAttribute("clubs", clubRepository.findAll());
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("param", new HashMap<String, Object>() {{
            put("name", name);
            put("category", category);
            put("universityId", universityId);
            put("isApproved", isApproved);
            put("countryName", countryName);
        }});
        return "crud"; // crud.html in templates
    }

    // =========================
    // COUNTRY CRUD & EDIT
    // =========================

    @PostMapping("/countries")
    public String addCountry(@RequestParam String countryName) {
        Country country = new Country();
        country.setCountryName(countryName);
        countryRepository.save(country);
        return "redirect:/dashboard";
    }

    @PostMapping(value = "/countries/{id}", params = "_method=delete")
    public String deleteCountry(@PathVariable UUID id) {
        countryRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    // Show edit form for Country
    @GetMapping("/countries/edit/{id}")
    public String editCountryForm(@PathVariable UUID id, Model model) {
        Country country = countryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid country ID"));
        model.addAttribute("country", country);
        return "edit-country"; // edit-country.html in templates
    }

    // Handle edit form submission
    @PostMapping("/countries/edit/{id}")
    public String updateCountry(@PathVariable UUID id,
                               @RequestParam String countryName) {
        Country country = countryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid country ID"));
        country.setCountryName(countryName);
        countryRepository.save(country);
        return "redirect:/dashboard";
    }

    // =========================
    // UNIVERSITY CRUD & EDIT
    // =========================

    @PostMapping("/universities")
    public String addUniversity(@RequestParam String universityName, @RequestParam UUID countryId) {
        University university = new University();
        university.setUniversityName(universityName);
        countryRepository.findById(countryId).ifPresent(university::setCountry);
        universityRepository.save(university);
        return "redirect:/dashboard";
    }

    @PostMapping(value = "/universities/{id}", params = "_method=delete")
    public String deleteUniversity(@PathVariable UUID id) {
        universityRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    // Show edit form for University
    @GetMapping("/universities/edit/{id}")
    public String editUniversityForm(@PathVariable UUID id, Model model) {
        University university = universityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid university ID"));
        model.addAttribute("university", university);
        model.addAttribute("countries", countryRepository.findAll());
        return "edit-university"; // edit-university.html in templates
    }

    // Handle edit form submission
    @PostMapping("/universities/edit/{id}")
    public String updateUniversity(@PathVariable UUID id,
                                  @RequestParam String universityName,
                                  @RequestParam UUID countryId) {
        University university = universityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid university ID"));
        university.setUniversityName(universityName);
        countryRepository.findById(countryId).ifPresent(university::setCountry);
        universityRepository.save(university);
        return "redirect:/dashboard";
    }

    // =========================
    // CLUB CRUD & EDIT
    // =========================

    @PostMapping("/clubs")
    public String addClub(@RequestParam String name,
                          @RequestParam String description,
                          @RequestParam String category,
                          @RequestParam UUID universityId,
                          @RequestParam String contactEmail) {
        Club club = new Club();
        club.setId(UUID.randomUUID());
        club.setName(name);
        club.setDescription(description);
        club.setCategory(category);
        club.setContactEmail(contactEmail);
        club.setIsApproved(false);
        club.setCreatedAt(Timestamp.from(Instant.now()));
        club.setUpdatedAt(Timestamp.from(Instant.now()));
        universityRepository.findById(universityId).ifPresent(club::setUniversity);
        clubRepository.save(club);
        return "redirect:/dashboard";
    }

    @PostMapping(value = "/clubs/{id}", params = "_method=delete")
    public String deleteClub(@PathVariable UUID id) {
        clubRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    // Club approval toggle
    @PostMapping("/clubs/{id}/approval")
    public String approveClub(@PathVariable UUID id, @RequestParam boolean approved) {
        clubRepository.findById(id).ifPresent(club -> {
            club.setIsApproved(approved);
            club.setUpdatedAt(Timestamp.from(Instant.now()));
            clubRepository.save(club);
        });
        return "redirect:/dashboard";
    }

    // Show edit form for Club
    @GetMapping("/clubs/edit/{id}")
    public String editClubForm(@PathVariable UUID id, Model model) {
        Club club = clubRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid club ID"));
        model.addAttribute("club", club);
        model.addAttribute("universities", universityRepository.findAll());
        return "edit-club"; // edit-club.html in templates
    }

    // Handle edit form submission
    @PostMapping("/clubs/edit/{id}")
    public String updateClub(@PathVariable UUID id,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String category,
                             @RequestParam UUID universityId,
                             @RequestParam String contactEmail) {
        Club club = clubRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid club ID"));
        club.setName(name);
        club.setDescription(description);
        club.setCategory(category);
        club.setContactEmail(contactEmail);
        club.setUpdatedAt(Timestamp.from(Instant.now()));
        universityRepository.findById(universityId).ifPresent(club::setUniversity);
        clubRepository.save(club);
        return "redirect:/dashboard";
    }
}