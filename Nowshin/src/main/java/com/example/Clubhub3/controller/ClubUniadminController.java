
package com.example.Clubhub3.controller;

import com.example.Clubhub3.dto.ClubUniadminDTO;
import com.example.Clubhub3.dto.ClubWithAdminUniadminDTO;
import com.example.Clubhub3.model.User;
import com.example.Clubhub3.Service.ClubAdminUniadminService;
import com.example.Clubhub3.Service.ClubUniadminService;
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
@RequestMapping("/uniadmin/clubs")
public class ClubUniadminController {
    
    @Autowired
    private ClubUniadminService clubService;
    
    @Autowired
    private ClubAdminUniadminService clubAdminService;
    
    @Autowired
    private UniversityUniadminService universityService;
    
    @GetMapping
    public String listClubs(Model model) {
        model.addAttribute("clubs", clubService.getAllClubs());
        return "uniadmin/clubs/list";
    }
    
    @GetMapping("/add")
    public String showAddClubForm(Model model) {
        model.addAttribute("clubDTO", new ClubUniadminDTO());
        model.addAttribute("universities", universityService.getAllUniversities());
        model.addAttribute("clubAdmins", clubAdminService.getAllClubAdmins());
        return "uniadmin/clubs/add";
    }
    
    @PostMapping("/add")
    public String addClub(@Valid @ModelAttribute("clubDTO") ClubUniadminDTO clubDTO,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("universities", universityService.getAllUniversities());
            model.addAttribute("clubAdmins", clubAdminService.getAllClubAdmins());
            return "uniadmin/clubs/add";
        }
        
        try {
            clubService.createClub(clubDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Club added successfully!");
            return "redirect:/uniadmin/clubs";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("universities", universityService.getAllUniversities());
            model.addAttribute("clubAdmins", clubAdminService.getAllClubAdmins());
            return "uniadmin/clubs/add";
        }
    }
    
    @GetMapping("/add-with-admin")
    public String showAddClubWithAdminForm(Model model) {
        model.addAttribute("clubWithAdminDTO", new ClubWithAdminUniadminDTO());
        model.addAttribute("universities", universityService.getAllUniversities());
        return "uniadmin/clubs/add-with-admin";
    }
    
    @PostMapping("/add-with-admin")
    public String addClubWithAdmin(@Valid @ModelAttribute("clubWithAdminDTO") ClubWithAdminUniadminDTO clubWithAdminDTO,
                                  BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("universities", universityService.getAllUniversities());
            return "uniadmin/clubs/add-with-admin";
        }
        
        try {
            clubService.createClubWithAdmin(clubWithAdminDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Club and admin created successfully!");
            return "redirect:/uniadmin/clubs";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("universities", universityService.getAllUniversities());
            return "uniadmin/clubs/add-with-admin";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditClubForm(@PathVariable UUID id, Model model) {
        ClubUniadminDTO club = clubService.getClubById(id);
        if (club == null) {
            return "redirect:/uniadmin/clubs";
        }
        
        model.addAttribute("clubDTO", club);
        model.addAttribute("universities", universityService.getAllUniversities());
        model.addAttribute("clubAdmins", clubAdminService.getAllClubAdmins());
        return "uniadmin/clubs/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String editClub(@PathVariable UUID id, @Valid @ModelAttribute("clubDTO") ClubUniadminDTO clubDTO,
                          BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("universities", universityService.getAllUniversities());
            model.addAttribute("clubAdmins", clubAdminService.getAllClubAdmins());
            return "uniadmin/clubs/edit";
        }
        
        try {
            clubService.updateClub(id, clubDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Club updated successfully!");
            return "redirect:/uniadmin/clubs";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("universities", universityService.getAllUniversities());
            model.addAttribute("clubAdmins", clubAdminService.getAllClubAdmins());
            return "uniadmin/clubs/edit";
        }
    }
    
    @GetMapping("/view/{id}")
    public String viewClub(@PathVariable UUID id, Model model) {
        ClubUniadminDTO club = clubService.getClubById(id);
        if (club == null) {
            return "redirect:/uniadmin/clubs";
        }
        
        model.addAttribute("club", club);
        return "uniadmin/clubs/view";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteClub(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            clubService.deleteClub(id);
            redirectAttributes.addFlashAttribute("successMessage", "Club deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/uniadmin/clubs";
    }
    
    @GetMapping("/api/admins-by-university/{universityId}")
    @ResponseBody
    public List<User> getClubAdminsByUniversity(@PathVariable UUID universityId) {
        return clubAdminService.getAvailableClubAdmins(universityId);
    }
}