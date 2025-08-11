package com.example.Clubhub3.controller;

import com.example.Clubhub3.dto.ClubAdminUniadminDTO;
import com.example.Clubhub3.Service.ClubAdminUniadminService;
import com.example.Clubhub3.Service.UniversityUniadminService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/uniadmin/club-admins")
public class ClubAdminUniadminController {

    @Autowired
    private ClubAdminUniadminService clubAdminService;

    @Autowired
    private UniversityUniadminService universityService;

    @GetMapping
    public String listClubAdmins(Model model) {
        try {
            List<ClubAdminUniadminDTO> clubAdmins = clubAdminService.getAllClubAdmins();
            model.addAttribute("clubAdmins", clubAdmins);
            System.out.println("Found " + (clubAdmins != null ? clubAdmins.size() : 0) + " club admins");
        } catch (Exception e) {
            System.err.println("Error loading club admins: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading club admins: " + e.getMessage());
        }
        return "uniadmin/club-admins/list";
    }

    @GetMapping("/add")
    public String showAddClubAdminForm(Model model) {
        try {
            model.addAttribute("clubAdminDTO", new ClubAdminUniadminDTO());
            model.addAttribute("universities", universityService.getAllUniversities());
        } catch (Exception e) {
            System.err.println("Error loading add form: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading form: " + e.getMessage());
        }
        return "uniadmin/club-admins/add";
    }

    @PostMapping("/add")
    public String addClubAdmin(@Valid @ModelAttribute("clubAdminDTO") ClubAdminUniadminDTO clubAdminDTO,
                               BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            try {
                model.addAttribute("universities", universityService.getAllUniversities());
            } catch (Exception e) {
                System.err.println("Error loading universities for add form: " + e.getMessage());
                model.addAttribute("errorMessage", "Error loading universities: " + e.getMessage());
            }
            return "uniadmin/club-admins/add";
        }

        try {
            clubAdminService.createClubAdmin(clubAdminDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Club admin added successfully!");
            return "redirect:/uniadmin/club-admins";
        } catch (Exception e) {
            System.err.println("Error creating club admin: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error creating club admin: " + e.getMessage());
            try {
                model.addAttribute("universities", universityService.getAllUniversities());
            } catch (Exception ex) {
                System.err.println("Error loading universities after create error: " + ex.getMessage());
                model.addAttribute("errorMessage", "Error loading universities: " + ex.getMessage());
            }
            return "uniadmin/club-admins/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditClubAdminForm(@PathVariable UUID id, Model model) {
        try {
            ClubAdminUniadminDTO clubAdmin = clubAdminService.getClubAdminById(id);
            if (clubAdmin == null) {
                System.err.println("Club admin not found with ID: " + id);
                return "redirect:/uniadmin/club-admins";
            }

            // Clear password for editing (security best practice)
            clubAdmin.setPassword("");

            model.addAttribute("clubAdminDTO", clubAdmin);
            model.addAttribute("universities", universityService.getAllUniversities());
        } catch (Exception e) {
            System.err.println("Error loading club admin for edit: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading club admin: " + e.getMessage());
        }
        return "uniadmin/club-admins/edit";
    }

    @PostMapping("/edit/{id}")
    public String editClubAdmin(@PathVariable UUID id,
                                @ModelAttribute("clubAdminDTO") ClubAdminUniadminDTO clubAdminDTO,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        // Manual validation for edit scenario
        if (clubAdminDTO.getFirstName() == null || clubAdminDTO.getFirstName().trim().isEmpty()) {
            result.rejectValue("firstName", "error.firstName", "First name is required");
        }

        if (clubAdminDTO.getLastName() == null || clubAdminDTO.getLastName().trim().isEmpty()) {
            result.rejectValue("lastName", "error.lastName", "Last name is required");
        }

        if (clubAdminDTO.getEmail() == null || clubAdminDTO.getEmail().trim().isEmpty()) {
            result.rejectValue("email", "error.email", "Email is required");
        }

        if (clubAdminDTO.getUniversityId() == null) {
            result.rejectValue("universityId", "error.universityId", "University is required");
        }

        if (result.hasErrors()) {
            try {
                model.addAttribute("universities", universityService.getAllUniversities());
            } catch (Exception e) {
                System.err.println("Error loading universities for edit form: " + e.getMessage());
                model.addAttribute("errorMessage", "Error loading universities: " + e.getMessage());
            }
            return "uniadmin/club-admins/edit";
        }

        try {
            clubAdminService.updateClubAdmin(id, clubAdminDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Club admin updated successfully!");
            return "redirect:/uniadmin/club-admins";
        } catch (Exception e) {
            System.err.println("Error updating club admin: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error updating club admin: " + e.getMessage());
            try {
                model.addAttribute("universities", universityService.getAllUniversities());
            } catch (Exception ex) {
                System.err.println("Error loading universities after update error: " + ex.getMessage());
                model.addAttribute("errorMessage", "Error loading universities: " + ex.getMessage());
            }
            return "uniadmin/club-admins/edit";
        }
    }

    @GetMapping("/view/{id}")
    public String viewClubAdmin(@PathVariable UUID id, Model model) {
        try {
            ClubAdminUniadminDTO clubAdmin = clubAdminService.getClubAdminById(id);
            if (clubAdmin == null) {
                System.err.println("Club admin not found with ID: " + id);
                model.addAttribute("error", "Club admin not found");
                return "uniadmin/club-admins/view";
            }

            model.addAttribute("clubAdmin", clubAdmin);
            System.out.println("Viewing club admin: " + clubAdmin.getFirstName() + " " + clubAdmin.getLastName());
        } catch (Exception e) {
            System.err.println("Error loading club admin: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading club admin: " + e.getMessage());
        }
        return "uniadmin/club-admins/view";
    }

    @PostMapping("/delete/{id}")
    public String deleteClubAdmin(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            clubAdminService.deleteClubAdmin(id);
            redirectAttributes.addFlashAttribute("successMessage", "Club admin deleted successfully!");
            System.out.println("Successfully deleted club admin with ID: " + id);
        } catch (Exception e) {
            System.err.println("Error deleting club admin: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting club admin: " + e.getMessage());
        }
        return "redirect:/uniadmin/club-admins";
    }

    // API endpoint for AJAX calls (optional)
    @GetMapping("/api")
    @ResponseBody
    public List<ClubAdminUniadminDTO> getAllClubAdminsApi() {
        try {
            return clubAdminService.getAllClubAdmins();
        } catch (Exception e) {
            System.err.println("Error in API endpoint: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }

    // API endpoint for getting club admins by university (for AJAX)
    @GetMapping("/api/by-university/{universityId}")
    @ResponseBody
    public List<ClubAdminUniadminDTO> getClubAdminsByUniversity(@PathVariable UUID universityId) {
        try {
            return clubAdminService.getClubAdminsByUniversity(universityId);
        } catch (Exception e) {
            System.err.println("Error getting club admins by university: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
}