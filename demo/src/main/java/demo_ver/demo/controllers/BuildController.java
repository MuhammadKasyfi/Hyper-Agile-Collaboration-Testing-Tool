package demo_ver.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.Build;
import demo_ver.demo.service.BuildService;

@Controller
public class BuildController {

    @Autowired
    private BuildService buildService;

    // Utility method to check if a user has a specific role
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ROLE_" + role));
    }

    // View the list of builds
    @GetMapping("/viewBuilds")
    public String viewBuilds(@RequestParam(required = false) String search, Model model) {
        // Call service to filter builds based on search
        List<Build> builds = buildService.filterBuilds(search != null ? search.trim() : "");
        model.addAttribute("builds", builds);
        
        model.addAttribute("search", search);


        return "viewBuilds";
    }

    // Create Build - GET
    @GetMapping("/createBuild")
    public String createBuildForm(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (!hasRole(authentication, "ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "You do not have permission to create builds.");
            return "redirect:/viewBuilds";
        }
        return "createBuild";
    }

    // Create Build - POST
    @PostMapping("/createBuild")
    public String createBuild(@RequestParam String title,
            @RequestParam String description,
            @RequestParam String releaseDate,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) String isOpen,
            RedirectAttributes redirectAttributes) {
        try {
            if (title.isEmpty() || description.isEmpty() || releaseDate.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "All fields except Active and Open are required.");
                return "redirect:/createBuild";
            }

            // Store the values as strings
            isActive = (isActive != null && !isActive.isEmpty()) ? isActive : "false";
            isOpen = (isOpen != null && !isOpen.isEmpty()) ? isOpen : "false";

            buildService.createBuild(title, description, releaseDate, isActive, isOpen);
            redirectAttributes.addFlashAttribute("success", "Build created successfully.");
            return "redirect:/viewBuilds";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while creating the build.");
            return "redirect:/createBuild";
        }
    }

    // Edit Build - GET
    @GetMapping("/editBuild")
    public String editBuildForm(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Build build = buildService.viewBuildById(id);
            model.addAttribute("build", build);
            return "editBuild";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Build not found.");
            return "redirect:/viewBuilds";
        }
    }

    // Delete Build
    @PostMapping("/deleteBuild")
    public String deleteBuild(@RequestParam String id, RedirectAttributes redirectAttributes) {
        if (buildService.deleteBuild(id)) {
            redirectAttributes.addFlashAttribute("success", "Build deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete build.");
        }
        return "redirect:/viewBuilds";
    }

    // View Build Details
    @GetMapping("/viewBuildDetails/{id}")
    public String viewBuildDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Build build = buildService.viewBuildById(id);
            model.addAttribute("build", build);

            return "viewBuildDetails";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Build not found.");
            return "redirect:/viewBuilds";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewBuilds";
        }
    }
    
}
